package com.neuedu.reflect;

import com.neuedu.pojo.Product;

public class Test {
    public static void main(String[] args) {
        Class<Product> c = Product.class;
        Product p = new Product();
        Class c2 = p.getClass();
        Class c3 = null;
        try {
            c3 = Class.forName("com.neuedu.pojo.Product");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(c);
        System.out.println(c2);
        System.out.println(c3);

        //构造类的实例
        try {
            Product product = c.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }



}
