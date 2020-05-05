# Spring-IOC

> Spring 是一个轻量级Java开发框架，而学习Spring框架的小伙伴们都知道Spring框架的核心是IOC容器和AOP模块，其中IOC容器是Spring框架的底层基础，负责管理对象的创建和对象依赖关系的维护。本文是一篇入门级别文章，主要介绍Spring-IOC的相关使用。

## 1. 基础概念

### 1.1 IOC是什么？

控制反转即IOC(Inversion of Control), 可以理解为通过容器来实现对象组件的装配和管理, 然后通过容器去获取相关对象进行操作。

### 1.2 IOC有什么作用？

- 管理对象的创建和依赖关系的维护。
- 解耦，由容器去维护具体的对象。
- 托管了类的产生过程，比如动态代理。

### 1.3 IOC是怎么分类的

控制反转IOC其主要的实现方式由两种：依赖注入和依赖查找

### 1.4 什么是依赖注入(DI)

所谓的依赖注入（Dependency Injection）即组件之间的依赖关系由容器动态地将某种依赖关系的目标对象实例注入到容器的各个关联的组件之中。

Spring提供以下几种注入方式：

- 接口注入（Interface Injection）
- Setter 方法注入 （Setter Injection）
- 构造器注入（Constructor Injection）

其中接口注入由于在灵活性和易用性比较差，从Spring4开始已被废弃。

### 1.5 依赖查找

依赖查找（Dependency Lookup）：容器提供回调接口和上下文环境给组件。EJB和Apache Avalon都使用这种方式。

## 2. Spring-IOC实战

> 说了这么多，那么Spring-IOC到底要怎么使用呢？本文作为一个入门级文章，主要关注实战方面。
>
> 项目地址：https://github.com/Y-Aron/sprting-ioc

### 2.1 创建maven项目

> Pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <properties>
        <!-- Environment Settings -->
        <java.version>1.8</java.version>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <spring.version>5.2.6.RELEASE</spring.version>
    </properties>

    <groupId>indi.yangwj.spring</groupId>
    <artifactId>sprting-ioc</artifactId>
    <version>1.0.0</version>
    
    <dependencies>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>${spring.version}</version>
        </dependency>
    </dependencies>
</project>
```

### 2.2 创建Java对象

创建 `indi.yangwj.spring.bean.User`

```java
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

