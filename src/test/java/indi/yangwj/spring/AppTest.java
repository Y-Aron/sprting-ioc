package indi.yangwj.spring;

import indi.yangwj.spring.bean.Role;
import indi.yangwj.spring.bean.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@Slf4j
public class AppTest {

    @Test
    public void testBeanFactory() {
        Resource resource = new ClassPathResource("spring-beans.xml");
        BeanFactory factory = new XmlBeanFactory(resource);
        User user = factory.getBean(User.class);
        log.info("user: {}", user);
    }

    @Test
    public void testClassPathXml() {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-beans-cp.xml");
        User user = context.getBean(User.class);
        log.info("{}", user);


    }

    @Test
    public void testAnnotationConfig() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        User user = context.getBean(User.class);
        log.info("user: {}", user);

        BeanDefinition beanDefinition = new RootBeanDefinition(Role.class);

        context.registerBeanDefinition("role1222", beanDefinition);

        Role role = context.getBean(Role.class);

        log.info("role: {}", role);

    }
}
