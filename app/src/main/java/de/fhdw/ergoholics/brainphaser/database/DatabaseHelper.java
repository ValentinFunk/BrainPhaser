package de.fhdw.ergoholics.brainphaser.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Chris on 2/15/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "brainphaser.db";
    public static final int DB_VERSION = 1;
    public static final String USER_TABLE_NAME = "user";
    public static final String USER_COL_1 = "id";
    public static final String USER_COL_2 = "name";
    public static final String USER_COL_3 = "avatar";
    public static final String CREATE_TABLE_USER = "CREATE TABLE " + USER_TABLE_NAME + " (" + USER_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + USER_COL_2 + " TEXT NOT NULL, " + USER_COL_3 + " TEXT);";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO
    }
}
