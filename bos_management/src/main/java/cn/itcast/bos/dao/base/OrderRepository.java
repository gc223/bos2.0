package cn.itcast.bos.dao.base;

import cn.itcast.bos.domain.take_delivery.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    Order findByOrderNum(String orderNum);
}
