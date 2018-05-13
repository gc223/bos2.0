package cn.itcast.bos.web.action.base;

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
public class ImageAction {

    private String imgFile;
    private String imgFileFileName;
    private String imgFileContentType;


    /**
     * 实现文件上传到服务器
     *
     * @return 成功时返回url error = 0  失败时返回 error = 1 message
     */
    @Action(value = "image_upload", results = {@Result(type = "json")})
    public String upload() {
        System.out.println(imgFile);
        System.out.println(imgFileFileName);
        System.out.println(imgFileContentType);
        return "success";
    }

    public void setImgFile(String imgFile) {
        this.imgFile = imgFile;
    }

    public void setImgFileFileName(String imgFileFileName) {
        this.imgFileFileName = imgFileFileName;
    }

    public void setImgFileContentType(String imgFileContentType) {
        this.imgFileContentType = imgFileContentType;
    }
}
