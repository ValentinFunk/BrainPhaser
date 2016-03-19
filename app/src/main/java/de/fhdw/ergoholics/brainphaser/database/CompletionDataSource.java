package de.fhdw.ergoholics.brainphaser.database;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import de.fhdw.ergoholics.brainphaser.model.Completion;
import de.fhdw.ergoholics.brainphaser.model.CompletionDao;
import de.fhdw.ergoholics.brainphaser.model.DaoSession;
import de.fhdw.ergoholics.brainphaser.model.User;
import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by Christian Kost
 * <p/>
 * Data Source class for custom access to completion table entries in the database
 */
public class CompletionDataSource {
    private DaoSession mDaoSession;

    /**
     * Constructor defines the daosession
     * @param session the DaoSession
     */
    @Inject
    CompletionDataSource(DaoSession session) {
        mDaoSession = session;
    }

    /**
     * Find a completion object of a challenge and a user (combined "primary" key)
     * @param challengeId the challenge
     * @param userId the user
     * @return The completion object
     */
    public Completion findByChallengeAndUser(long challengeId, long userId) {
        return mDaoSession.getCompletionDao().queryBuilder().where(CompletionDao.Properties.ChallengeId.eq(challengeId), CompletionDao.Properties.UserId.eq(userId)).unique();
    }

    /**
     * Updates a completion object in the database
     * @param completed Completion
     */
    public void update(Completion completed) {
        mDaoSession.update(completed);
    }

    /**
     * Inserts a completion object in the database
     * @param completed Completion object
     * @return row number
     */
    public long create(Completion completed) {
        return mDaoSession.getCompletionDao().insert(completed);
    }

    /**
     * Get all completion objects depending on the user and the stage
     * @param user the user
     * @param stage the stage
     * @return List of completion objects
     */
    public List<Completion> findByUserAndStage(User user, int stage) {
        QueryBuilder<Completion> completed = mDaoSession.getCompletionDao().queryBuilder()
            .where(CompletionDao.Properties.UserId.eq(user.getId()),
                CompletionDao.Properties.Stage.eq(stage));
        return completed.list();
    }

    /**
     * Get all completion objects depending on the user, the stage and the category
     * @param user the user
     * @param stage the stage
     * @param categoryId the category
     * @return List of completion objects
     */
    public List<Completion> findByUserAndStageAndCategory(User user, int stage, long categoryId) {
        List<Completion> userStageCompletions = findByUserAndStage(user, stage);
        if (categoryId == CategoryDataSource.CATEGORY_ID_ALL)
            return userStageCompletions;
        else {
            List<Completion> completions = new ArrayList<>();
            for (Completion completion : userStageCompletions) {
                if (completion.getChallenge().getCategoryId()==categoryId) {
                    completions.add(completion);
                }
            }
            return completions;
        }
    }
}