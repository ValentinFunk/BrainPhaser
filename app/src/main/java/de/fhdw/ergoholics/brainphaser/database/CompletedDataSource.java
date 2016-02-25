package de.fhdw.ergoholics.brainphaser.database;

import java.util.Date;
import java.util.List;

import de.fhdw.ergoholics.brainphaser.BrainPhaserApplication;
import de.fhdw.ergoholics.brainphaser.model.Completed;
import de.fhdw.ergoholics.brainphaser.model.CompletedDao;
import de.fhdw.ergoholics.brainphaser.model.User;
import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by Chris on 2/25/2016.
 */
public class CompletedDataSource {

    public static Completed fingByChallengeAndUser(long challengeId, long userId) {
        return DaoManager.getSession().getCompletedDao().queryBuilder().where(CompletedDao.Properties.ChallengeId.eq(challengeId),CompletedDao.Properties.UserId.eq(userId)).unique();
    }

    public static void update (Completed completed){
        DaoManager.getSession().update(completed);
    }

    public static long create(Completed completed) {
        return DaoManager.getSession().getCompletedDao().insert(completed);
    }

    public static List<Completed> getByUserAndStage(User user, int stage) {
        QueryBuilder completed = DaoManager.getSession().getCompletedDao().queryBuilder()
                .where(CompletedDao.Properties.UserId.eq(user.getId()),
                        CompletedDao.Properties.Stage.eq(stage));
        return completed.list();
    }

    /**
     *
     * @param challengeId The Challenge ID
     * @param userId The currently loggen in user
     * @param stageUp 1 for StageUp -1 for StageDown (answer right, answer wrong)
     */
    public static void updateAfterAnswer(long challengeId, long userId, int stageUp){
        if(stageUp!=-1 && stageUp!=1){
            return;
        }
        Completed completed = fingByChallengeAndUser(challengeId,userId);
        if (completed==null){
            completed = new Completed(null, 2, new Date(), userId, challengeId);
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
            completed.setTimeLastCompleted(new Date());
            update(completed);
        }

    }
}
