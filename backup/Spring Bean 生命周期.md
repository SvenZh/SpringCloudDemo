## Spring bean的生命周期
``` text
把普通java类变为Spring管理的Bean的过程，即Spring的核心之一控制反转，即把原来对象创建的控制权由用户程序交由Spring管理: 先解析java类为BeanDefinition之后进行实例化、属性赋值、初始化、最后销毁。
```

## 容器启动阶段
- ApplicationContextInitializer.initialize：环境配置、Profile 激活、PropertySource 添加
- EnvironmentPostProcessor.postProcessEnvironment：添加/修改 PropertySource、配置中心集成、解密、Profile激活

## BeanDefinition 加载与注册阶段
- BeanDefinitionReader
  - XmlBeanDefinitionReader：Spring 解析 XML 生成 BeanDefinition
  - AnnotatedBeanDefinitionReader：Spring 解析注解 @Component, @Service 生成 BeanDefinition
  - ConfigurationClassBeanDefinitionReader：Spring 解析 @Bean 方法生成 BeanDefinition
  - ClassPathBeanDefinitionScanner：包扫描注解类 `@ComponentScan` 生成 BeanDefinition
  - PropertiesBeanDefinitionReader：解析Properties文件 `userService.class=com.example.UserService` 生成 BeanDefinition
  - 自定义 BeanDefinitionReader
- MergedBeanDefinitionPostProcessor.postProcessMergedBeanDefinition
  - ApplicationListenerDetector：记录是否单例bean

## BeanDefinition 处理阶段  
- BeanFactoryPostProcessor：读取BeanDefiniion数据，并且可以根据需要进行修改
- PropertySourcesPlaceholderConfigurer：解析配置文件中的 ${...} 占位符，并注册解析器，由PropertyPlaceholderAutoConfiguration自动配置
- BeanDefinitionRegistryPostProcessor.postProcessBeanDefinitionRegistry：读取BeanDefiniion数据，并且可以根据需要进行修改，手动注册Bean
  - ConfigurationClassPostProcessor：解析 @Configuration

## BeanDefinition 存储
- BeanDefinitionRegistry (接口)
  - DefaultListableBeanFactory (主要实现类)
    - beanDefinitionMap: ```ConcurrentHashMap<String, BeanDefinition>  // 核心存储```
    - beanDefinitionNames: ```List<String>  // 保持注册顺序```
    - mergedBeanDefinitions: ```Map<String, RootBeanDefinition>  // 合并后的定义```

## 生命周期的概要流程 
- 入口`SpringApplication.run -> refreshContext -> AbstractApplicationContext.refresh -> finishBeanFactoryInitialization -> AbstractAutowireCapableBeanFactory.doCreateBean`
  - 实例化（Instantiation）
    - 通过反射调用构造方法创建对象实例
    - 此时对象已创建，但属性都为默认值（null、0等）
    - 可以通过 InstantiationAwareBeanPostProcessor 干预
  - 属性赋值（Populate）
    - 依赖注入（@Autowired、@Resource、@Value等）
    - 可以通过 InstantiationAwareBeanPostProcessor.postProcessProperties 干预
    - 此时 Bean 的属性已填充完成
  - 初始化（Initialization）
    - 执行 Aware 接口回调
    - 执行 BeanPostProcessor.postProcessBeforeInitialization
    - 执行 @PostConstruct、InitializingBean、init-method
    - 执行 BeanPostProcessor.postProcessAfterInitialization
    - 此时 Bean 已完全准备就绪，可以使用
  - 销毁（Destruction）
    - 容器关闭时执行
    - 执行 @PreDestroy、DisposableBean、destroy-method
    - 释放资源、清理连接等

## 实例化
- InstantiationAwareBeanPostProcessor.postProcessBeforeInstantiation：返回代理对象

