package de.fhdw.ergoholics.brainphaser.database;

import de.fhdw.ergoholics.brainphaser.BuildConfig;
import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.model.DaoSession;
import de.fhdw.ergoholics.brainphaser.model.User;
import de.fhdw.ergoholics.brainphaser.model.UserDao;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by funkv on 20.02.2016.
 */
public class UserDataSource {
    public final static int MAX_USERNAME_LENGTH = 30;

    private SettingsDataSource mSettingsDataSource;
    private DaoSession mDaoSession;

    @Inject
    UserDataSource(DaoSession session, SettingsDataSource settingsDataSource) {
        mDaoSession = session;
        mSettingsDataSource = settingsDataSource;
    }

    public List<User> getAll() {
        return mDaoSession.getUserDao().loadAll();
    }

    /**
     * Creates a new user and assigns default settings
     * @param user user to create
     * @return id of the created user
     */
    public long create(User user) {
        if (user.getSettingsId() == 0) {
            user.setSettings(mSettingsDataSource.getNewDefaultSettings());
        }

        return mDaoSession.getUserDao().insert(user);
    }

    public User findOneByName(String name) {
        return mDaoSession.getUserDao().queryBuilder().where(UserDao.Properties.Name.eq(name)).unique();
    }

    public User getById(long id) {
        return mDaoSession.getUserDao().load(id);
    }

    public void update(User user) {
        mDaoSession.update(user);
    }

    public void delete(User user) {
        mDaoSession.delete(user);
    }
}
