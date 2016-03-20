package de.fhdw.ergoholics.brainphaser.logic;

import android.app.Application;
import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.fhdw.ergoholics.brainphaser.database.UserDataSource;
import de.fhdw.ergoholics.brainphaser.model.User;

@Singleton
/**
 * Created by funkv on 06.03.2016.
 *
 * Controls the saving of the last logged in user and provides functions
 * for switching and persisting the current user.
 */
public class UserManager {
    private static final String PREFS_NAME = "BrainPhaserPrefsFile";
    private static final String KEY_PERSISTENT_USER_ID = "loggedInUser";
    private User mCurrentUser;

    private Application mApplication;
    private UserDataSource mUserDataSource;

    /**
     * Constructor of the user manager requires the application and a user data source
     *
     * @param application    Apllication
     * @param userDataSource UserDataSource
     */
    @Inject
    public UserManager(Application application, UserDataSource userDataSource) {
        mApplication = application;
        mUserDataSource = userDataSource;
    }

    /**
     * Logs in the last logged in user.
     *
     * @return true, if user was logged in, false if no user has been persisted
     */
    public boolean logInLastUser() {
        SharedPreferences settings = mApplication.getSharedPreferences(PREFS_NAME, 0);
        long lastLoggedInUserId = settings.getLong(KEY_PERSISTENT_USER_ID, -1);
        if (lastLoggedInUserId != -1) {
            User user = mUserDataSource.getById(lastLoggedInUserId);
            if (user == null) {
                return false;
            }

            mCurrentUser = user;
            return true;
        }

        return false;
    }

    /**
     * Get the currently logged in user
     *
     * @return Currently logged in user
     */
    public User getCurrentUser() {
        return mCurrentUser;
    }

    /**
     * Switches the current user.
     *
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
        SharedPreferences settings = mApplication.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(KEY_PERSISTENT_USER_ID, mCurrentUser.getId());
        editor.apply();
    }
}