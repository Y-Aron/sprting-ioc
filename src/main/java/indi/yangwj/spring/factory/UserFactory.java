package indi.yangwj.spring.factory;

import indi.yangwj.spring.bean.User;

public class UserFactory {

    public static User createStaticUser() {
        return new User();
    }

    public User createUser() {
        return new User();
    }
}
