package de.fhdw.ergoholics.brainphaser.database;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import de.fhdw.ergoholics.brainphaser.model.Challenge;
import de.fhdw.ergoholics.brainphaser.model.DaoSession;
import de.fhdw.ergoholics.brainphaser.model.Statistics;
import de.fhdw.ergoholics.brainphaser.model.StatisticsDao;
import de.fhdw.ergoholics.brainphaser.model.User;

/**
 * Created by Daniel Hoogen on 11/03/2016.
 * <p/>
 * Data Source class for custom access to statistics table entries in the database
 */
public class StatisticsDataSource {
    //Attributes
    private DaoSession mDaoSession;

    /**
     * Constructor which saves all given parameters to local member attributes.
     *
     * @param session the session to be saved as a member attribute
     */
    @Inject
    StatisticsDataSource(DaoSession session) {
        mDaoSession = session;
    }

    /**
     * Updates a Statistics object in the database
     *
     * @param statistics representation of the object to be updated
     */
    public void update(Statistics statistics) {
        mDaoSession.update(statistics);
    }

    /**
     * Adds a Statistics object to the database
     *
     * @param statistics statistics entry to be created in the statistics table
     * @return id of the created object
     */
    public long create(Statistics statistics) {
        return mDaoSession.getStatisticsDao().insert(statistics);
    }

    /**
     * Returns all Statistics objects of the given user
     *
     * @param userId     the user whose statistics entries will be returned
     * @return list of Statistics objects of the given user
     */
    public List<Statistics> findByUser(long userId) {
        return mDaoSession.getStatisticsDao().queryBuilder()
                .where(StatisticsDao.Properties.UserId.eq(userId)).list();
    }

    /**
     * Returns all Statistics objects of the given user and category
     *
     * @param categoryId the id of the category whose statistics entries will be returned
     * @param user       the user whose statistics entries will be returned
     * @return list of Statistics objects with the given category id and user
     */
    public List<Statistics> findByCategoryAndUser(long categoryId, User user) {
        List<Statistics> userStatistics = findByUser(user.getId());

        if (categoryId == CategoryDataSource.CATEGORY_ID_ALL)
            return userStatistics;
        else {
            List<Statistics> statistics = new ArrayList<>();
            for (Statistics statistic : userStatistics) {
                Challenge challenge;
                challenge = mDaoSession.getChallengeDao().load(statistic.getChallengeId());

                if (challenge.getCategoryId() == categoryId) {
                    statistics.add(statistic);
                }
            }
            return statistics;
        }
    }
}