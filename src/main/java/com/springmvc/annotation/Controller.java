package com.springmvc.annotation;

import java.lang.annotation.*;


/**
 * @author think
 */
@Target(ElementType.TYPE)  //元注解
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Controller {
    String value() default "";
}
