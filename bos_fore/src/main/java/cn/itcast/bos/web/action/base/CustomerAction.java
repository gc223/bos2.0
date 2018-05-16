package cn.itcast.bos.web.action.base;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.web.action.common.BaseAction;
import cn.itcast.crm.domain.Customer;

import javax.ws.rs.core.MediaType;

@ParentPackage("struts-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class CustomerAction extends BaseAction<Customer> {

    private String msgCode;

    @Action(value = "customer_sendMsg")
    public String sendMsg() {
        //生成短信随机码
        String randomCode = RandomStringUtils.randomNumeric(4);
        //生成短信内容
        String msg = "尊敬的手机号为" + model.getTelephone() + "的客户，您的短信验证码为：" + randomCode;
        System.out.println(msg);
        //将短信验证码保存到session中
        ServletActionContext.getRequest().getSession().setAttribute("mgsCode", randomCode);

        return NONE;
    }

    @Action(value = "customer_register", results = {@Result(type = "redirect", location = "./signup-success.html"),
            @Result(name = "error", type = "redirect", location = "./signup.html")})
    public String register() {
        String mgsCode = (String) ServletActionContext.getRequest().getSession().getAttribute("mgsCode");
        if (mgsCode != null && mgsCode.equals(msgCode)) {
            WebClient.create("http://localhost:9002/crm_management/services/" +
                    "customerService/register").type(MediaType.APPLICATION_JSON_TYPE).post(model);
            return SUCCESS;
        }
        return ERROR;
    }

    public void setMsgCode(String msgCode) {
        this.msgCode = msgCode;
    }
}
