package com.springmvc.annotation;

/**
 * @author 白起老师
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
@Retention注解表示Annotation的保留策略
RetentionPolicy.Class：运行时不保留，不可以通过反射读取。
        RetentionPolicy.RUNTIME：运行是保留，可以通过反射读取。
        RetentionPolicy.SOURCE：丢弃。
        *
        */
@Target(value= ElementType.FIELD)
@Retention(value= RetentionPolicy.RUNTIME)
public @interface AutoWired {
       String value();
}
