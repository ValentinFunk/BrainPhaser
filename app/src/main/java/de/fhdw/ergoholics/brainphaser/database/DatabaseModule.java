package de.fhdw.ergoholics.brainphaser.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.fhdw.ergoholics.brainphaser.model.DaoMaster;
import de.fhdw.ergoholics.brainphaser.model.DaoSession;

/**
 * Created by funkv on 06.03.2016.
 * <p/>
 * Modules that provides DataSource objects
 */
@Module
public class DatabaseModule {
    DaoMaster.DevOpenHelper mDevOpenHelper;
    SQLiteDatabase mDatabase;

    /**
     * Constructs a DatabaseModule and instantiates its context and database name
     *
     * @param context      Context
     * @param databaseName Database name
     */
    public DatabaseModule(Context context, String databaseName) {
        mDevOpenHelper = new DaoMaster.DevOpenHelper(context, databaseName, null);
        mDatabase = mDevOpenHelper.getWritableDatabase();
    }

    /**
     * Provides the DaoSession
     *
     * @return DaoSession
     */
    @Provides
    @Singleton
    DaoSession provideSession() {
        DaoMaster daoMaster = new DaoMaster(mDatabase);
        return daoMaster.newSession();
    }

    /**
     * Provides the writeable database
     *
     * @return SQLiteDatabase
     */
    @Provides
    @Singleton
    SQLiteDatabase provideDatabase() {
        return mDevOpenHelper.getWritableDatabase();
    }

    /**
     * Provides the AnswerDataSource
     *
     * @param session Current DaoSession
     * @return AnswerDataSource
     */
    @Provides
    @Singleton
    AnswerDataSource provideAnswerDataSource(DaoSession session) {
        return new AnswerDataSource(session);
    }

    /**
     * Provides the CategoryDataSource
     *
     * @param session Current DaoSession
     * @return CategoryDataSource
     */
    @Provides
    @Singleton
    CategoryDataSource provideCategoryDataSource(DaoSession session) {
        return new CategoryDataSource(session);
    }

    /**
     * Provides the ChallengeDataSource
     *
     * @param session Current DaoSession
     * @return ChallengeDataSource
     */
    @Provides
    @Singleton
    ChallengeDataSource provideChallengeDataSource(DaoSession session) {
        return new ChallengeDataSource(session);
    }

    /**
     * Provides the CompletionDataSource
     *
     * @param session Current DaoSession
     * @return CompletionDataSource
     */
    @Provides
    @Singleton
    CompletionDataSource provideCompletionDataSource(DaoSession session) {
        return new CompletionDataSource(session);
    }

    /**
     * Provides the SettingsDataSource
     *
     * @param session Current DaoSession
     * @return SettingsDataSource
     */
    @Provides
    @Singleton
    SettingsDataSource provideSettingsDataSource(DaoSession session) {
        return new SettingsDataSource(session);
    }

    /**
     * Provides the StatisticsDataSource
     *
     * @param session Current DaoSession
     * @return StatisticsDataSource
     */
    @Provides
    @Singleton
    StatisticsDataSource provideStatisticsDataSource(DaoSession session) {
        return new StatisticsDataSource(session);
    }

    /**
     * Provides the UserDataSource
     *
     * @param session Current DaoSession
     * @return UserDataSource
     */
    @Provides
    @Singleton
    UserDataSource provideUserDataSource(DaoSession session, SettingsDataSource settingsDataSource) {
        return new UserDataSource(session, settingsDataSource);
    }
}
