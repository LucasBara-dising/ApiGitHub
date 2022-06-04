package com.example.apigithub;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserGit {

    @SerializedName("login")
    @Expose
    private String login;

    @SerializedName("avatar_url")
    @Expose
    private String avatar_url;

    @SerializedName("name")
    @Expose
    private String name;


    //nessecario para o banco inserir
    public UserGit(String vUserNick, String vUserName) {
        this.login=vUserNick;
        this.name=vUserName;
    }

    public UserGit() {

    }


    //construtores
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    @Override
    public String toString() {
        return "login:" + getLogin() +
                "\n avatar_url" + getAvatar_url()+
                "\n name:" + getName();
    }
}
