package de.fhdw.ergoholics.brainphaser.logic;

import javax.inject.Inject;

import de.fhdw.ergoholics.brainphaser.BrainPhaserApplication;
import de.fhdw.ergoholics.brainphaser.database.ChallengeDataSource;
import de.fhdw.ergoholics.brainphaser.database.CompletionDataSource;
import de.fhdw.ergoholics.brainphaser.database.StatisticsDataSource;
import de.fhdw.ergoholics.brainphaser.logic.statistics.ChartDataLogic;
import de.fhdw.ergoholics.brainphaser.logic.statistics.ChartSettings;
import de.fhdw.ergoholics.brainphaser.logic.statistics.StatisticsLogic;
import de.fhdw.ergoholics.brainphaser.model.User;

/**
 * Created by funkv on 06.03.2016.
 * <p/>
 * Factory that is used to create logic objects which require a user.
 * Dependencies are injected automatically.
 */
public class UserLogicFactory {
    @Inject
    BrainPhaserApplication mApplication;
    @Inject
    CompletionDataSource mCompletionDataSource;
    @Inject
    ChallengeDataSource mChallengeDataSource;
    @Inject
    StatisticsDataSource mStatisticsDataSource;
    @Inject
    ChartSettings mSettings;

    /**
     * Create a DueChallengeLogic for the specified user.
     *
     * @param user user whose challenges are inspected
     * @return the DueChallengeLogic object
     */
    public DueChallengeLogic createDueChallengeLogic(User user) {
        return new DueChallengeLogic(user, mCompletionDataSource, mChallengeDataSource);
    }

    /**
     * Creates ChartDataLogic
     *
     * @param user
     * @param categoryId category to inspect
     * @return ChartDataLogic
     */
    public ChartDataLogic createChartDataLogic(User user, long categoryId) {
        return new ChartDataLogic(user,
                categoryId,
                mApplication,
                mChallengeDataSource,
                mCompletionDataSource,
                mStatisticsDataSource,
                this);
    }

    /**
     * Create a DueChallengeLogic for the specified user.
     *
     * @param user       user whose challenges are inspected
     * @param categoryId category to inspect
     * @return the DueChallengeLogic object
     */
    public StatisticsLogic createStatisticsLogic(User user, long categoryId) {
        return new StatisticsLogic(mApplication, mSettings, createChartDataLogic(user, categoryId));
    }
}
