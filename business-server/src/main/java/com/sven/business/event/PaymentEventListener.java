package com.sven.business.event;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PaymentEventListener {

    @Async
    @EventListener(value = PaymentEvent.class)
    public void handler(PaymentEvent event) {
        log.info(event.getMessage());
    }
}
