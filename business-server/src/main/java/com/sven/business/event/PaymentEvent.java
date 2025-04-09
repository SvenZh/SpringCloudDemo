package com.sven.business.event;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentEvent extends ApplicationEvent {
    
    public PaymentEvent(String message) {
        super(message);
        this.message = message;
    }

    private static final long serialVersionUID = 1L;
    
    private String message;

}
