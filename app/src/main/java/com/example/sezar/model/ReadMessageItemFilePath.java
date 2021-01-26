package com.example.sezar.model;

public class ReadMessageItemFilePath {
    private String filePath,fileUrl;
    private String cipheringKey;

    public ReadMessageItemFilePath() {
    }

    public ReadMessageItemFilePath(String filePath, String fileUrl, String cipheringKey) {
        this.filePath = filePath;
        this.fileUrl = fileUrl;
        this.cipheringKey = cipheringKey;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getCipheringKey() {
        return cipheringKey;
    }

    public void setCipheringKey(String cipheringKey) {
        this.cipheringKey = cipheringKey;
    }
}
