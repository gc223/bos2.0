package cn.itcast.bos.web.action.base;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.web.action.common.BaseAction;
import cn.itcast.crm.domain.Customer;

@ParentPackage("struts-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class CustomerAction extends BaseAction<Customer> {


    @Action(value = "customer_sendMsg", results = {
            @Result(type = "redirect", location = "./signup-success.html"),
            @Result(name = "error", type = "redirect", location = "./signup.html")})
    public String sendMsg() {
        System.out.println("成功");
        return SUCCESS;
    }
}