//@Component
@Slf4j
@NoArgsConstructor
@Setter
@Getter
@ToString
public class User {
    private String username;
    private String password;
    private Role role;
    private List<String> interests;
    private Properties properties;
    private String[] array;
    private Map<String, String> map;
    private Set<String> set;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
```

### 2.3 装配Bean

#### 2.3.1 在xml中装配bean

> 创建 resources/spring-beans.xml 文件，在xml中装配bean主要有几种方式：
>
> 1. 无参构造函数：`spring-beans.xml`
> 2. 静态工厂装配bean：`spring-beans-static-factory.xml`
> 3. 实例工厂装配bean：`spring-beans-factory.xml`
> 4. 更多装配方式：[属性注入](#2.5)
>
> 注意在一个应用程序中，bean的类名一定是唯一的，即IOC容器中不允许出现重复的类！所以以上三种方式一次只能配置一种。

- 无参构造函数：Bean类中必须提供无参数构造

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
   
    <!-- 无参构造函数创建对象 -->
    <!-- bean参数介绍:
        1. scope:
            prototype: 多例对象, 对象在IOC容器之前就已经创建
            singleton: 单例对象(默认值)，对象在使用时才创建
        2. lazy-init:
            true: 对象在使用时创建，只对单例对象有效(scope=singleton)
            false: 对象在IOC容器之前就已经创建 默认值
        3. init-method: 创建对象时初始化方法
        4. destroy-method: 执行ClassPathXmlApplicationContext.close()时执行的方法。注意当scope=prototype时，是不会执行destroy-method
    -->
    <bean id="user" class="indi.yangwj.spring.bean.User" />
</beans>
```

- 静态工厂：创建一个工厂类,在工厂类中提供一个static返回bean对象的方法

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <!-- 工厂的静态方法创建对象，class: 工厂类，factory-method: 静态方法 -->
    <bean id="user1" class="indi.yangwj.spring.factory.UserFactory" factory-method="createStaticUser" />

</beans>
```

- 实例工厂：创建一个工厂类,在工厂类中提供一个非static的创建bean对象的方法,在配置文件中需要将工厂配置,还需要配置bean

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

	<!-- 工厂的非静态方法创建对象 -->
    <!-- 创建工厂类的bean -->
    <bean id="userFactory" class="indi.yangwj.spring.factory.UserFactory"/>
    
    <!-- factory-bean：工厂类 factory-method: 非静态方法 -->
    <bean id="user2" factory-bean="userFactory" factory-method="createUser"/>

</beans>
```

工厂类定义：`UserFactory.java`

```java
import indi.yangwj.spring.bean.User;

public class UserFactory {

    public static User createStaticUser() {
        return new User();
    }

    public User createUser() {
        return new User();
    }
}
```

#### 2.3.2 Java代码装配bean

通过以下几种注解可以定义一个bean类：@Component；@Service；@Controller；@Configuration+@Bean

注意使用注解的方式需要预先定义扫包路径！

- @Component；@Service；@Controller：在类定义相关注解即可
- @Configuration+@Bean

```java
import indi.yangwj.spring.bean.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// @Configuration注解表明这个类是一个配置类，该类应该包含在Spring应用上下文中如何创建bean的细节。
@Configuration
public class UserConfig {

    // @Bean注解会告诉Spring这个方法将会返回一个对象
    // 该对象要注册为Spring应用上下文中的bean。方法体中包含了最终产生bean实例的逻辑。
    // 该方法返回的对象类不允许被 @Component；@Service；@Controller这些注解修饰，否则会报错！
    @Bean
    public User createUser() {
        return new User();
    }
}
```

### 2.4 创建IOC容器

> Spring IOC容器的创建有以下几种方式：
>
> 1. XmlBeanFactory读取xml文件（已过时）
> 2. ClassPathXmlApplicationContext读取xml文件
> 3. AnnotationConfigApplicationContext注解方式

#### 2.4.1 读取xml创建IOC容器

- XmlBeanFactory创建（已过时）

```java
@Test
public void testBeanFactory() {
    Resource resource = new ClassPathResource("spring-beans.xml");
    BeanFactory factory = new XmlBeanFactory(resource);
    User user = factory.getBean(User.class);
    log.info("user: {}", user);
}
```

- ClassPathXmlApplicationContext读取xml文件创建

```java
@Test
public void testClassPathXml() {
    ApplicationContext context = new ClassPathXmlApplicationContext("spring-beans.xml");
    User user = context.getBean(User.class);
    log.info("{}", user);
}
```

#### 2.4.2 Java代码创建IOC容器

- 创建`AppConfig.java` 

```java
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
// 配置扫包路径，否则获取bean时会报org.springframework.beans.factory.NoSuchBeanDefinitionException异常
@ComponentScan("indi.yangwj.spring")
public class AppConfig {
}
```

- 通过AnnotationConfigApplicationContext创建IOC容器

```java
@Test
public void testAnnotationConfig() {
    ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
    User user = context.getBean(User.class);
    log.info("{}", user);
}
```

### 2.5 <span id="2.5">属性注入</span>

#### 2.5.1 构造器注入

> 创建 `spring-beans-constructor.xml`
>
> 必须保证bean类存在有参构造方法，并且参数要与配置完全一致，否则解析异常！！
>
> 比如存在构造方法：`public User(String username, String password) `
>
> 那么`<constructor-arg />` 必须要有两个，一个是username，一个是password！否则解析会报错！

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

	<!-- 有参数构造方法注入 -->
    <bean id="role" class="indi.yangwj.spring.bean.Role" />
    <bean id="user4" class="indi.yangwj.spring.bean.User" >
        <!-- index: 参数在构造方法上的位置 -->
        <!-- name: 参数名称 -->
        <!-- type: 参数类型 -->
        <!-- value: 参数值 -->
        <constructor-arg index="0" name="username" 
                         type="java.lang.String" value="user" />
        <constructor-arg index="1" name="password"
                         type="java.lang.String" value="passwd" />
        
