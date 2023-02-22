package com.codestates.Header2Footer.helper.email;

import org.springframework.stereotype.Component;

@Component
public interface EmailSendable {
    void send(String message) throws InterruptedException;
}
