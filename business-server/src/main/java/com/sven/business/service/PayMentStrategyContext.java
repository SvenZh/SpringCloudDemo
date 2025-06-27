package com.sven.business.service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import com.sven.common.annotation.PayStrategyAnnotation;

@Component
public class PayMentStrategyContext {

    private Map<String, IPaymentService> payMentStrategyMap;
    
    @Autowired
    private List<IPaymentService> paymentServiceList;
    
    @Autowired
    public PayMentStrategyContext(List<IPaymentService> strategies) {
        this.payMentStrategyMap = strategies.stream()
                .collect(Collectors.toMap(
                        strategy -> strategy.getClass().getAnnotation(PayStrategyAnnotation.class).value(),
                        Function.identity()));
    }

    public Boolean toPay(String payCode, BigDecimal price) {
        IPaymentService strategy = payMentStrategyMap.get(payCode);
        if (strategy == null) {
            throw new IllegalArgumentException("不支持的支付方式: " + payCode);
        }
        return strategy.payment(price);
    }

//    public Boolean toPay(String payCode, BigDecimal price) {
//
//        Optional<IPaymentService> paymentService = paymentServiceList.stream()
//                .filter(payService -> payService.isSupport(payCode))
//                .max(Comparator.comparingInt(Ordered::getOrder));
//
//        IPaymentService strategy = paymentService.get();
//        if (strategy == null) {
//            throw new IllegalArgumentException("不支持的支付方式: " + payCode);
//        }
//        return strategy.payment(price);
//    }
    
}
