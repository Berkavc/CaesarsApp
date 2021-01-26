package com.example.sezar.model;

public class ReadMessageItemFile {
private String imageValue,audioValue,videoValue,locationValue,documentValue,fileContainer;
private byte[] imageValueByte;

    public ReadMessageItemFile() {
    }

    public ReadMessageItemFile(String imageValue, String audioValue, String videoValue, String locationValue, String fileContainer) {
        this.imageValue = imageValue;
        this.audioValue = audioValue;
        this.videoValue = videoValue;
        this.locationValue = locationValue;
        this.fileContainer = fileContainer;
    }

    public ReadMessageItemFile(byte[] imageValueByte, String audioValue, String videoValue, String locationValue,String documentValue, String fileContainer) {
        this.imageValueByte = imageValueByte;
        this.audioValue = audioValue;
        this.videoValue = videoValue;
        this.locationValue = locationValue;
        this.documentValue=documentValue;
        this.fileContainer = fileContainer;
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

    public String getFileContainer() {
        return fileContainer;
    }

    public void setFileContainer(String fileContainer) {
        this.fileContainer = fileContainer;
    }

    public byte[] getImageValueByte() {
        return imageValueByte;
    }


    public String getDocumentValue() {
        return documentValue;
    }

    public void setImageValueByte(byte[] imageValueByte) {
        this.imageValueByte = imageValueByte;
    }
}
