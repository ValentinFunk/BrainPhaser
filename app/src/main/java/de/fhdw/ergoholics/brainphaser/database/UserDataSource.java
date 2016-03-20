package de.fhdw.ergoholics.brainphaser.database;

import java.util.List;

import javax.inject.Inject;

import de.fhdw.ergoholics.brainphaser.model.DaoSession;
import de.fhdw.ergoholics.brainphaser.model.User;
import de.fhdw.ergoholics.brainphaser.model.UserDao;

/**
 * Created by funkv on 20.02.2016. <p/>
 * Data Source class for custom access to user table entries in the database
 */
public class UserDataSource {
    public final static int MAX_USERNAME_LENGTH = 30;

    private SettingsDataSource mSettingsDataSource;
    private DaoSession mDaoSession;

    /**
     * Constructor defines the daosession
     *
     * @param session            the DaoSession
     * @param settingsDataSource the SettingsDataSource
     */
    @Inject
    UserDataSource(DaoSession session, SettingsDataSource settingsDataSource) {
        mDaoSession = session;
        mSettingsDataSource = settingsDataSource;
    }

    /**
     * Gets all users from the database
     *
     * @return List of all users
     */
    public List<User> getAll() {
        return mDaoSession.getUserDao().loadAll();
    }

    /**
     * Creates a new user and assigns default settings
     *
     * @param user user to create
     * @return id of the created user
     */
    public long create(User user) {
        if (user.getSettingsId() == 0) {
            user.setSettings(mSettingsDataSource.createNewDefaultSettings());
        }

        return mDaoSession.getUserDao().insert(user);
    }

    /**
     * Gets a user by its name
     *
     * @param name Name of the user
     * @return User
     */
    public User findOneByName(String name) {
        return mDaoSession.getUserDao().queryBuilder().where(UserDao.Properties.Name.eq(name)).unique();
    }

    /**
     * Gets a user by its id
     *
     * @param id Id of the user
     * @return User
     */
    public User getById(long id) {
        return mDaoSession.getUserDao().load(id);
    }

    /**
     * Updates a user in the database
     *
     * @param user To updated user
     */
    public void update(User user) {
        mDaoSession.update(user);
    }

    /**
     * Deletes a user from the database
     *
     * @param user To deleted user
     */
    public void delete(User user) {
        mDaoSession.delete(user);
    }
}
