package cn.itcast.bos.service.base;

import cn.itcast.bos.domain.take_delivery.Order;

public interface WorkBillService {
    void generateWorkBill(Order order);
}
