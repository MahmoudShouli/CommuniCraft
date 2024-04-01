package com.aswe.communicraft.services.impl;

import com.aswe.communicraft.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
/*
 * The EmailService class provides email-related services.
 */
public class EmailServiceImpl implements EmailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);
    private final JavaMailSender mailSender;
    /**
     * Sends an email message.
     * This method sends an email message with the specified subject and body
     * It uses the configured mail sender to send the email. The email is sent from a fixed sender email address.
     *
     * @param toEmail the recipient email address
     * @param subject the subject of the email
     * @param body the body content of the email
     */
    @Override
    public void sendEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("communicraftt@gmail.com");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);
        mailSender.send(message);
        LOGGER.info("sending email..");
    }
}
