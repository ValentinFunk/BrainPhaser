package de.fhdw.ergoholics.brainphaser.database;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.fhdw.ergoholics.brainphaser.model.Completion;
import de.fhdw.ergoholics.brainphaser.model.CompletionDao;
import de.fhdw.ergoholics.brainphaser.model.DaoSession;
import de.fhdw.ergoholics.brainphaser.model.User;
import de.greenrobot.dao.query.QueryBuilder;

import javax.inject.Inject;

/**
 * Created by Chris on 2/25/2016.
 */
public class CompletionDataSource {
    public static int ANSWER_RIGHT = 1;
    public static int ANSWER_WRONG = -1;
    private DaoSession mDaoSession;

    @Inject
    CompletionDataSource(DaoSession session) {
        mDaoSession = session;
    }

    public Completion getByChallengeAndUser(long challengeId, long userId) {
        return mDaoSession.getCompletionDao().queryBuilder().where(CompletionDao.Properties.ChallengeId.eq(challengeId), CompletionDao.Properties.UserId.eq(userId)).unique();
    }

    public void update(Completion completed) {
        mDaoSession.update(completed);
    }

    public long create(Completion completed) {
        return mDaoSession.getCompletionDao().insert(completed);
    }

    public List<Completion> getByUserAndStage(User user, int stage) {
        QueryBuilder<Completion> completed = mDaoSession.getCompletionDao().queryBuilder()
            .where(CompletionDao.Properties.UserId.eq(user.getId()),
                CompletionDao.Properties.Stage.eq(stage));
        return completed.list();
    }

    public List<Completion> getByUserAndStageAndCategory(User user, int stage, long categoryId) {
        List<Completion> userStageCompletions = getByUserAndStage(user, stage);
        if (categoryId == CategoryDataSource.CATEGORY_ID_ALL)
            return userStageCompletions;
        else {
            List<Completion> completions = new ArrayList<>();
            for (Completion completion : userStageCompletions) {
                if (completion.getChallengeCompletions().getCategoryId()==categoryId) {
                    completions.add(completion);
                }
            }
            return completions;
        }
    }

    /**
     * @param challengeId The Challenge ID
     * @param userId      The currently loggen in user
     * @param stageUp     1 for StageUp -1 for StageDown (answer right, answer wrong)
     */
    public void updateAfterAnswer(long challengeId, long userId, int stageUp) {
        if (stageUp != ANSWER_WRONG && stageUp != ANSWER_RIGHT) {
            return;
        }
        Completion completed = getByChallengeAndUser(challengeId, userId);
        if (completed == null) {
            completed = new Completion(null, 2, new Date(), userId, challengeId);
            if (stageUp == ANSWER_WRONG) {
                completed.setStage(1);
            }
            create(completed);
        } else {
            completed.setStage(completed.getStage() + stageUp);
            if (completed.getStage() < 1) {
                completed.setStage(1);
            } else if (completed.getStage() > 6) {
                completed.setStage(6);
            }
            completed.setLastCompleted(new Date());
            update(completed);
        }

    }
}
