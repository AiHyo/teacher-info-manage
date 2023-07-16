package com.aih.utils;

import com.aih.entity.Teacher;

public class UserInfoContext {
    private static ThreadLocal<Teacher> threadLocal = new ThreadLocal<>();

    public static void setTeacher(Teacher teacher)
    {
        threadLocal.set(teacher);
    }
    public static Teacher getTeacher(){
        return threadLocal.get();
    }
    public static void remove(){
        threadLocal.remove();
    }

}
