package de.fhdw.ergoholics.brainphaser;

import android.app.Application;
import android.content.SharedPreferences;

import de.fhdw.ergoholics.brainphaser.database.DaoManager;
import de.fhdw.ergoholics.brainphaser.database.UserDataSource;
import de.fhdw.ergoholics.brainphaser.model.DaoMaster;
import de.fhdw.ergoholics.brainphaser.model.User;

/**
 * Created by funkv on 17.02.2016.
 *
 * Custom Application class for hooking into App creation
 */
public class BrainPhaserApplication extends Application {
    private static final String PREFS_NAME = "BrainPhaserPrefsFile";
    private static final String KEY_PERSISTENT_USER_ID = "loggedInUser";

    private User mCurrentUser;

    /**
     * initializes the DaoManager with a writeable database
     */
    @Override
    public void onCreate() {
        super.onCreate();

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this.getApplicationContext(), DaoManager.DATABASE_NAME, null);
        DaoManager.intialize(helper.getWritableDatabase());
    }

    /**
     * Logs in the last logged in user.
     * @return true, if user was logged in, false if no user has been persisted
     */
    public boolean logInLastUser() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        long lastLoggedInUserId = settings.getLong(KEY_PERSISTENT_USER_ID, -1);
        if (lastLoggedInUserId != -1) {
            User user = UserDataSource.getById(lastLoggedInUserId);
            mCurrentUser = user;
            return true;
        }

        return false;
    }

    /**
     * Get the currently logged in user
     * @return Currently logged in user
     */
    public User getCurrentUser( ) {
        return mCurrentUser;
    }

    /**
     * Switches the current user.
     * @param user the user to log in
     */
    public void switchUser(User user) {
        mCurrentUser = user;
        persistCurrentUser();
    }

    /**
     * Save the selected user to persistent storage
     */
    public void persistCurrentUser() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(KEY_PERSISTENT_USER_ID, mCurrentUser.getId());
        editor.apply();
    }
}
