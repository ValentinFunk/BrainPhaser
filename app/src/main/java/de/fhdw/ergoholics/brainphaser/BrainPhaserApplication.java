package de.fhdw.ergoholics.brainphaser;

import android.app.Application;

import de.fhdw.ergoholics.brainphaser.database.DatabaseHelper;

/**
 * Created by funkv on 17.02.2016.
 *
 * Custom Application class for hooking into App creation
 */
public class BrainPhaserApplication extends Application {
    /**
     * initializes the DatabaseHelper with the Application context
     */
    @Override
    public void onCreate() {
        super.onCreate();
        DatabaseHelper.initialize(getApplicationContext());
    }
}
