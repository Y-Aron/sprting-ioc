package indi.yangwj.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
// 配置扫包路径，否则获取bean时会报org.springframework.beans.factory.NoSuchBeanDefinitionException异常
@ComponentScan("indi.yangwj.spring")
public class AppConfig {
}
