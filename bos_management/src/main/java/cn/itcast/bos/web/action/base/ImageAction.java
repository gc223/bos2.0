package cn.itcast.bos.web.action.base;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class ImageAction extends ActionSupport {

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
        String savePath = ServletActionContext.getServletContext().getRealPath("/upload");
        String urlPath = ServletActionContext.getServletContext().getContextPath() + "/upload/" + imgFileFileName;
        System.out.println("图片访问路径:" + urlPath);
        System.out.println("图片保存路径:" + savePath);
        try {
            FileUtils.copyFile(new File(imgFile), new File(savePath + "/" + imgFileFileName));
            Map<String, Object> map = new HashMap<>();
            map.put("error", 0);
            map.put("url", urlPath);
            ActionContext.getContext().getValueStack().push(map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return SUCCESS;
    }

    @Action(value = "image_manager", results = {@Result(type = "json")})
    public String picmanage() {
        String realPath = ServletActionContext.getServletContext().getRealPath("/upload/");
        File directory = new File(realPath);
        List<Map<String, Object>> fileList = new ArrayList<>();
        if (directory.listFiles() != null) {
            for (File file : directory.listFiles()) {
                Map<String, Object> hash = new HashMap<>();
                String fileName = file.getName();
                if (file.isDirectory()) {
                    hash.put("is_dir", true);
                    hash.put("has_file", (file.listFiles() != null));
                    hash.put("filesize", 0L);
                    hash.put("is_photo", false);
                    hash.put("filetype", "");
                } else {
                    String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
                    hash.put("is_dir", false);
                    hash.put("has_file", false);
                    hash.put("filesize", file.length());
                    hash.put("is_photo", true);
                    hash.put("filetype", fileExt);
                }
                hash.put("filename", fileName);
                hash.put("datetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file.lastModified()));
                fileList.add(hash);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("moveup_dir_path", "");
        result.put("current_dir_path", realPath);
        result.put("current_url", ServletActionContext.getRequest().getContextPath() + "/upload/");
        result.put("total_count", fileList.size());
        result.put("file_list", fileList);

        ActionContext.getContext().getValueStack().push(result);

        return SUCCESS;
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
