package de.fhdw.ergoholics.brainphaser.database;


import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Christian on 16.02.2016.
 *
 * Class to open and close the database
 */
public class DataSource {
    protected SQLiteDatabase mDatabase;
    protected DatabaseHelper mDbHelper;

    //Constructor
    public DataSource() {
        mDbHelper = DatabaseHelper.getInstance();
        mDatabase = mDbHelper.getWritableDatabase();
    }
}