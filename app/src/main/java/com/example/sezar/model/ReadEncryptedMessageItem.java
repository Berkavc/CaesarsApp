package com.example.sezar.model;

import android.graphics.Bitmap;

import com.example.sezar.fragment.Fragment_Read_Encrypted_Message;
import com.example.sezar.fragment.Fragment_Write_Encrypted_Message;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class ReadEncryptedMessageItem {
    private String  phoneNumber,message;
    private byte [] imageValue;
    private Bitmap bitmap;


    public ReadEncryptedMessageItem() {
    }

    public ReadEncryptedMessageItem(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public ReadEncryptedMessageItem(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public ReadEncryptedMessageItem(byte[] imageValue) {
        this.imageValue = imageValue;
    }

    public byte[] getImageValue() {
        return imageValue;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
