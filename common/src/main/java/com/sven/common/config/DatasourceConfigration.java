package com.sven.common.config;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;

@Configuration
public class DatasourceConfigration {
    @Bean("transactionAdvice")
    public TransactionInterceptor TransactionAdvice(@Qualifier("transactionManager") TransactionManager transactionManager) {
        DefaultTransactionAttribute required = new DefaultTransactionAttribute();
        required.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        DefaultTransactionAttribute requiredNew = new DefaultTransactionAttribute();
        requiredNew.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        DefaultTransactionAttribute supports = new DefaultTransactionAttribute();
        supports.setPropagationBehavior(TransactionDefinition.PROPAGATION_SUPPORTS);
        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
        source.addTransactionalMethod("insert*", required);
        source.addTransactionalMethod("update*", required);
        source.addTransactionalMethod("delete*", required);
        source.addTransactionalMethod("query*", supports);
        source.addTransactionalMethod("retrieve*", supports);
        supports.setReadOnly(false);

        return new TransactionInterceptor(transactionManager, source);
    }

    @Bean
    public Advisor txAdviceAdvisor(@Qualifier("transactionAdvice") TransactionInterceptor transactionInterceptor) {
        AspectJExpressionPointcutAdvisor pointcutAdvisor = new AspectJExpressionPointcutAdvisor();
        pointcutAdvisor.setExpression("execution(* com.sven..*Service*.*(..))");
        pointcutAdvisor.setAdvice(transactionInterceptor);
        pointcutAdvisor.setOrder(1);
        return pointcutAdvisor;
    }

}
