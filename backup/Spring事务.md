## @Transactional实现
- @EnableTransactionManagement：由TransactionAutoConfiguration自动配置 CGLIB 代理（默认）
  - TransactionManagementConfigurationSelector.selectImports()：导入配置
    - AutoProxyRegistrar：注册自动代理创建器
      - InfrastructureAdvisorAutoProxyCreator：创建CGLIB代理
    - ProxyTransactionManagementConfiguration：事务配置类
      - BeanFactoryTransactionAttributeSourceAdvisor：创建事务增强器 Advisor
        - Pointcut: 匹配有 @Transactional 的方法
        - Advice: TransactionInterceptor
      - AnnotationTransactionAttributeSource：创建事务属性解析器
      - TransactionInterceptor：创建事务拦截器

``` java
// 1. REQUIRED（默认）：当前有事务则加入，否则新建
@Transactional(propagation = Propagation.REQUIRED)

// 2. REQUIRES_NEW：挂起当前事务，总是新建独立事务
@Transactional(propagation = Propagation.REQUIRES_NEW)

// 3. NESTED：如果外层事务回滚，嵌套事务也会回滚，但嵌套事务可以独立回滚，不影响外层事务
@Transactional(propagation = Propagation.NESTED)

// 4. SUPPORTS：有事务则加入，没有则非事务执行
@Transactional(propagation = Propagation.SUPPORTS)

// 5. NOT_SUPPORTED：总是非事务执行，如果有事务则挂起
@Transactional(propagation = Propagation.NOT_SUPPORTED)

// 6. NEVER：以非事务方式执行，如果当前有事务则抛出异常
@Transactional(propagation = Propagation.NEVER)

// 7. MANDATORY：必须在事务中执行，否则抛出异常
@Transactional(propagation = Propagation.MANDATORY)
```