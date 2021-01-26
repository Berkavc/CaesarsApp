package com.example.sezar.model;

import java.util.ArrayList;

public class ReadEncryptedMessageListItem {
    private ArrayList message;


    public ReadEncryptedMessageListItem() {
    }

    public ReadEncryptedMessageListItem(ArrayList message) {
        this.message = message;
    }

    public ArrayList getMessage() {
        return message;
    }

    public void setMessage(ArrayList message) {
        this.message = message;
    }
}
