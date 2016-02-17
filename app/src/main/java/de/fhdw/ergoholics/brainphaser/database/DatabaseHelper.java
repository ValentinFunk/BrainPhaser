package de.fhdw.ergoholics.brainphaser.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Chris on 2/15/2016.
 * DatabaseHelper to create and upgrade the database
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    // Singleton
    private static DatabaseHelper dbHelper;

    // Called once on app start
    public static void initialize(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public static DatabaseHelper getInstance(){
        return  dbHelper;
    }

    //Constructor
    public DatabaseHelper(Context context) {
        super(context, DatabaseStatics.DATABASE_NAME, null, DatabaseStatics.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseStatics.CREATE_TABLE_USER);
        /* db.execSQL(DatabaseStatics.CREATE_TABLE_CHALLENGE);
        db.execSQL(DatabaseStatics.CREATE_TABLE_CATEGORY);
        db.execSQL(DatabaseStatics.CREATE_TABLE_ANSWER); */
    }

    /**
     * Keep a reference to the database to ensure it is only opened once.
     */
    private SQLiteDatabase mDatabase;
    @Override
    public SQLiteDatabase getWritableDatabase() {
        if (mDatabase == null) {
            mDatabase = super.getWritableDatabase();
        }
        return mDatabase;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 0) {
            db.execSQL(DatabaseStatics.CREATE_TABLE_USER);
            /*db.execSQL(DatabaseStatics.CREATE_TABLE_CHALLENGE);
            db.execSQL(DatabaseStatics.CREATE_TABLE_CATEGORY);
            db.execSQL(DatabaseStatics.CREATE_TABLE_ANSWER);*/
        } else if(oldVersion != newVersion) {
            /*
            db.execSQL("DROP TABLE " + DatabaseStatics.ANSWER_TABLE_NAME);
            db.execSQL("DROP TABLE " + DatabaseStatics.CATEGORY_TABLE_NAME);
            db.execSQL("DROP TABLE " + DatabaseStatics.CHALLENGE_TABLE_NAME);*/
            System.out.println(oldVersion + " " + newVersion);
            db.execSQL("DROP TABLE " + DatabaseStatics.USER_TABLE_NAME);

            db.execSQL(DatabaseStatics.CREATE_TABLE_USER);
            /*db.execSQL(DatabaseStatics.CREATE_TABLE_CHALLENGE);
            db.execSQL(DatabaseStatics.CREATE_TABLE_CATEGORY);
            db.execSQL(DatabaseStatics.CREATE_TABLE_ANSWER);*/
        }
    }
}
