package indi.yangwj.spring.config;

import indi.yangwj.spring.bean.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfig {

    @Bean
    public User createUser() {
        return new User();
    }
}
