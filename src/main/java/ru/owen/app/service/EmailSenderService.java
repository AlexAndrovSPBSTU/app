//package ru.owen.app.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSenderImpl;
//import org.springframework.stereotype.Service;
//
//import java.util.Properties;
//
//@Service
//public class EmailSenderService {
//    private final JavaMailSenderImpl mailSender;
//
//    @Autowired
//    public EmailSenderService(JavaMailSenderImpl mailSender, @Value("${emailName}") String username,
//                              @Value("${emailPassword}") String password) {
//        this.mailSender = mailSender;
//        mailSender.setHost("smtp.gmail.com");
//        mailSender.setPort(587);
//        mailSender.setUsername(username);
//        mailSender.setPassword(password);
//        Properties props = mailSender.getJavaMailProperties();
//        props.put("mail.transport.protocol", "smtp");
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.debug", "true");
//    }
//
//    private void sendEmail(String toEmail, String subject, String body) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(toEmail);
//        message.setText(body);
//        message.setSubject(subject);
//        mailSender.send(message);
//    }
//
//    public void sendEmail(String email, String hash) {
//        sendEmail(email, "Подтвердите регистрацию", "http://81.29.132.234:8080/verify?confirm=" + hash);
//    }
//}
