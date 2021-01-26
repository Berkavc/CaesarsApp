package com.example.sezar.model;

public class LoginScreenCheckBoxItem {
    private String name,cipheringKey;


    public LoginScreenCheckBoxItem(String name, String cipheringKey) {
        this.name = name;
        this.cipheringKey = cipheringKey;
    }

    public String getUserName() {
        return name;
    }

    public void setUserName(String name) {
        this.name = name;
    }

    public String getCipheringKey() {
        return cipheringKey;
    }

    public void setCipheringKey(String cipheringKey) {
        this.cipheringKey = cipheringKey;
    }

    public LoginScreenCheckBoxItem() {
    }
}