## 属性赋值
- InstantiationAwareBeanPostProcessor.postProcessAfterInstantiation：决定是否继续属性赋值
- InstantiationAwareBeanPostProcessor.postProcessProperties：修改特定bean的属性值
    - AutowiredAnnotationBeanPostProcessor：处理 @Autowired 和 @Value(此时占位符已解析)
    - CommonAnnotationBeanPostProcessor：处理 @Resource 注入

## 初始化
- `initializeBean.invokeAwareMethods`
  - BeanNameAware：注入当前bean对应beanName
  - BeanClassLoaderAware：注入加载当前bean的ClassLoader
  - BeanFactoryAware：注入当前BeanFactory容器的引用
- `initializeBean.applyBeanPostProcessorsBeforeInitialization(初始化前)`
  - BeanPostProcessor.postProcessBeforeInitialization
    - ApplicationContextAwareProcessor：处理容器相关的Aware接口
        - ApplicationContextAware：获取 ApplicationContext
        - EnvironmentAware：获取Environment
        - EmbeddedValueResolverAware：解析占位符
        - ImportAware：获取导入配置类的元数据
        - ResourceLoaderAware：获取资源加载器
        - ApplicationEventPublisherAware：获取事件发布器
        - MessageSourceAware：获取国际化消息源
        - ServletContextAware：获取ServletContext
        - ServletConfigAware：获取ServletConfig
        - HttpSessionAware：获取HttpSession
    - BeanValidationPostProcessor：Bean 验证
    - InitDestroyAnnotationBeanPostProcessor：处理 @PostConstruct
    - PersistenceExceptionTranslationPostProcessor：异常转换
    - 其他自定义 BeanPostProcessor：用户自定义扩展
- `initializeBean.invokeInitMethods`
  - InitializingBean：
  - init-method：自定义初始化方法（```@Bean(initMethod = "initMethod")```）
- `initializeBean.applyBeanPostProcessorsAfterInitialization`
  - BeanPostProcessor.postProcessAfterInitialization(初始化后)
    - AbstractAdvisingBeanPostProcessor：AOP 代理创建
    - AsyncAnnotationBeanPostProcessor：处理 @Async
    - ScheduledAnnotationBeanPostProcessor：处理@Scheduled 
    - ApplicationListenerDetector：扫描ApplicationListener监听器接口的Bean，并添加到ApplicationEventMulticaster(只有单例监听器会被自动注册)
    - ImportAwareBeanPostProcessor：处理 ImportAware
    - ConfigurationPropertiesBindingPostProcessor 处理 @ConfigurationProperties 属性绑定
    - 其他自定义 BeanPostProcessor：用户自定义扩展
    - SmartInitializingSingleton.afterSingletonsInstantiated：在所有单例Bean的postProcessAfterInitialization之后初始化后回调
      - EventListenerMethodProcessor：扫描 @EventListener 方法, 创建 ApplicationListener 适配器，并添加到ApplicationEventMulticaster

## 启动完成后
- 发布ApplicationStartedEvent事件
- ApplicationRunner：支持复杂参数解析，热点数据的预加载、清除临时文件、读取自定义配置
- CommandLineRunner：简单参数，不需要复杂解析，热点数据的预加载、清除临时文件、读取自定义配置
- 发布ApplicationReadyEvent事件

## 销毁
- DestructionAwareBeanPostProcessor.postProcessBeforeDestruction：销毁前回调做资源清理、连接关闭（作用范围为所有Bean，其它为单个Bean）
- InitDestroyAnnotationBeanPostProcessor.postProcessBeforeDestruction：处理@PreDestroy 
- DisposableBean.destroy()：实现 DisposableBean 接口
- destroy-method：自定义销毁方法```@Bean(destroyMethod = "cleanup")```
- 注：原型 Bean 不自动销毁，由容器管理生命周期

## 异常处理机制
- 实例化阶段异常：BeanInstantiationException
- 属性赋值阶段异常：BeanCreationException
- 初始化阶段异常：BeanInitializationException
- 销毁阶段异常：记录日志，不影响其他 Bean 销毁