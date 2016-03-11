package de.fhdw.ergoholics.brainphaser.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import de.fhdw.ergoholics.brainphaser.model.DaoMaster;
import de.fhdw.ergoholics.brainphaser.model.DaoSession;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.fhdw.ergoholics.brainphaser.model.Statistics;

/**
 * Created by funkv on 06.03.2016.
 *
 * Modules that provides DataSource objects
 */
@Module
public class DatabaseModule {
    DaoMaster.DevOpenHelper mDevOpenHelper;
    SQLiteDatabase mDatabase;

    public DatabaseModule(Context context, String databaseName) {
        mDevOpenHelper = new DaoMaster.DevOpenHelper(context, databaseName, null);
        mDatabase = mDevOpenHelper.getWritableDatabase();
    }

    @Provides
    @Singleton
    DaoSession provideSession() {
        DaoMaster daoMaster = new DaoMaster(mDatabase);
        return daoMaster.newSession();
    }

    @Provides
    @Singleton
    SQLiteDatabase provideDatabase() {
        return mDevOpenHelper.getWritableDatabase();
    }

    @Provides
    @Singleton
    AnswerDataSource provideAnswerDataSource(DaoSession session) {
        return new AnswerDataSource(session);
    }

    @Provides
    @Singleton
    CategoryDataSource provideCategoryDataSource(DaoSession session) {
        return new CategoryDataSource(session);
    }

    @Provides
    @Singleton
    ChallengeDataSource provideChallengeDataSource(DaoSession session, CompletionDataSource completionDataSource) {
        return new ChallengeDataSource(session, completionDataSource);
    }

    @Provides
    @Singleton
    CompletionDataSource provideCompletionDataSource(DaoSession session) {
        return new CompletionDataSource(session);
    }

    @Provides
    @Singleton
    SettingsDataSource provideSettingsDataSource(DaoSession session) {
        return new SettingsDataSource(session);
    }

    @Provides
    @Singleton
    StatisticsDataSource provideStatisticsDataSource(DaoSession session) {
        return new StatisticsDataSource(session);
    }

    @Provides
    @Singleton
    UserDataSource provideUserDataSource(DaoSession session, SettingsDataSource settingsDataSource) {
        return new UserDataSource(session, settingsDataSource);
    }
}
