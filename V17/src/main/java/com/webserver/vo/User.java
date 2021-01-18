package com.webserver.vo;

/**
 * VO:Vaue Object  值对象
 * 值对象不需要定义什么业务逻辑,这种类的每一个实例就是用来表示一组信息的.
 *
 * User这个类的每一个实例用于表示user.dat文件中的一个用户信息
 */
public class User {
    private String username;
    private String password;
    private String nickname;
    private int age;

    public User(String username, String password, String nickname, int age) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return  username + ","+password+","+nickname+","+age;

    }
}
