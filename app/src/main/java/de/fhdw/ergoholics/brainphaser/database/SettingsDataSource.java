package de.fhdw.ergoholics.brainphaser.database;

import java.util.Date;

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

    /**
     * Creates a new Settings object with default values in the database.
     * @return Setttings Object containing default values
     */
    public static Settings getNewDefaultSettings( ) {
        Settings settings = new Settings();

        settings.setTimeBoxStage1(new Date(1000 * 60 * 5));         //5 minutes
        settings.setTimeBoxStage2(new Date(1000 * 60 * 60));        //1 hour
        settings.setTimeBoxStage3(new Date(1000 * 60 * 60 * 24));     //1 day
        settings.setTimeBoxStage4(new Date(1000 * 60 * 60 * 24 * 7));   //7 days
        settings.setTimeBoxStage5(new Date(1000l * 60 * 60 * 24 * 30));  //30 days
        settings.setTimeBoxStage6(new Date(1000l * 60 * 60 * 24 * 180)); //180 days

        DaoManager.getSession().getSettingsDao().insert(settings);
        return settings;
    }
}
