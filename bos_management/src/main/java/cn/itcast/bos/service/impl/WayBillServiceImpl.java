package cn.itcast.bos.service.impl;

import cn.itcast.bos.dao.base.WayBillRepository;
import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.index.WayBillIndexRepository;
import cn.itcast.bos.service.base.WayBillService;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;

@Service
@Transactional
public class WayBillServiceImpl implements WayBillService {
    @Autowired
    private WayBillRepository wayBillRepository;
    @Autowired
    private WayBillIndexRepository wayBillIndexRepository;

    @Override
    public void save(WayBill wayBill) {
        WayBill persisWayBill = wayBillRepository.findByWayBillNum(wayBill.getWayBillNum());
        if (persisWayBill == null || persisWayBill.getId() == null) {
            wayBillRepository.save(wayBill);
            wayBillIndexRepository.save(wayBill);
        } else {
            Integer id = persisWayBill.getId();
            try {
                BeanUtils.copyProperties(persisWayBill, wayBill);
                persisWayBill.setId(id);
                wayBillIndexRepository.save(persisWayBill);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public Page<WayBill> pageQuery(Pageable pageable) {
        return wayBillRepository.findAll(pageable);
    }

    @Override
    public WayBill findByWayBillNum(String wayBillNum) {
        return wayBillRepository.findByWayBillNum(wayBillNum);
    }
}
