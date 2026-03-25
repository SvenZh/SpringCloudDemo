## Spring bean的生命周期
``` text
把普通java类变为Spring管理的Bean的过程，即Spring的核心之一控制反转，即把原来对象创建的控制权由用户程序交由Spring管理: 先解析java类为BeanDefinition，然后保存起来（本质是一个ConcurrentHashMap）
之后进行实例化、属性赋值、初始化、最后销毁。
```

## 生命周期的概要流程
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

## 容器启动阶段
- ApplicationContextInitializer：环境配置、Profile 激活、PropertySource 添加
- EnvironmentPostProcessor：添加/修改 PropertySource、配置中心集成、解密、Profile激活

## BeanDefinition 加载与注册阶段
- BeanDefinitionReader
  - XmlBeanDefinitionReader：Spring 解析 XML 生成 BeanDefinition
  - AnnotatedBeanDefinitionReader：Spring 解析注解 @Component, @Service 生成 BeanDefinition
  - ConfigurationClassBeanDefinitionReader：Spring 解析 @Bean 方法生成 BeanDefinition
  - ClassPathBeanDefinitionScanner：包扫描注解类 `@ComponentScan` 生成 BeanDefinition
  - PropertiesBeanDefinitionReader：解析Properties文件 `userService.class=com.example.UserService` 生成 BeanDefinition
  - 自定义 BeanDefinitionReader

## BeanDefinition 处理阶段  
- BeanFactoryPostProcessor：读取BeanDefiniion数据，并且可以根据需要进行修改
- PropertySourcesPlaceholderConfigurer
- BeanDefinitionRegistryPostProcessor：读取BeanDefiniion数据，并且可以根据需要进行修改，手动注册Bean

## BeanDefinition 存储
- BeanDefinitionRegistry (接口)
  - DefaultListableBeanFactory (主要实现类)
    - beanDefinitionMap: ```ConcurrentHashMap<String, BeanDefinition>  // 核心存储```
    - beanDefinitionNames: ```List<String>  // 保持注册顺序```
    - mergedBeanDefinitions: ```Map<String, RootBeanDefinition>  // 合并后的定义```

## 实例化前
- InstantiationAwareBeanPostProcessor.postProcessBeforeInstantiation：返回代理对象

## Aware接口扩展点注入容器资源(实例化后)
- BeanNameAware：注入当前bean对应beanName
- BeanClassLoaderAware：注入加载当前bean的ClassLoader
- BeanFactoryAware：注入当前BeanFactory容器的引用
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

## 属性赋值
- InstantiationAwareBeanPostProcessor.postProcessAfterInstantiation：决定是否继续属性赋值
- InstantiationAwareBeanPostProcessor.postProcessProperties：修改特定bean的属性值

## BeanPostProcessor接口扩展点
### postProcessBeforeInitialization(初始化前)
- ApplicationContextAwareProcessor：处理容器相关的Aware接口
- BeanValidationPostProcessor：Bean 验证
- InitDestroyAnnotationBeanPostProcessor：处理 @PostConstruct
- AutowiredAnnotationBeanPostProcessor：处理 @Autowired 和 @Value
- CommonAnnotationBeanPostProcessor：处理 @Resource 等
- PersistenceExceptionTranslationPostProcessor：异常转换
- 其他自定义 BeanPostProcessor：用户自定义扩展

### invokeInitMethods(初始化)
- InitializingBean：
- init-method：自定义初始化方法（```@Bean(initMethod = "initMethod")```）

### postProcessAfterInitialization(初始化后)
- AbstractAdvisingBeanPostProcessor：AOP 代理创建
- AsyncAnnotationBeanPostProcessor：@Async 代理
- ScheduledAnnotationBeanPostProcessor：@Scheduled 处理
- ApplicationListenerDetector：监听器检测
- ImportAwareBeanPostProcessor：处理 ImportAware
- 其他自定义 BeanPostProcessor：用户自定义扩展
- SmartInitializingSingleton.afterSingletonsInstantiated：在所有单例Bean的postProcessAfterInitialization之后初始化后回调

## 启动完成后
- ApplicationRunner：支持复杂参数解析，热点数据的预加载、清除临时文件、读取自定义配置
- CommandLineRunner：简单参数，不需要复杂解析，热点数据的预加载、清除临时文件、读取自定义配置

## 销毁
- DestructionAwareBeanPostProcessor.postProcessBeforeDestruction：销毁前回调做资源清理、连接关闭（作用范围为所有Bean，其它为单个Bean）
- @PreDestroy：注解 
- DisposableBean.destroy()：实现 DisposableBean 接口
- destroy-method：自定义销毁方法```@Bean(destroyMethod = "cleanup")```
- 注：原型 Bean 不自动销毁，由容器管理生命周期

## 异常处理机制
- 实例化阶段异常：BeanInstantiationException
- 属性赋值阶段异常：BeanCreationException
- 初始化阶段异常：BeanInitializationException
- 销毁阶段异常：记录日志，不影响其他 Bean 销毁