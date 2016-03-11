package de.fhdw.ergoholics.brainphaser.database;

import javax.inject.Inject;
import de.fhdw.ergoholics.brainphaser.model.DaoSession;
import de.fhdw.ergoholics.brainphaser.model.Statistics;

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
}