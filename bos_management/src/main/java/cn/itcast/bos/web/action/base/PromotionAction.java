package cn.itcast.bos.web.action.base;

import cn.itcast.bos.domain.take_delivery.Promotion;
import cn.itcast.bos.service.base.PromotionService;
import cn.itcast.bos.web.action.common.BaseAction;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class PromotionAction extends BaseAction<Promotion> {
    @Autowired
    private PromotionService promotionService;

    private File titleImgFile;
    private String titleImgFileFileName;
    private String titleImgFileContentType;

    @Action(value = "promotion_pageQuery", results = {@Result(type = "json")})
    public String pageQuery() {
//        System.out.println(page);
//        System.out.println(rows);
        PageRequest pageable = new PageRequest(page - 1, rows);
        Page<Promotion> pageData = promotionService.pageQuery(pageable);
        this.pushPageDataToValueStack(pageData);
        return SUCCESS;
    }


    @Action(value = "promotion_save", results = {@Result(type = "redirect", location = "./pages/take_delivery/promotion.html")})
    public String save() throws IOException {
        System.out.println(titleImgFile);
        System.out.println(titleImgFileFileName);
        System.out.println(titleImgFileContentType);
        System.out.println(model);
        String savePath = ServletActionContext.getServletContext().getRealPath("/upload/");
        String saveUrl = ServletActionContext.getServletContext().getContextPath() + "/upload/";
        String imgName = UUID.randomUUID().toString() + "_" + titleImgFileFileName;
        FileUtils.copyFile(titleImgFile, new File(savePath + "/" + imgName));
        model.setTitleImg(saveUrl + imgName);
        promotionService.save(model);
        return SUCCESS;
    }

    public void setTitleImgFile(File titleImgFile) {
        this.titleImgFile = titleImgFile;
    }

    public void setTitleImgFileFileName(String titleImgFileFileName) {
        this.titleImgFileFileName = titleImgFileFileName;
    }

    public void setTitleImgFileContentType(String titleImgFileContentType) {
        this.titleImgFileContentType = titleImgFileContentType;
    }
}
