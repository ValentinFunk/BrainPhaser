package de.fhdw.ergoholics.brainphaser.database;

import android.database.sqlite.SQLiteDatabase;

import de.fhdw.ergoholics.brainphaser.model.DaoMaster;
import de.fhdw.ergoholics.brainphaser.model.DaoSession;

/**
 * Created by funkv on 20.02.2016.
 */
public class DaoManager {
    // Singleton
    private static DaoManager singletonInstance;

    public static DaoManager getInstance( ) {
        return singletonInstance;
    }

    // Shortcut delegate to instance
    public static DaoSession getSession( ) {
        return getInstance().getDaoSession();
    }

    public static void intialize(SQLiteDatabase db) {
        singletonInstance = new DaoManager(db);
    }


    DaoMaster mDaoMaster;
    DaoSession mSession;
    SQLiteDatabase mDatabase;

    public static String DATABASE_NAME = "brainphaser-db";

    public DaoManager(SQLiteDatabase database) {
        mDatabase = database;
        mDaoMaster = new DaoMaster(database);
        mSession = mDaoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return mSession;
    }
}
