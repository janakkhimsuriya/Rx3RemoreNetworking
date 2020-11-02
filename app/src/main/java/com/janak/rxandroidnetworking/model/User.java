package com.janak.rxandroidnetworking.model;

public class User {
    public long id;
    public String firstname;
    public String lastname;
    public boolean isFollowing;

    public User(ApiUser apiUser) {
        this.id = apiUser.id;
        this.firstname = apiUser.firstname;
        this.lastname = apiUser.lastname;
    }
}