package cn.itcast.bos.utils;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

public class MailUtils {
    private static String smtp_host = "smtp.live.com";
    private static String username = "gc22331@hotmail.com";
    private static String password = "gsmmb7m5gc";
    private static String from = "gc22331@hotmail.com";
    public static String activeUrl = "http://localhost:9003/bos_fore/customer_active.action";

    public static void sendMail(String subject, String content, String to) {
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", "smtp.live.com");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.port", "587");
        Session session = Session.getInstance(props);
        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(from));
            message.setRecipient(RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setContent(content, "text/html;charset=utf-8");
            Transport transport = session.getTransport();
            transport.connect(smtp_host, 25, username, password);
            transport.sendMessage(message, message.getAllRecipients());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("邮件发送失败...");
        }
    }

    //  测试
    public static void main(String[] args) {

        sendMail("测试邮件", "你好，传智播客", "gc22331@hotmail.com");
    }
}
