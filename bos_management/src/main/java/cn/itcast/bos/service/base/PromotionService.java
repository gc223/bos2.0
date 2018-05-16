package cn.itcast.bos.service.base;

import cn.itcast.bos.domain.take_delivery.Promotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Date;

public interface PromotionService {
    void save(Promotion model);

    Page<Promotion> pageQuery(PageRequest pageable);

    void updateStatus(Date date);
}
