package cn.itcast.bos.web.action.base;

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.domain.constant.Constants;
import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.bos.web.action.common.BaseAction;
import cn.itcast.crm.domain.Customer;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.ws.rs.core.MediaType;
import java.util.Date;

@ParentPackage("struts-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class OrderAction extends BaseAction<Order> {

    private String sendAreaInfo;
    private String recAreaInfo;


    @Action(value = "order_save", results = {@Result(type = "redirect", location = "index.html#/myhome")})
    public String saveOrder() {
        model.setOrderTime(new Date());

        Area sendArea = new Area();
        String[] strings = sendAreaInfo.split("/");
        sendArea.setProvince(strings[0]);
        sendArea.setCity(strings[1]);
        sendArea.setDistrict(strings[2]);
        model.setSendArea(sendArea);

        Area recArea = new Area();
        String[] strings1 = recAreaInfo.split("/");
        recArea.setProvince(strings1[0]);
        recArea.setCity(strings1[1]);
        recArea.setDistrict(strings1[2]);
        model.setRecArea(recArea);

        Customer customer = (Customer) ServletActionContext.getRequest().getSession().getAttribute("customer");
        if (customer != null) {
            model.setCustomer_id(customer.getId());
        }

        System.out.println(model);
        WebClient.create(Constants.BOS_MANAGEMENT_URL + "/services/orderService/save").type(MediaType.APPLICATION_JSON).post(model);
        return SUCCESS;
    }

    public void setSendAreaInfo(String sendAreaInfo) {
        this.sendAreaInfo = sendAreaInfo;
    }

    public void setRecAreaInfo(String recAreaInfo) {
        this.recAreaInfo = recAreaInfo;
    }
}
