package com.neuedu.dao;

import java.lang.annotation.*;

@Target({ElementType.TYPE})//定义在类上
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
    String value();
}

