package com.osk2090.blog.user.service;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
public class DummyMailSender implements MailSender {
    public void send(SimpleMailMessage mailMessage) throws MailException {
    }

    public void send(SimpleMailMessage[] mailMessage) throws MailException {
    }
}
