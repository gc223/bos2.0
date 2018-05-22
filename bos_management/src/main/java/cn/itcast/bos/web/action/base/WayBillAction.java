package cn.itcast.bos.web.action.base;

import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.web.action.common.BaseAction;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class WayBillAction extends BaseAction<WayBill> {

    @Action(value = "waybill_save", results = {@Result(type = "json")})
    public String save() {
        System.out.println(model);

        return SUCCESS;
    }
}
