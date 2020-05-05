package indi.yangwj.spring.bean;

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
    private List<Integer> roleIds;

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