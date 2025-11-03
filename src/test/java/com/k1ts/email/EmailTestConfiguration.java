package com.k1ts.email;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.util.HashSet;
import java.util.Set;

@TestConfiguration
public class EmailTestConfiguration {
    private Set<Email> emails = new HashSet<>();

    @Primary
    @Bean
    public EmailService emailService()  {
        return (from, subject, to, content) -> {
            emails.add(Email
                    .builder()
                    .from(from)
                    .to(to)
                    .subject(subject)
                    .content(content)
                    .build());
        };
    }

    public Set<Email> getEmails() {
        return emails;
    }
}
