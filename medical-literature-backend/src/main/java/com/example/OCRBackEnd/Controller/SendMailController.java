package com.example.OCRBackEnd.Controller;

import com.example.OCRBackEnd.POJO.MailRequest;
import com.example.OCRBackEnd.Service.impl.MailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sendmail")
public class SendMailController {
    @Autowired
    private MailServiceImpl sendMailService;

    @PostMapping("/simple")
    public void SendSimpleMessage(@RequestBody MailRequest mailRequest){
        sendMailService.sendSimpleMail(mailRequest);
    }

    @PostMapping("/html")
    public void SendHtmlMessage(@RequestBody MailRequest mailRequest){
        sendMailService.sendHtmlMail(mailRequest);
    }
}
