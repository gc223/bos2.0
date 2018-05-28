package cn.itcast.bos.service.base;

import cn.itcast.bos.domain.take_delivery.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Service
@Transactional
public interface OrderService {
    @Path("/save")
    @POST
    @Consumes({"application/json", "application/xml"})
    void saveOrder(Order order);

    Order findByOrderNum(String orderNum);
}
