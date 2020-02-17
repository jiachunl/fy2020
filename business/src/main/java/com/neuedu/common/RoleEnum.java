package com.neuedu.common;

public enum RoleEnum {
    ADMIN(0,"管理员"),
    USER(1,"普通用户"),
    ;
    private int role;
    private String desc;

    RoleEnum(int role,String desc){
        this.role = role;
        this.desc = desc;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
