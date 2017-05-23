package com.example;

/**
 * Created by Tadej on 26.2.2017.
 */

public class User {
    private String idUser;
    private String nickName;

    public User(String idUser, String nickName) {
        this.idUser = idUser;
        this.nickName = nickName;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public String toString() {
        return "User{" +
                "idUser=" + idUser +
                ", nickName=" + nickName +
                '}';
    }
}
