package cn.itcast.bos.web.action.base;

import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.bos.service.base.OrderService;
import cn.itcast.bos.web.action.common.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class OrderAction extends BaseAction<Order> {
    @Autowired
    private OrderService orderService;

    @Action(value = "order_findByOrderNum", results = {@Result(type = "json")})
    public String findByOrderNum() {
        Order order = orderService.findByOrderNum(model.getOrderNum());
        Map<String, Object> result = new HashMap<>();
        if (order == null) {
            result.put("success", false);
        } else {
            result.put("success", true);
            result.put("orderData", order);
        }
        ActionContext.getContext().getValueStack().push(result);
        return SUCCESS;
    }
}
