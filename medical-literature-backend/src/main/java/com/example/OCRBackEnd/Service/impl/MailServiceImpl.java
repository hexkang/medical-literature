package com.example.OCRBackEnd.Service.impl;

import com.example.OCRBackEnd.POJO.MailRequest;
import com.example.OCRBackEnd.Service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.Date;


@Component
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender javaMailSender;
    // 用.properties就能没错？不行

    @Value("${spring.mail.username}")
    private String MailSender;

    private static final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

    public void CheckMail(MailRequest mailRequest){
        Assert.notNull(mailRequest, "邮件请求不能为空");
        Assert.notNull(mailRequest.getSendTo(), "邮件收件人不能为空");
        Assert.notNull(mailRequest.getSubject(), "邮件主题不能为空");
        Assert.notNull(mailRequest.getText(), "邮件收件人不能为空");
    }

    @Override
    public void sendSimpleMail(MailRequest mailRequest) {
        SimpleMailMessage message = new SimpleMailMessage();
        CheckMail(mailRequest);
        message.setFrom(MailSender);
        message.setTo(mailRequest.getSendTo().split(","));
        message.setSubject(mailRequest.getSubject());
        message.setText(mailRequest.getText());
        message.setSentDate(new Date());
        javaMailSender.send(message);
        System.out.println("发送邮件成功:" + MailSender + "->" + mailRequest.getSendTo());
    }

    @Override
    public void sendHtmlMail(MailRequest mailRequest) {
        MimeMessage message = javaMailSender.createMimeMessage();
        CheckMail(mailRequest);
        try{
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(MailSender);
            helper.setTo(mailRequest.getSendTo().split(","));
            helper.setSubject(mailRequest.getSubject());
            helper.setText(mailRequest.getText(), true);
            helper.setSentDate(new Date());
            String filePath = mailRequest.getFilePath();
            if(StringUtils.hasText(filePath)){
                FileSystemResource file = new FileSystemResource(new File(filePath));
                String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
                helper.addAttachment(fileName, file);
            }
            javaMailSender.send(message);
            System.out.println("发送邮件成功" + MailSender + "->" + mailRequest.getSendTo());

        } catch (MessagingException e) {
            System.out.println("发送邮件异常");
            e.printStackTrace();
        }
    }
}
