package de.fhdw.ergoholics.brainphaser.database;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import de.fhdw.ergoholics.brainphaser.model.Challenge;
import de.fhdw.ergoholics.brainphaser.model.Completion;
import de.fhdw.ergoholics.brainphaser.model.DaoSession;
import de.fhdw.ergoholics.brainphaser.model.Statistics;
import de.fhdw.ergoholics.brainphaser.model.StatisticsDao;
import de.fhdw.ergoholics.brainphaser.model.User;
import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by Daniel Hoogen on 11/03/2016.
 */
public class StatisticsDataSource {
    private DaoSession mDaoSession;

    @Inject
    StatisticsDataSource(DaoSession session) {
        mDaoSession = session;
    }

    public void update(Statistics statistics) {
        mDaoSession.update(statistics);
    }

    public long create(Statistics statistics) {
        return mDaoSession.getStatisticsDao().insert(statistics);
    }

    public List<Statistics> findByCategoryAndUser(long categoryId, User user) {
        List<Statistics> userStatistics = user.getStatistics();
        if (categoryId == CategoryDataSource.CATEGORY_ID_ALL)
            return userStatistics;
        else {
            List<Statistics> statistics = new ArrayList<>();
            for (Statistics statistic : userStatistics) {
                Challenge challenge = mDaoSession.getChallengeDao().load(statistic.getChallengeId());
                if (challenge.getCategoryId() == categoryId) {
                    statistics.add(statistic);
                }
            }
            return statistics;
        }
    }
}