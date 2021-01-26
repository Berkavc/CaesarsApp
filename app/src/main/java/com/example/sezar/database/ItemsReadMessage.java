package com.example.sezar.database;

public class ItemsReadMessage {
    private static final String TABLE_ITEMS = "r";
    private static final String TABLE_ITEMS_FILES = "f";
    private static final String TABLE_ITEMS_PHONE = "readMessagePhone";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_ENCRYPTED_MESSAGE = "encryptedmessage";
    private static final String COLUMN_DELETE_ADAPTER_LIST = "deleteadapter";
    private static final String COLUMN_MESSAGE = "message";
    private static final String COLUMN_PHONE_NUMBER = "phonenumberlocal";
    private static final String COLUMN_READ_STATE = "readstate";
    private static final String COLUMN_IMAGE_VALUE = "imagevalue";
    private static final String COLUMN_AUDIO_VALUE = "audiovalue";
    private static final String COLUMN_VIDEO_VALUE = "videovalue";
    private static final String COLUMN_LOCATION_VALUE = "locationvalue";
    private static final String COLUMN_FILE_VALUE = "filevalue";
    private static final String COLUMN_DOCUMENT_VALUE = "documentvalue";
    private static final String COLUMN_CIPHERING_KEY = "cipheringkey";





    public static final String SQL_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_ITEMS + "(" +
                    COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    COLUMN_MESSAGE +" TEXT,"+
                    COLUMN_PHONE_NUMBER +" TEXT,"+
                    COLUMN_ENCRYPTED_MESSAGE +" TEXT, "+
                    COLUMN_READ_STATE +" TEXT, "+
                    COLUMN_CIPHERING_KEY +" TEXT" + ");";



    public static final String SQL_CREATE_PHONE =
            "CREATE TABLE IF NOT EXISTS  " + TABLE_ITEMS_PHONE + "(" +
                    COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    COLUMN_PHONE_NUMBER+" TEXT,"+
                    COLUMN_CIPHERING_KEY +" TEXT" + ");";

    public static final String SQL_DELETE =
            "DROP TABLE " + TABLE_ITEMS;


    public static final String SQL_CREATE_FILE =
            "CREATE TABLE IF NOT EXISTS  " + TABLE_ITEMS_FILES + "(" +
                    COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    COLUMN_IMAGE_VALUE +" BLOB,"+
                    COLUMN_AUDIO_VALUE +" TEXT,"+
                    COLUMN_VIDEO_VALUE +" TEXT,"+
                    COLUMN_LOCATION_VALUE +" TEXT,"+
                    COLUMN_DOCUMENT_VALUE +" TEXT,"+
                    COLUMN_FILE_VALUE +" TEXT" + ");";



}
