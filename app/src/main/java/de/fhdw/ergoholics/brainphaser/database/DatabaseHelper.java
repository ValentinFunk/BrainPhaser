package de.fhdw.ergoholics.brainphaser.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Chris on 2/15/2016.
 * DatabaseHelper to create and upgrade the database
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    //Constructor
    public DatabaseHelper(Context context) {
        super(context, DatabaseStatics.DATABASE_NAME, null, DatabaseStatics.DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseStatics.CREATE_TABLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO
    }
}
