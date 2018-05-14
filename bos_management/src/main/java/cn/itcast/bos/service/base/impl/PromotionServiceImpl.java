package cn.itcast.bos.service.base.impl;

import cn.itcast.bos.dao.base.PromotionRepository;
import cn.itcast.bos.domain.take_delivery.Promotion;
import cn.itcast.bos.service.base.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class PromotionServiceImpl implements PromotionService {
    @Autowired
    private PromotionRepository promotionRepository;

    @Override
    public void save(Promotion model) {
        promotionRepository.save(model);
    }

    @Override
    public Page<Promotion> pageQuery(PageRequest pageable) {
        return promotionRepository.findAll(pageable);
    }
}
