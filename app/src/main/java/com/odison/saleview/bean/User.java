package com.odison.saleview.bean;

/**
 * 保存用户登录信息，方便自动登录
 *
 * Created by odison on 2015/12/21.
 */
public class User {
    private String phone;

    private String password;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "phone:["+phone+"],password:["+password+"]";
    }
}
