package com.codestates.Header2Footer.helper.email;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailConfiguration {
    @Bean
    public EmailSendable emailSendable() {
        return new MockEmailSendable();
    }
}
