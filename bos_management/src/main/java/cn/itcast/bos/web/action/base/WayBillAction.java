package cn.itcast.bos.web.action.base;

import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.service.base.WayBillService;
import cn.itcast.bos.web.action.common.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class WayBillAction extends BaseAction<WayBill> {
    private static final Logger LOGGER = Logger.getLogger(WayBillAction.class);

    @Autowired
    private WayBillService wayBillService;

    @Action(value = "waybill_save", results = {@Result(type = "json")})
    public String save() {
        Map<String, Object> result = new HashMap<>();
        try {
            if (model.getOrder() != null && model.getOrder().getId() == null || model.getOrder().getId() == 0) {
                model.setOrder(null);
            }
            wayBillService.save(model);
            result.put("success", true);
            result.put("msg", "保存运单成功");
            LOGGER.info("保存运单成功，运单号为：" + model.getWayBillNum());
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("msg", "保存运单失败");
            LOGGER.error("保存运单失败，运单号为：" + model.getWayBillNum());
        }
        ActionContext.getContext().getValueStack().push(result);
        return SUCCESS;
    }

    @Action(value = "waybill_pageQuery", results = {@Result(type = "json")})
    public String pageQuery() {
        Pageable pageable = new PageRequest(page - 1, rows, new Sort(Sort.Direction.ASC));
        Page<WayBill> page = wayBillService.pageQuery(model, pageable);
        pushPageDataToValueStack(page);
        return SUCCESS;
    }

    @Action(value = "waybill_findByWayBillNum", results = {@Result(type = "json")})
    public String findByWayBillNum() {
        WayBill wayBill = wayBillService.findByWayBillNum(model.getWayBillNum());
        Map<String, Object> result = new HashMap<>();
        if (wayBill != null) {
            result.put("success", true);
            result.put("wayBillData", wayBill);
        } else {
            result.put("success", false);
        }
        ActionContext.getContext().getValueStack().push(result);
        return SUCCESS;
    }
}
