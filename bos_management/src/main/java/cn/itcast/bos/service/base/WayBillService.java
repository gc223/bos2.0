package cn.itcast.bos.service.base;

import cn.itcast.bos.domain.take_delivery.WayBill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WayBillService {
    void save(WayBill wayBill);

    Page<WayBill> pageQuery(Pageable pageable);

    WayBill findByWayBillNum(String wayBillNum);
}
