## 生命周期的概要流程
- 实例化（Instantiation）
- 属性赋值（Populate）
- 初始化（Initialization）
- 销毁（Destruction）

## BeanFactoryPostProcessor接口扩展点
### (Bean 实例化前)
- BeanDefinitionRegistryPostProcessor：修改 BeanDefinition

## Aware接口扩展点(实例化后)
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
AbstractAdvisingBeanPostProcessor：AOP 代理创建
AsyncAnnotationBeanPostProcessor：@Async 代理
ScheduledAnnotationBeanPostProcessor：@Scheduled 处理
ApplicationListenerDetector：监听器检测
ImportAwareBeanPostProcessor：处理 ImportAware
其他自定义 BeanPostProcessor：用户自定义扩展
### postProcessProperties(属性赋值前拦截)
- InstantiationAwareBeanPostProcessor：修改特定bean的属性值

## 其它扩展类
### EnvironmentPostProcessor(应用启动最早期，容器刷新前，全局配置影响所有Bean)：添加/修改 PropertySource、配置中心集成、解密、Profile激活
