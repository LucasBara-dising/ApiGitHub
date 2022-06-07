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

    @SerializedName("company")
    @Expose
    private String company;

    @SerializedName("location")
    @Expose
    private String location;

    @SerializedName("bio")
    @Expose
    private String bio;

    @SerializedName("public_repos")
    @Expose
    private String public_project;

    @SerializedName("followers")
    @Expose
    private String followers;

    @SerializedName("following")
    @Expose
    private String following;

    @SerializedName("updated_at")
    @Expose
    private String ultimoComit;


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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPublic_project() {
        return public_project;
    }

    public void setPublic_project(String public_project) {
        this.public_project = public_project;
    }

    public String getFollowers() {
        return followers;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }

    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }

    public String getUltimoComit() {
        return ultimoComit;
    }

    public void setUltimoComit(String ultimoComit) {
        this.ultimoComit = ultimoComit;
    }



    @Override
    public String toString() {
        return "login:" + getLogin() +
                "\n avatar_url" + getAvatar_url()+
                "\n name:" + getName()+
                "\n company:" + getCompany()+
                "\n location:" + getLocation()+
                "\n bio:" + getBio()+
                "\n public_repos:" + getPublic_project()+
                "\n followers:" + getFollowers()+
                "\n following:" + getFollowing()+
                "\n updated_at:" + getUltimoComit();
    }
}
