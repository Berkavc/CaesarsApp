package com.example.sezar.model;

public class LoginScreenItem {
private String userName,password,deviceId,cipheringKey;

    public LoginScreenItem(String userName, String password, String deviceId,String cipheringKey) {
        this.userName = userName;
        this.password = password;
        this.deviceId = deviceId;
        this.cipheringKey=cipheringKey;
    }

    public LoginScreenItem() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getCipheringKey() {
        return cipheringKey;
    }

    public void setCipheringKey(String cipheringKey) {
        this.cipheringKey = cipheringKey;
    }
}
