package cn.itcast.bos.service.impl;

import cn.itcast.bos.dao.base.OrderRepository;
import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.domain.base.SubArea;
import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.bos.service.base.AreaService;
import cn.itcast.bos.service.base.OrderService;
import cn.itcast.bos.service.base.WorkBillService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.UUID;

public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private AreaService areaService;
    @Autowired
    private WorkBillService workBillService;

    @Override
    public void saveOrder(Order order) {
        order.setSendArea(areaService.findArea(order.getSendArea()));
        order.setRecArea(areaService.findArea(order.getRecArea()));

        Area area = areaService.findArea(order.getSendArea());
        for (SubArea subArea :
                area.getSubareas()) {
            if (order.getSendAddress().contains(subArea.getKeyWords())) {
                FixedArea fixedArea = subArea.getFixedArea();
                if (fixedArea != null) {
                    Set<Courier> couriers = fixedArea.getCouriers();
                    if (couriers != null) {
                        order.setCourier(couriers.iterator().next());
                        order.setOrderNum(UUID.randomUUID().toString().replaceAll("-", ""));
                        orderRepository.save(order);
                        workBillService.generateWorkBill(order);
                        return;
                    }
                }

            }
        }
        order.setOrderType("2");
        orderRepository.save(order);
    }

    @Override
    public Order findByOrderNum(String orderNum) {
        return orderRepository.findByOrderNum(orderNum);
    }
}
