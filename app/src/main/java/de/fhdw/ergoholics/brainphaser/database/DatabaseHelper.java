package de.fhdw.ergoholics.brainphaser.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Chris on 2/15/2016.
 * DatabaseHelper to create and upgrade the database
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    // Singleton
    private static DatabaseHelper dbHelper;

    public static DatabaseHelper getInstance(){
        if (dbHelper == null) {
            dbHelper = new DatabaseHelper();
        }
        return  dbHelper;
    }

    //Constructor
    public DatabaseHelper() {
        super(null, DatabaseStatics.DATABASE_NAME, null, DatabaseStatics.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseStatics.CREATE_TABLE_USER);
        db.execSQL(DatabaseStatics.CREATE_TABLE_CHALLENGE);
        db.execSQL(DatabaseStatics.CREATE_TABLE_CATEGORY);
        db.execSQL(DatabaseStatics.CREATE_TABLE_ANSWER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO
    }
}