        <!-- 当构造函数的值是一个对象，而不是一个普通类型的值时，使用ref属性关联bean对象 -->
        <constructor-arg index="2" name="role" 
                         type="indi.yangwj.spring.bean.Role" ref="role" />
    </bean>
</beans>
```

#### 2.5.2 Setter注入

> 创建 `spring-beans-setter.xml`
>
> 相关属性必须存在set方法，否则解析异常！

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="role1" class="indi.yangwj.spring.bean.Role" />
    <bean id="user5" class="indi.yangwj.spring.bean.User" >
        <!--普通值类型-->
        <property name="password" value="密码"/>
        <!--特殊字符-->
        <property name="username">
            <value><![CDATA[测试]]></value>
        </property>
        <!--引用类型-->
        <property name="role" ref="role1" />
    </bean>
</beans>
```

#### 2.5.3 集合属性注入

> 创建 `spring-beans-property.xml`
>
> 集合属性注入包括数组；List；Set；Map；Properties 等集合对象的注入，一般情况下很少使用…

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:c="http://www.springframework.org/schema/c"
       xmlns:p="http://www.springframework.org/schema/p"
       xmlns:util="http://www.springframework.org/schema/util"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/util http://www.springframework.org/schema/util/spring-util.xsd">

    <bean id="user7" class="indi.yangwj.spring.bean.User" >

        <!-- 使用<array></array>或<list></list>标签注入数组 -->
        <property name="array">
            <list>
                <value>1</value>
                <value>2</value>
                <value>3</value>
            </list>
        </property>

        <!--使用<list></list>注入 `java.util.List`-->
        <property name="interests" >
            <list>
                <value>list aaa</value>
                <value>list bbb</value>
                <value>list ccc</value>
            </list>
        </property>
        <!--使用<set></set>注入 `java.util.Set`-->
        <property name="set" >
            <set>
                <value>set 123</value>
                <value>set 456</value>
                <value>set 789</value>
            </set>
        </property>

        <!-- 使用<map></map>注入 `java.util.Map`-->
        <property name="map">
            <map>
                <entry key="key1" value="value1" />
                <entry key="key2" value="value2" />
                <entry key="key3" value="value3" />
            </map>
        </property>
        <!--使用<props></props>注入 `java.util.Properties`-->
        <property name="properties">
            <props>
                <prop key="propKet1">propValue1</prop>
                <prop key="propKet2">propValue2</prop>
                <prop key="propKet3">propValue3</prop>
            </props>
        </property>
        <!-- 通过util命名空间配置集合类型的bean -->
        <property name="roleIds" ref="roleIds" />
    </bean>

    <!--
       util命名空间定义
       1. 导入util命名空间: <beans xmlns:util="http://www.springframework.org/schema/util"
               xsi:schemaLocation="http://www.springframework.org/schema/util http://www.springframework.org/schema/util/spring-util.xsd"></beans>
       2. Array/List/Set/Map/Properties等,用法与上述相同
    -->
    <util:list id="roleIds" value-type="java.lang.Integer">
        <value>1</value>
        <value>2</value>
    </util:list>

</beans>
```

#### 2.5.4 c和p注入

> 创建 `spring-beans-cp.xml`
>
> c-命名空间和p-命名空间是Spring3.引入，需要在xml顶部声明其模式。
>
> 1. c-命名空间实现构造器注入：简化 `<constructor-arg />`, 但是规则与其一致！
>
> 2. p-命名空间实现属性注入

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:c="http://www.springframework.org/schema/c"
       xmlns:p="http://www.springframework.org/schema/p"
       xmlns:util="http://www.springframework.org/schema/util"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/util http://www.springframework.org/schema/util/spring-util.xsd">

    <!--
        使用c名称空间进行参数赋值
        1. 导入c命名空间：<beans xmlns:c="http://www.springframework.org/schema/c"></beans>
        2. 使用c命名空间配置构造函数的参数
        3. c命名空间无法装配集合
     -->
    <bean id="role2" class="indi.yangwj.spring.bean.Role" />

    <bean id="user6" class="indi.yangwj.spring.bean.User"
          c:password="passwd"
          c:role-ref="role2"
          c:username="admin"/>
    
    <!--
    p命名空间注入参数,
    1. 导入p命名空间: <beans xmlns:p="http://www.springframework.org/schema/p"></beans>
    2. 使用p:属性完整注入
        |-值类型: p:属性名="值"
        |-对象类型: p:属性名-ref="bean名称" -->
<bean id="sysadmin" class="org.aron.springTest.bean.Sysadmin"
      p:nickname="昵称"
      p:role-ref="role" />
</beans>
```

#### 2.5.5 使用外部配置文件

- 创建 `application.properties`

```properties
jdbc.url=127.0.0.1
jdbc.username=root
jdbc.password=123456
```

- 创建 `spring-application.xml`，通过加载外部配置文件装配 bean

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd">

