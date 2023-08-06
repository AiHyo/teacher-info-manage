package com.aih.utils;

import com.aih.utils.vo.User;

public class UserInfoContext {
    private static ThreadLocal<User> threadLocal = new ThreadLocal<>();

    public static void setUser(User user)
    {
        threadLocal.set(user);
    }
    public static User getUser(){
        return threadLocal.get();
    }
    public static void remove(){
        threadLocal.remove();
    }

}
