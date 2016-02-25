package de.fhdw.ergoholics.brainphaser.database;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.fhdw.ergoholics.brainphaser.model.Category;
import de.fhdw.ergoholics.brainphaser.model.Challenge;
import de.fhdw.ergoholics.brainphaser.model.Completed;
import de.fhdw.ergoholics.brainphaser.model.CompletedDao;
import de.fhdw.ergoholics.brainphaser.model.Settings;
import de.fhdw.ergoholics.brainphaser.model.User;
import de.greenrobot.dao.query.Join;
import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by Daniel Hoogen on 25/02/2016.
 */
public class ChallengeDataSource {
    public static List<Challenge> getAll() {
        return DaoManager.getSession().getChallengeDao().loadAll();
    }

    public static List<Challenge> getNotCompletedByUser(User user) {
        //Todo: Test
        QueryBuilder challenges = DaoManager.getSession().getChallengeDao().queryBuilder();
        challenges.join(CompletedDao.Properties.ChallengeId, Completed.class)
                .where(CompletedDao.Properties.ChallengeId.isNull(),
                        CompletedDao.Properties.UserId.eq(user.getId()));

        return challenges.list();
    }

    public enum ChallengeType{
        MULTIPLE_CHOICE, TEXT
    }

    public static Challenge getById(long id) {
        return DaoManager.getSession().getChallengeDao().load(id);
    }

    public static long create(Challenge challenge) {
        return DaoManager.getSession().getChallengeDao().insert(challenge);
    }
}
