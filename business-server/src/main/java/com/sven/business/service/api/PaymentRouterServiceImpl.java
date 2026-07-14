package com.sven.business.service.api;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sven.business.service.PayMentStrategyContext;
import com.sven.common.dubbo.server.IPaymentService;

@Service("paymentRouterService")
public class PaymentRouterServiceImpl implements IPaymentService {

    @Autowired
    private PayMentStrategyContext payMentStrategyContext;

    @Override
    public boolean payment(String payCode, BigDecimal price) {
        return payMentStrategyContext.toPay(payCode, price);
    }

    @Override
    public boolean isSupport(String payCode) {
        return true;
    }

}
