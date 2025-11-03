package com.k1ts.email;

public interface EmailService {

    void sendEmail(String from, String subject, String to, String content);
}
