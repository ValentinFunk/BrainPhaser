package de.fhdw.ergoholics.brainphaser.database;

import android.support.annotation.NonNull;

import java.util.Date;

import de.fhdw.ergoholics.brainphaser.model.DaoSession;
import de.fhdw.ergoholics.brainphaser.model.Settings;

import org.joda.time.Period;

import javax.inject.Inject;

/**
 * Created by Daniel Hoogen on 25/02/2016.
 *
 * Data Source class for custom access to settings table entries in the database
 */
public class SettingsDataSource {
    // Amount of classes/stages
    public static final int STAGE_COUNT = 6;

    //Attributes
    private DaoSession mDaoSession;

    //Constructor
    @Inject
    SettingsDataSource(DaoSession session) {
        mDaoSession = session;
    }

    /**
     * Returns the Settings object with the given id
     * @param id settings id in the database
     * @return Settings object with the given id
     */
    public Settings getById(long id) {
        return mDaoSession.getSettingsDao().load(id);
    }

    /**
     * Creates a new Settings object with default values in the database.
     * @return Setttings Object containing default values
     */
    public Settings createNewDefaultSettings() {
        Settings settings = new Settings();

        settings.setTimeBoxStage1(new Date(Period.minutes(5).toStandardDuration().getMillis()));
        settings.setTimeBoxStage2(new Date(Period.hours(1).toStandardDuration().getMillis()));
        settings.setTimeBoxStage3(new Date(Period.days(1).toStandardDuration().getMillis()));
        settings.setTimeBoxStage4(new Date(Period.weeks(1).toStandardDuration().getMillis()));
        settings.setTimeBoxStage5(new Date(Period.weeks(4).toStandardDuration().getMillis())); // a Month
        settings.setTimeBoxStage6(new Date(Period.weeks(24).toStandardDuration().getMillis())); // 6 Months

        mDaoSession.getSettingsDao().insert(settings);
        return settings;
    }

    /**
     * Returns the timebox of the given stage in the given Settings object
     * @param settings Settings object whose timebox will be returned
     * @param stage number of the stage whose timebox will be returned
     * @return the Date object containing the timebox of the given settings object
     */
    @NonNull
    public static Date getTimeboxByStage(Settings settings, int stage) {
        switch (stage) {
            case 1:
                return settings.getTimeBoxStage1();
            case 2:
                return settings.getTimeBoxStage2();
            case 3:
                return settings.getTimeBoxStage3();
            case 4:
                return settings.getTimeBoxStage4();
            case 5:
                return settings.getTimeBoxStage5();
            case 6:
                return settings.getTimeBoxStage6();
            default:
                throw new IllegalArgumentException("Attempting to get invalid timebox " + stage);
        }
    }

    /**
     * Removes a Settings object from the database
     * @param settings the representation of the Settings object to be removed from the database
     */
    public void delete(Settings settings) {
        mDaoSession.delete(settings);
    }
}