    <!--
        1. 导入context命名空间: <beans xmlns:context="http://www.springframework.org/schema/context"
               xsi:schemaLocation="http://www.springframework.org/schema/context
               http://www.springframework.org/schema/context/spring-context.xsd">
        2. 加载外部属性文件
        3. 使用 ${} 获取属性
    -->
    <context:property-placeholder location="classpath:/application.properties" />

    <bean id="jdbcBean" class="indi.yangwj.spring.bean.JDBCBean">
        <constructor-arg name="username" value="${jdbc.username}"/>
        <constructor-arg name="url" value="${jdbc.url}" />
        <constructor-arg name="password" value="${jdbc.password}" />
    </bean>
</beans>
```

## 3. <span id="3">SPEL的使用</span>

> **Spring 表达式语言(简称SpEL)**：是一个支持运行时查询和操作对象图的强大的表达式语言。
>
> **语法类似于 EL**：SpEL 使用 #{...} 作为定界符 , 所有在大括号中的字符都将被认为是 SpEL , SpEL 为 bean 的属性进行动态赋值提供了便利。

### 3.1 定义字面量

- 整数: `<property name="count" value="#{5}" />`
- 小数: `<property name="frequency" value="#{89.7}" />`
- 科学计算法: `<property name="capacity" value="#{1e4}" />`
- string可以使用单引号或双引号作为字符串的定界符号

```xml
<property name="name" value="#{'Chuck'}" />

<property name="name" value='#{"Chuck"}' />
```

- Boolean: `<property name="enabled" value="#{false}" />`
- Array/List/Set: `<property name="name" value="#{{'set1', 'set2', 'set3'}}" />`
- Map/Properties: `<property name="map" value="#{{'k1': 'v1', k2: 'v2'}}">`

### 3.2 引用Bean/属性/方法

- 引用其他对象

```xml
<!-- 通过value属性和spel配置bean之间的应用关系 -->
<property name="prefix" value="#{prefixGenerator}" />
```

- 引用其他对象的属性

```xml
<!-- 通过value属性和spel配置suffix属性值为另一个bean的suffix属性值 -->
<property name="suffix" value="#{prefixGenerator.suffix}" />
```

- 调用其他方法,还可以进行链式调用

```xml
<!-- 通过value属性值和spel配置suffix属性值为另一个bean的方法的返回值 -->
<property name="suffix" value="#{prefixGenerator.toString()}" />

<!-- 方法连调 -->
<property name="suffix" value="#{prefixGenerator.toString().toUpperCase()}" />
```

### 3.3 支持的运算符号

- 算数运算符: +, -, *, /, %, ^

```xml
<property name="adjustedAmount" value="#{counter.total + 42}" />
<property name="adjustedAmount" value="#{counter.total - 20}" />
<property name="circumference" value="#{2 * T(java.lang.Math).PI * circle.radius}" />
<property name="average" value="#{counter.total / counter.count}" />
<property name="remainder" value="#{counter.total % counter.count}" />
<property name="area" value="#{T(java.lang.Math).PI * circle.radius ^ 2}" />
```

- 加号还可以用作字符串连接

```xml
<constructor-arg value="#{performer.firstName + ' ' + performer.lastName}" />
```

- 比较运算符: <, >, ==, <=, >=, lt, gt, eq, le, ge

```xml
<property name="equal" value="#{counter.total == 100}" />
<property name="hasCapacity" value="#{counter.total le 1000}" />
```

- 逻辑运算符号: and, or, not, !

```xml
<property name="large" value="#{shape.kind == shape.perimeter}" />
<property name="outOfStock" value="#{!product.available}" />
<property name="outOfStock" value="#{not product.available}" />
```

- if-else运算符: ?:(elvis)

```xml
<constructor-arg value="#{songSelect? 'jelly': 'jack'}" />
```

- if-else的变体

```xml
<constructor-arg value="#{songSelect?: 'jack'}" />
```

- 正则表达式:matches

```xml
<constructor-arg value="#{admin.email matches '[a-zA]'}" />
```

- 调用静态方法和静态属性：通过T()调用一个类的静态方法,它将返回一个Class Object,然后再调用相应的方法或属性

```xml
<property name="initValue" value="#{T(java.lang.Math).PT}" />
```
