package com.example.OCRBackEnd.Service;

import com.example.OCRBackEnd.POJO.MailRequest;
import org.springframework.stereotype.Service;

@Service
public interface MailService {
    void sendSimpleMail(MailRequest mailRequest);
    void sendHtmlMail(MailRequest mailRequest);
}
