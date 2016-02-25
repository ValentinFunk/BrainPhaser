package de.fhdw.ergoholics.brainphaser.database;

import java.util.Date;

import de.fhdw.ergoholics.brainphaser.model.CompletedDao;
import de.fhdw.ergoholics.brainphaser.model.Settings;
import de.fhdw.ergoholics.brainphaser.model.SettingsDao;
import de.fhdw.ergoholics.brainphaser.model.User;

/**
 * Created by Daniel Hoogen on 25/02/2016.
 */
public class SettingsDataSource {
    public static Settings getById(long id) {
        return DaoManager.getSession().getSettingsDao().load(id);
    }

    public static Settings getByUserId(long userId) {
        return DaoManager.getSession().getSettingsDao().queryBuilder()
                .where(SettingsDao.Properties.UserId.eq(userId)).unique();
    }
}
