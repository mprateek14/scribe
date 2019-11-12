package com.example.scribe.Models;

import androidx.annotation.NonNull;

public class Users {

    String uid;
    String name,email;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Users(){

    }

    @NonNull
    @Override
    public String toString() {
        return "User{"+
                "uid='" + uid + '\'' +
                "name='" + name + '\'' +
                "email='" + email + '\'' +
                '}';

    }
}
