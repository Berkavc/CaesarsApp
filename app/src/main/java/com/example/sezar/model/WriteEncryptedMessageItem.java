package com.example.sezar.model;

public class WriteEncryptedMessageItem {
    private String  phoneNumber;

    public WriteEncryptedMessageItem(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public WriteEncryptedMessageItem() {
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
