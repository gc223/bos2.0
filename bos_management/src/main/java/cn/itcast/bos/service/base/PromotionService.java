package cn.itcast.bos.service.base;

import cn.itcast.bos.domain.page.PageBean;
import cn.itcast.bos.domain.take_delivery.Promotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.ws.rs.*;
import java.util.Date;

public interface PromotionService {

    //保存宣传任务
    void save(Promotion model);

    //分页查询
    Page<Promotion> pageQuery(PageRequest pageable);

    //修改宣传任务过期
    void updateStatus(Date date);

    @Path("/pageQuery")
    @GET
    @Produces({"application/xml", "application/json"})
    PageBean<Promotion> findPageData(@QueryParam("page") int page, @QueryParam("rows") int rows);

    @Path("/promotion/{id}")
    @GET
    @Produces({"application/xml", "application/json"})
    Promotion findById(@PathParam("id") Integer id);
}
