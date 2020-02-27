package com.neuedu.dao;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Test {
    public static void main(String[] args) {
        User user1 = new User();
        user1.setId(10);//查询id为10的用户

        User user2 = new User();
        user2.setUsername("张三");//查询用户名为张三的用户
        user2.setAge(18);

        User user3 = new User();
        user3.setEmail("12@qq.com,zh@qq.com,kkkk@qq.com");//查询邮箱为任意一个的用户

        String sq1 =query(user1);
        String sq2 =query(user2);
        String sq3 =query(user3);

        System.out.println(sq1);
        System.out.println(sq2);
        System.out.println(sq3);

    }

    private static  String query(User user) {
        StringBuilder sb = new StringBuilder();
        //1.获取到User类的class
        Class c = user.getClass();
        //2.获取到table的名字,
        //A.isAnnotationPresent(B.class)；意思就是：注解B是否在此A上。如果在则返回true；不在则返回false。
        boolean isTaAno = c.isAnnotationPresent(Table.class);
        if (!isTaAno){
            return null;
        }
        //获取到注解实例
        Table table = (Table) c.getAnnotation(Table.class);
        //获取注解的值(table的名字)
        String tableName = table.value();
        sb.append("select * from ").append(tableName).append(" where 1=1");
        //3.遍历所有字段
        Field[] declaredFields = c.getDeclaredFields();
        for (Field f:declaredFields){
            //4.处理每个字段对应的sql
            //4.1拿到列名(注解的值)
            boolean isCoAno = f.isAnnotationPresent(Column.class);
            //如果该字段上没有Column注解就跳出
            if(!isCoAno){
                continue;
            }
            Column column = f.getAnnotation(Column.class);
            String columnName = column.value();//列名(其实也就是注解的成员value的值)
            //4.2拿到字段的值
            String fieldName = f.getName();//字段名字
            //拿到get方法的名字
            String getMethodName = "get"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
            Object fieldValue = null;
            try {
                Method getMethod = c.getMethod(getMethodName);
                //调用user的get方法取得字段的值
                fieldValue = getMethod.invoke(user);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //4.3拼装sql
            if (fieldValue==null||(fieldValue instanceof Integer && (Integer)fieldValue==0)){
                continue;
            }
            sb.append(" and ").append(columnName);
            if (fieldValue instanceof String){
                if (((String) fieldValue).contains(",")){
                    String[] values = ((String) fieldValue).split(",");
                    sb.append(" in(");
                    for (String v:values) {
                        sb.append("'").append(v).append("'").append(",");
                    }
                    sb.deleteCharAt(sb.length()-1);
                    sb.append(")");
                }else {
                    sb.append("=").append("'").append(fieldValue).append("'");
                }
            }else if(fieldValue instanceof Integer){
                sb.append("=").append(fieldValue);
            }
        }//注意:其他類型可以仿照int以及String来写

        return sb.toString();
    }
}

