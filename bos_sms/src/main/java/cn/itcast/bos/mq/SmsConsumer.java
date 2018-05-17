package cn.itcast.bos.mq;

import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

@Service("smsConsumer")
public class SmsConsumer implements MessageListener {

    @Override
    public void onMessage(Message message) {
        MapMessage mapMessage = (MapMessage) message;
        String result = "000/xxxxx";
        try {
            if (result.startsWith("000")) {

                System.out.println("发送短信成功，手机号：" + mapMessage.getString("telephone") + ",验证码为：" + mapMessage.getString("msg"));

            } else {
                throw new RuntimeException("短信发送失败，信息码" + result);
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
