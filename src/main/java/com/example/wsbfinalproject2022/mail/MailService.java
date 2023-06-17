package com.example.wsbfinalproject2022.mail;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {


    final private JavaMailSender javaMailSender;

    public void sendMail(Mail mail) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

            mimeMessageHelper.setTo(mail.recipient);
            mimeMessageHelper.setSubject(mail.subject);
            mimeMessageHelper.setText(mail.content);

            javaMailSender.send(mimeMessage);

        } catch (Exception e) {
            System.out.println("Wysyłanie mejla nie powiodło się");
        }
    }
}
