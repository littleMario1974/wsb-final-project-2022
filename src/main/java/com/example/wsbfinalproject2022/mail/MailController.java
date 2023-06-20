package com.example.wsbfinalproject2022.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mails")
@RequiredArgsConstructor
public class MailController {

    final private MailService mailService;

    @GetMapping
    String showForm() {
        return "mailindex";

    }
    @PostMapping
    String sendMail(@ModelAttribute Mail mail) {
    mailService.sendMail(mail);
        return "mailindex";
    }
}
