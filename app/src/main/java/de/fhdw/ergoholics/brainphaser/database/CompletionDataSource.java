package de.fhdw.ergoholics.brainphaser.database;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.fhdw.ergoholics.brainphaser.model.Completion;
import de.fhdw.ergoholics.brainphaser.model.CompletionDao;
import de.fhdw.ergoholics.brainphaser.model.User;
import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by Chris on 2/25/2016.
 */
public class CompletionDataSource {

    public static Completion getByChallengeAndUser(long challengeId, long userId) {
        return DaoManager.getSession().getCompletionDao().queryBuilder().where(CompletionDao.Properties.ChallengeId.eq(challengeId),CompletionDao.Properties.UserId.eq(userId)).unique();
    }

    public static void update (Completion completed){
        DaoManager.getSession().update(completed);
    }

    public static long create(Completion completed) {
        return DaoManager.getSession().getCompletionDao().insert(completed);
    }

    public static List<Completion> getByUserAndStage(User user, int stage) {
        QueryBuilder completions = DaoManager.getSession().getCompletionDao().queryBuilder()
                .where(CompletionDao.Properties.UserId.eq(user.getId()),
                        CompletionDao.Properties.Stage.eq(stage));
        return completions.list();
    }

    public static List<Completion> getByUserAndStageAndCategory(User user, int stage, long categoryId) {
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
     *
     * @param challengeId The Challenge ID
     * @param userId The currently loggen in user
     * @param stageUp 1 for StageUp -1 for StageDown (answer right, answer wrong)
     */
    public static void updateAfterAnswer(long challengeId, long userId, int stageUp){
        if(stageUp!=-1 && stageUp!=1) {
            return;
        }
        Completion completed = getByChallengeAndUser(challengeId,userId);
        if (completed==null){
            completed = new Completion(null, 2, new Date(), userId, challengeId);
            if (stageUp==-1){
                completed.setStage(1);
            }
            create(completed);
        }else{
            completed.setStage(completed.getStage() + stageUp);
            if(completed.getStage()<1){
                completed.setStage(1);
            }else if (completed.getStage()>6){
                completed.setStage(6);
            }
            completed.setLastCompleted(new Date());
            update(completed);
        }

    }
}
