package de.fhdw.ergoholics.brainphaser.logic;

import java.util.Date;

import de.fhdw.ergoholics.brainphaser.database.CompletionDataSource;
import de.fhdw.ergoholics.brainphaser.model.Completion;

/**
 * Created by Christian Kost
 * <p/>
 * Provides functions for completion logic checking
 */
public class CompletionLogic {

    public static int ANSWER_RIGHT = 1;
    public static int ANSWER_WRONG = -1;

    private CompletionDataSource mCompletionDataSource;

    /**
     * Constructor which saves the given parameters as member attributes.
     *
     * @param completionDataSource the completion data source to be saved as a member attribute
     */
    public CompletionLogic(CompletionDataSource completionDataSource) {
        mCompletionDataSource = completionDataSource;
    }

    /**
     * Updates or inserts a completion object depending on the answer
     * @param challengeId The Challenge ID
     * @param userId      The currently loggen in user
     * @param stageUp     1 for StageUp -1 for StageDown (answer right, answer wrong)
     */
    public void updateAfterAnswer(long challengeId, long userId, int stageUp) {
        if (stageUp != ANSWER_WRONG && stageUp != ANSWER_RIGHT) {
            return;
        }
        Completion completed = mCompletionDataSource.findByChallengeAndUser(challengeId, userId);
        if (completed == null) {
            completed = new Completion(null, 2, new Date(), userId, challengeId);
            if (stageUp == ANSWER_WRONG) {
                completed.setStage(1);
            }
            mCompletionDataSource.create(completed);
        } else {
            completed.setStage(completed.getStage() + stageUp);
            if (completed.getStage() < 1) {
                completed.setStage(1);
            } else if (completed.getStage() > 6) {
                completed.setStage(6);
            }
            completed.setLastCompleted(new Date());
            mCompletionDataSource.update(completed);
        }
    }
}