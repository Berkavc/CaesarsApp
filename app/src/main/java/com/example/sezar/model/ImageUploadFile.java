package com.example.sezar.model;

public class ImageUploadFile {

    public String imageName;

    public String imageURL;

    public ImageUploadFile() {
    }

    public ImageUploadFile(String imageName, String imageURL) {
        this.imageName = imageName;
        this.imageURL = imageURL;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
