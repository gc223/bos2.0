package cn.itcast.bos.web.action.base;

import cn.itcast.bos.utils.MailUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.web.action.common.BaseAction;
import cn.itcast.crm.domain.Customer;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@ParentPackage("struts-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class CustomerAction extends BaseAction<Customer> {

    @Autowired
    private JmsTemplate jmsQueueTemplate;
    private String msgCode;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    private String activeCode;

    @Action(value = "customer_sendMsg")
    public String sendMsg() {
        //生成短信随机码
        String randomCode = RandomStringUtils.randomNumeric(4);
        //生成短信内容
        final String msg = "尊敬的手机号为" + model.getTelephone() + "的客户，您的短信验证码为：" + randomCode;
        System.out.println(msg);
        //将短信验证码保存到session中
        ServletActionContext.getRequest().getSession().setAttribute("mgsCode", randomCode);

        jmsQueueTemplate.send("bos_sms", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                MapMessage message = session.createMapMessage();
                message.setString("telephone", model.getTelephone());
                message.setString("msg", msg);
                return message;
            }
        });
        return NONE;
    }

    @Action(value = "customer_register", results = {@Result(type = "redirect", location = "./signup-success.html"),
            @Result(name = "error", type = "redirect", location = "./signup.html")})
    public String register() {
        String mgsCode = (String) ServletActionContext.getRequest().getSession().getAttribute("mgsCode");
        if (mgsCode != null && mgsCode.equals(msgCode)) {
            WebClient.create("http://localhost:9002/crm_management/services/" +
                    "customerService/register").type(MediaType.APPLICATION_JSON_TYPE).post(model);
            String activeCode = RandomStringUtils.randomNumeric(16);

            redisTemplate.opsForValue().set(model.getTelephone(), activeCode, 24, TimeUnit.HOURS);
            String content = "尊敬的客户您好，请于24小时内进行邮箱绑定，点击下面的地址完成绑定：<br/><a href='" +
                    MailUtils.activeUrl + "?telephone=" + model.getTelephone() + "&activeCode=" + activeCode + "'>" +
                    "速运快递邮箱绑定地址</a>";
            MailUtils.sendMail("速运快递激活邮件", content, model.getEmail());
            return SUCCESS;
        }
        return ERROR;
    }

    @Action("customer_active")
    public String active() throws IOException {
        ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
        String getCode = redisTemplate.opsForValue().get(model.getTelephone());
        //激活码错误或超过24小时
        if (getCode == null || !getCode.equals(activeCode)) {
            ServletActionContext.getResponse().getWriter().write("激活码无效，请登录后重新绑定邮箱！");
        } else {
            WebClient.create("http://localhost:9002/crm_management/services/" +
                    "customerService/active").type(MediaType.APPLICATION_JSON_TYPE).put(model);
//            redisTemplate.delete(model.getTelephone());
            ServletActionContext.getResponse().getWriter().write("激活成功");
        }
        return NONE;
    }

    public void setMsgCode(String msgCode) {
        this.msgCode = msgCode;
    }

    public void setActiveCode(String activeCode) {
        this.activeCode = activeCode;
    }
}
