package de.fhdw.ergoholics.brainphaser.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Christian on 16.02.2016.
 *
 * Class to open and close the database
 */
public class DataSource {


    protected SQLiteDatabase mDatabase;
    protected DatabaseHelper mDbHelper;

    //Constructor
    public DataSource(Context context) {
        mDbHelper = new DatabaseHelper(context);
    }

    /**
     * Open the database connection
     */
    public void open(){
        mDatabase=mDbHelper.getWritableDatabase();
    }

    /**
     * Close the database connection
     */
    public void close(){
        mDbHelper.close();
    }

}