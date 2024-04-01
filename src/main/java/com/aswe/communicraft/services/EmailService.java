package com.aswe.communicraft.services;

public interface EmailService {
    void sendEmail(String toEmail,
                         String subject,
                         String body);
}
