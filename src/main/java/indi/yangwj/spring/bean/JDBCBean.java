package indi.yangwj.spring.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Data
public class JDBCBean {
    private String url;
    private String username;
    private String password;
}