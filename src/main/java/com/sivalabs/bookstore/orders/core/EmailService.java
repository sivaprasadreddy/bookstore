package com.sivalabs.bookstore.orders.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
class EmailService {
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    public void sendEmail(String to, String subject, String content) {
        String mail =
                """
                =============================
                To: %s
                Subject: %s
                %s
                =============================
                """
                        .formatted(to, subject, content);
        log.info(mail);
    }
}
