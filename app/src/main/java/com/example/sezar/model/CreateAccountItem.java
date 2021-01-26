package com.example.sezar.model;

public class CreateAccountItem {
    private String id, name, password, email;


    public CreateAccountItem(String id, String name, String password, String email) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public CreateAccountItem(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public CreateAccountItem(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public CreateAccountItem(String email) {
        this.email = email;
    }

    public String getUserName() {
        return name;
    }

    public void setUserName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public CreateAccountItem() {
    }
}
