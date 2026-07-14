package com.sven.common.dubbo.server;

import java.math.BigDecimal;

public interface IPaymentService {
    
    public boolean payment(String payCode, BigDecimal price);

    public boolean isSupport(String payCode);
}