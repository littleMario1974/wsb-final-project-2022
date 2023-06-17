package com.example.wsbfinalproject2022.mail;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Mail {

    String recipient;

    String subject;

    String content;
}
