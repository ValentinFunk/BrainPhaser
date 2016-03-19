package de.fhdw.ergoholics.brainphaser.database;

import android.support.annotation.NonNull;

import org.joda.time.Period;

import java.util.Date;

import javax.inject.Inject;

import de.fhdw.ergoholics.brainphaser.model.DaoSession;
import de.fhdw.ergoholics.brainphaser.model.Settings;

/**
 * Created by Lars Kahra on 25/02/2016.
 *
 * Data Source class for custom access to settings table entries in the database
 */
public class SettingsDataSource {
    // Amount of classes/stages
    public static final int STAGE_COUNT = 6;

    //Attributes
    private DaoSession mDaoSession;

    /**
     * Constructor which saves all given parameters to local member attributes.
     *
     * @param session the session to be saved as a member attribute
     */
    @Inject
    SettingsDataSource(DaoSession session) {
        mDaoSession = session;
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
     * Sets the timebox of the given stage in the given Settings object
     * @param settings Settings object whose timebox will be set
     * @param stage number of the stage whose timebox will be set
     * @param date Date representation of msec duration
     */
    public static void setTimeboxByStage(Settings settings, int stage, Date date) {
        switch (stage) {
            case 1:
                settings.setTimeBoxStage1(date);
                break;
            case 2:
                settings.setTimeBoxStage2(date);
                break;
            case 3:
                settings.setTimeBoxStage3(date);
                break;
            case 4:
                settings.setTimeBoxStage4(date);
                break;
            case 5:
                settings.setTimeBoxStage5(date);
                break;
            case 6:
                settings.setTimeBoxStage6(date);
                break;
            default:
                throw new IllegalArgumentException("Attempting to set invalid timebox " + stage);
        }
    }

    /**
     * Returns the Settings object with the given id
     *
     * @param id settings id in the database
     * @return Settings object with the given id
     */
    public Settings getById(long id) {
        return mDaoSession.getSettingsDao().load(id);
    }

    /**
     * Creates a new Settings object with default values in the database.
     *
     * @return Setttings Object containing default values
     */
    public Settings createNewDefaultSettings() {
        Settings settings = new Settings();
        setToDefaultSettings(settings);
        mDaoSession.getSettingsDao().insert(settings);
        return settings;
    }

    /**
     * Copy all settings parameters from src into dest
     *
     * @param dest destination object
     * @param src  source object
     */
    public void copySettings(Settings dest, Settings src) {
        dest.setTimeBoxStage1(src.getTimeBoxStage1());
        dest.setTimeBoxStage2(src.getTimeBoxStage2());
        dest.setTimeBoxStage3(src.getTimeBoxStage3());
        dest.setTimeBoxStage4(src.getTimeBoxStage4());
        dest.setTimeBoxStage5(src.getTimeBoxStage5());
        dest.setTimeBoxStage6(src.getTimeBoxStage6());
    }

    /**
     * Clones a settings object
     *
     * @param settings object received
     * @return cloned Object
     */
    public Settings cloneSettings(Settings settings) {
        Settings clone = new Settings();
        copySettings(clone, settings);
        return clone;
    }

    /**
     * Applies default values to a settings object
     * @param settings object to reset to defaults
     */
    public void setToDefaultSettings(Settings settings) {
        settings.setTimeBoxStage1(new Date(Period.minutes(5).toStandardDuration().getMillis()));
        settings.setTimeBoxStage2(new Date(Period.hours(1).toStandardDuration().getMillis()));
        settings.setTimeBoxStage3(new Date(Period.days(1).toStandardDuration().getMillis()));
        settings.setTimeBoxStage4(new Date(Period.weeks(1).toStandardDuration().getMillis()));
        settings.setTimeBoxStage5(new Date(Period.weeks(4).toStandardDuration().getMillis())); // a Month
        settings.setTimeBoxStage6(new Date(Period.weeks(24).toStandardDuration().getMillis())); // 6 Months
    }

    /**
     * Removes a Settings object from the database
     * @param settings the representation of the Settings object to be removed from the database
     */
    public void delete(Settings settings) {
        mDaoSession.delete(settings);
    }

    /**
     * Updates settings in the database
     * @param settings object to save changes from
     */
    public void update(Settings settings) {
        mDaoSession.update(settings);
    }
}
