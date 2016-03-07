package de.fhdw.ergoholics.brainphaser.database;

import java.util.ArrayList;
import java.util.List;

import de.fhdw.ergoholics.brainphaser.model.Challenge;
import de.fhdw.ergoholics.brainphaser.model.DaoSession;
import de.fhdw.ergoholics.brainphaser.model.User;

import javax.inject.Inject;

/**
 * Created by Daniel Hoogen on 25/02/2016.
 *
 * Data Source class for custom access to challenge table entries in the database
 */
public class ChallengeDataSource {
    private DaoSession mDaoSession;
    private CompletionDataSource mCompletionDataSource;

    @Inject
    ChallengeDataSource(DaoSession session, CompletionDataSource completionDataSource) {
        mDaoSession = session;
        mCompletionDataSource = completionDataSource;
    }

    /**
     * Returns the Settings object with the given id
     * @return Settings object with the given id
     */
    public List<Challenge> getAll() {
        return mDaoSession.getChallengeDao().loadAll();
    }

    /**
     * enum containing the different challenge types
     * MULTIPLE_CHOICE: type for multiple choice questions
     * TEXT: type for text entry challenges
     * DECIDE: type for challenges, where the user makes the decision if his answer was correct
     */


    /**
     * Returns the Challenge object with the given id
     * @param id challenge id in the database
     * @return Challenge object with the given id
     */
    public Challenge getById(long id) {
        return mDaoSession.getChallengeDao().load(id);
    }

    /**
     * Adds a Challenge object to the database
     * @param challenge challege to be created in the challenge table
     * @return id of the created object
     */
    public long create(Challenge challenge) {
        return mDaoSession.getChallengeDao().insert(challenge);
    }

    /**
     * Returns all challenges that have never been completed by the given user before
     * @param user the user whose not completed challenges will be returned
     * @return list of uncompleted challenges
     */
    public List<Challenge> getUncompletedChallenges(User user) {
        List<Challenge> notCompleted = new ArrayList<>();
        long userId = user.getId();

        List<Challenge> challenges = mDaoSession.getChallengeDao().queryBuilder().list();

        for (Challenge challenge : challenges) {
            if (mCompletionDataSource.getByChallengeAndUser(challenge.getId(), userId)==null) {
                notCompleted.add(challenge);
            }
        }

        return notCompleted;
    }
}
