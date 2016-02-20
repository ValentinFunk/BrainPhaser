package de.fhdw.ergoholics.brainphaser;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;

import de.fhdw.ergoholics.brainphaser.activities.CategorySelect.SelectCategoryActivity;
import de.fhdw.ergoholics.brainphaser.activities.UserCreation.CreateUserActivity;
import de.fhdw.ergoholics.brainphaser.database.DatabaseHelper;
import de.fhdw.ergoholics.brainphaser.database.User;
import de.fhdw.ergoholics.brainphaser.database.UserDataSource;

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
     * initializes the DatabaseHelper with the Application context
     */
    @Override
    public void onCreate() {
        super.onCreate();
        DatabaseHelper.initialize(getApplicationContext());
    }

    /**
     * Logs in the last logged in user.
     * @return true, if user was logged in, false if no user has been persisted
     */
    public boolean logInLastUser() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        int lastLoggedInUserId = settings.getInt(KEY_PERSISTENT_USER_ID, -1);
        if (lastLoggedInUserId != -1) {
            UserDataSource ds = new UserDataSource();
            User user = ds.getUser(lastLoggedInUserId);
            switchUser(user);
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
        persistUser(user);
    }

    /**
     * Save the selected user to persistent storage
     * @param user to persist
     */
    public void persistUser(User user) {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(KEY_PERSISTENT_USER_ID, user.getId());
    }
}
