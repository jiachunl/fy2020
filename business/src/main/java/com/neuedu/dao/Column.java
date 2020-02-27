package com.neuedu.dao;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})//定义在字段上
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    String value();
}
