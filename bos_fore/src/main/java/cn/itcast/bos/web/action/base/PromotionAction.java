package cn.itcast.bos.web.action.base;

import cn.itcast.bos.domain.constant.Constants;
import cn.itcast.bos.domain.page.PageBean;
import cn.itcast.bos.domain.take_delivery.Promotion;
import cn.itcast.bos.web.action.common.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.FileUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class PromotionAction extends BaseAction<Promotion> {

    @Action(value = "promotion_pageQuery", results = {@Result(type = "json")})
    public String pageQuery() {
        System.out.println(page);
        System.out.println(rows);
        PageBean<Promotion> pageBean = WebClient.create("http://localhost:9001/bos_management/services/promotionService/pageQuery?page=" + page + "&rows=" + rows).accept(MediaType.APPLICATION_JSON_TYPE).get(PageBean.class);
        ActionContext.getContext().getValueStack().push(pageBean);
        return SUCCESS;
    }

    @Action(value = "promotion_showDetail")
    public String showDetail() throws IOException, TemplateException {
        String htmlRealPath = ServletActionContext.getServletContext().getRealPath("/freemarker");
        File htmlFile = new File(htmlRealPath + "/" + model.getId() + ".html");
        if (!htmlFile.exists()) {
            Configuration configuration = new Configuration(Configuration.VERSION_2_3_22);
            configuration.setDirectoryForTemplateLoading(new File(ServletActionContext.getServletContext().getRealPath("/WEB-INF/freemarker_templates")));
            Template template = configuration.getTemplate("promotion_detail.ftl");
            Promotion promotion = WebClient.create(Constants.BOS_MANAGEMENT_HOST + "/bos_management/services/promotionService/promotion/" +
                    model.getId()).accept(MediaType.APPLICATION_JSON_TYPE).get(Promotion.class);
            Map<String, Object> parameteMap = new HashMap<>();
            parameteMap.put("promotion", promotion);
            template.process(parameteMap, new OutputStreamWriter(new FileOutputStream(htmlFile), "utf-8"));

        }
        ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
        FileUtils.copyFile(htmlFile, ServletActionContext.getResponse().getOutputStream());

        return NONE;
    }
}
