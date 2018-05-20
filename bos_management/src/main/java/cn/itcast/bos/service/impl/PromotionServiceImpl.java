package cn.itcast.bos.service.impl;

import cn.itcast.bos.dao.base.PromotionRepository;
import cn.itcast.bos.domain.page.PageBean;
import cn.itcast.bos.domain.take_delivery.Promotion;
import cn.itcast.bos.service.base.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service("promotionService")
@Transactional
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

    @Override
    public void updateStatus(Date date) {
        promotionRepository.updateStatus(date);
    }

    @Override
    public PageBean<Promotion> findPageData(int page, int rows) {
        System.out.println("page:" + page);
        System.out.println("rows:" + rows);
        Pageable pageable = new PageRequest(page - 1, rows);
        System.out.println(pageable);
        Page<Promotion> pageData = promotionRepository.findAll(pageable);
        PageBean<Promotion> pageBean = new PageBean<>();
        pageBean.setPageData(pageData.getContent());
        pageBean.setTotalCount(pageData.getTotalElements());
        return pageBean;
    }

    @Override
    public Promotion findById(Integer id) {
        return promotionRepository.findOne(id);
    }
}
