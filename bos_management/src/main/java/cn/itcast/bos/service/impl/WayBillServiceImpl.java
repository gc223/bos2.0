package cn.itcast.bos.service.impl;

import cn.itcast.bos.dao.base.WayBillRepository;
import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.index.WayBillIndexRepository;
import cn.itcast.bos.service.base.WayBillService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
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
    public Page<WayBill> pageQuery(WayBill wayBill, Pageable pageable) {
        if (StringUtils.isBlank(wayBill.getWayBillNum()) && StringUtils.isBlank(wayBill.getSendAddress())
                && StringUtils.isBlank(wayBill.getRecAddress()) && StringUtils.isBlank(wayBill.getSendProNum())
                && (wayBill.getSignStatus() == null || wayBill.getSignStatus() == 0)) {
            return wayBillRepository.findAll(pageable);
        } else {
            BoolQueryBuilder query = new BoolQueryBuilder();
            if (StringUtils.isNotBlank(wayBill.getWayBillNum())) {
                QueryBuilder termQuery = new TermQueryBuilder("wayBillNum", wayBill.getWayBillNum());
                query.must(termQuery);
            }
            if (StringUtils.isNotBlank(wayBill.getSendAddress())) {
                QueryBuilder wildQuery = new WildcardQueryBuilder("sendAddress", "*" + wayBill.getSendAddress() + "*");
                query.must(wildQuery);
            }
            if (StringUtils.isNotBlank(wayBill.getRecAddress())) {
                QueryBuilder wildQuery = new WildcardQueryBuilder("recAddress", "*" + wayBill.getRecAddress() + "*");
                query.must(wildQuery);
            }
            if (StringUtils.isNotBlank(wayBill.getSendProNum())) {
                QueryBuilder termQuery = new TermQueryBuilder("sendProNum", wayBill.getSendProNum());
                query.must(termQuery);
            }
            if (wayBill.getSignStatus() != null && wayBill.getSignStatus() != 0) {
                QueryBuilder termQuery = new TermQueryBuilder("signStatus", wayBill.getSignStatus());
                query.must(termQuery);
            }
            SearchQuery searchQuery = new NativeSearchQuery(query);
            searchQuery.setPageable(pageable);
            return wayBillIndexRepository.search(searchQuery);
        }

    }

    @Override
    public WayBill findByWayBillNum(String wayBillNum) {
        return wayBillRepository.findByWayBillNum(wayBillNum);
    }
}
