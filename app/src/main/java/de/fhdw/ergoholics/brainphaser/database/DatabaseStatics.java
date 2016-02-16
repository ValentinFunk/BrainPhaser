package de.fhdw.ergoholics.brainphaser.database;

/**
 * Created by Lars on 16.02.2016.
 * Class for static variables for the database
 */
public class DatabaseStatics {
    //General
    public static final String DATABASE_NAME = "brainphaser.db";
    public static final int DB_VERSION = 1;

    //User Table
    public static final String USER_TABLE_NAME = "user";
    public static final String USER_COL_1 = "id";
    public static final String USER_COL_2 = "name";
    public static final String USER_COL_3 = "avatar";
    public static final String CREATE_TABLE_USER = "CREATE TABLE " + USER_TABLE_NAME + " (" + USER_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + USER_COL_2 + " TEXT NOT NULL, " + USER_COL_3 + " TEXT);";

    //Challenge table - to be filled with data!!!
    public static final String CHALLENGE_TABLE_NAME = "";
    public static final String CHALLENGE_COL_1 = "";
    public static final String CHALLENGE_COL_2 = "";
    public static final String CHALLENGE_COL_3 = "";
    public static final String CHALLENGE_COL_4 = "";
    public static final String CREATE_TABLE_CHALLENGE = "";

    //Category table - to be filled with data!!!
    public static final String CATEGORY_TABLE_NAME = "";
    public static final String CATEGORY_COL_1 = "";
    public static final String CATEGORY_COL_2 = "";
    public static final String CATEGORY_COL_3 = "";
    public static final String CATEGORY_COL_4 = "";
    public static final String CREATE_TABLE_CATEGORY = "";

}
