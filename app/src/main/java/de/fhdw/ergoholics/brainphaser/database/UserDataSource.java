package de.fhdw.ergoholics.brainphaser.database;

import de.fhdw.ergoholics.brainphaser.model.User;
import de.fhdw.ergoholics.brainphaser.model.UserDao;

import java.util.List;

/**
 * Created by funkv on 20.02.2016.
 */
public class UserDataSource {
    public static List<User> getAll() {
        return DaoManager.getSession().getUserDao().loadAll();
    }

    /**
     * Creates a new user and assigns default settings
     * @param user user to create
     * @return id of the created user
     */
    public static long create(User user) {
        if (user.getSettings() == null && user.getSettingsId() == 0) {
            user.setSettings(SettingsDataSource.getDefaultSettings());
        }

        return DaoManager.getSession().getUserDao().insert(user);
    }

    public static User findOneByName(String name) {
        return DaoManager.getSession().getUserDao().queryBuilder().where(UserDao.Properties.Name.eq(name)).unique();
    }

    public static User getById(long id) {
        return DaoManager.getSession().getUserDao().load(id);
    }

    public static void update(User user) {
        DaoManager.getSession().update(user);
    }

    public static void delete(User user) {
        DaoManager.getSession().delete(user);
    }
}
