package com.example.sezar.model;

public class ReadMessageItem {
private String encryptedMessage, deleteAdapterList,message,phoneNumber,readState,imageValue,audioValue,videoValue,locationValue,documentValue,fileContainer;
private String cipheringKey;
    public ReadMessageItem() {
    }


    public ReadMessageItem(String message,String phoneNumber,String encryptedMessage, String readState,String cipheringKey) {
        this.encryptedMessage = encryptedMessage;
        this.message = message;
        this.phoneNumber = phoneNumber;
        this.readState = readState;
        this.cipheringKey=cipheringKey;
    }

    public String getDocumentValue() {
        return documentValue;
    }

    public void setDocumentValue(String documentValue) {
        this.documentValue = documentValue;
    }

    public ReadMessageItem(String phoneNumber,String cipheringKey) {
        this.phoneNumber = phoneNumber;
        this.cipheringKey=cipheringKey;
    }

    public String getEncryptedMessage() {
        return encryptedMessage;
    }

    public String getFileContainer() {
        return fileContainer;
    }

    public void setFileContainer(String fileContainer) {
        this.fileContainer = fileContainer;
    }

    public void setEncryptedMessage(String encryptedMessage) {
        this.encryptedMessage = encryptedMessage;
    }

    public String getDeleteAdapterList() {
        return deleteAdapterList;
    }

    public void setDeleteAdapterList(String deleteAdapterList) {
        this.deleteAdapterList = deleteAdapterList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getReadState() {
        return readState;
    }

    public void setReadState(String readState) {
        this.readState = readState;
    }

    public String getImageValue() {
        return imageValue;
    }

    public void setImageValue(String imageValue) {
        this.imageValue = imageValue;
    }

    public String getAudioValue() {
        return audioValue;
    }

    public void setAudioValue(String audioValue) {
        this.audioValue = audioValue;
    }

    public String getVideoValue() {
        return videoValue;
    }

    public void setVideoValue(String videoValue) {
        this.videoValue = videoValue;
    }

    public String getLocationValue() {
        return locationValue;
    }

    public void setLocationValue(String locationValue) {
        this.locationValue = locationValue;
    }

    public String getCipheringKey() {
        return cipheringKey;
    }

    public void setCipheringKey(String cipheringKey) {
        this.cipheringKey = cipheringKey;
    }
}
