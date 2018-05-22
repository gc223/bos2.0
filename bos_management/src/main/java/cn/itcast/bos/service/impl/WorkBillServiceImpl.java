package cn.itcast.bos.service.impl;

import cn.itcast.bos.dao.base.WorkBillRepository;
import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.bos.domain.take_delivery.WorkBill;
import cn.itcast.bos.service.base.WorkBillService;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import java.util.Date;

@Service
@Transactional
public class WorkBillServiceImpl implements WorkBillService {
    @Autowired
    private WorkBillRepository workBillRepository;
    @Autowired
    private JmsTemplate jmsQueueTemplate;

    @Override
    public void generateWorkBill(Order order) {
        WorkBill workBill = new WorkBill();
        workBill.setType("新");
        workBill.setPickstate("新单");
        workBill.setBuildtime(new Date());
        workBill.setRemark(order.getRemark());
        final String smsNumber = RandomStringUtils.randomNumeric(4);
        workBill.setSmsNumber(smsNumber);
        workBill.setOrder(order);
        workBill.setCourier(order.getCourier());
        workBillRepository.save(workBill);
        jmsQueueTemplate.send(new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                MapMessage mapMessage = session.createMapMessage();
                mapMessage.setString("telephone", order.getCourier().getTelephone());
                mapMessage.setString("msg", "短信序号：" + workBill.getSmsNumber() + ",取件地址：" + order.getSendAddress()
                        + ",联系人：" + order.getSendName() + ",手机：" + order.getSendMobile() + ",快递员捎话：" + order.getSendMobileMsg());

                return mapMessage;
            }
        });
        workBill.setPickstate("已通知");
    }
}
