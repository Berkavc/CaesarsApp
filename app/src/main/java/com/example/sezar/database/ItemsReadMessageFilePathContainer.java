package com.example.sezar.database;

public class ItemsReadMessageFilePathContainer {
    public static final String TABLE_ITEMS = "filespath";
    public static final String COLUMN_ID = "id";
    private static final String COLUMN_FILES_PATH = "filespaths";
    private static final String COLUMN_FILES_URL = "filesurls";
    private static final String COLUMN_CIPHERING_KEY = "cipheringkey";



    public static final String SQL_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_ITEMS + "(" +
                    COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    COLUMN_FILES_PATH +" TEXT, "+
                    COLUMN_FILES_URL +" TEXT, "+
                    COLUMN_CIPHERING_KEY +" TEXT" + ");";


    public static final String SQL_DELETE =
            "DROP TABLE " + TABLE_ITEMS;

}
