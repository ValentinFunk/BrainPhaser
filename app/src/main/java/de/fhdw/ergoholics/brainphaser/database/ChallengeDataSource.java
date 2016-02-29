package de.fhdw.ergoholics.brainphaser.database;

import java.util.List;

import de.fhdw.ergoholics.brainphaser.model.Challenge;

/**
 * Created by Daniel Hoogen on 25/02/2016.
 *
 * Data Source class for custom access to challenge table entries in the database
 */
public class ChallengeDataSource {
    /**
     * Returns the Settings object with the given id
     * @return Settings object with the given id
     */
    public static List<Challenge> getAll() {
        return DaoManager.getSession().getChallengeDao().loadAll();
    }

    /**
     * enum containing the different challenge types
     * MULTIPLE_CHOICE: type for multiple choice questions
     * TEXT: type for text entry challenges
     * DECIDE: type for challenges, where the user makes the decision if his answer was correct
     */
    public enum ChallengeType{
        MULTIPLE_CHOICE, TEXT,
    }

    /**
     * Returns the Challenge object with the given id
     * @param id challenge id in the database
     * @return Challenge object with the given id
     */
    public static Challenge getById(long id) {
        return DaoManager.getSession().getChallengeDao().load(id);
    }

    /**
     * Adds a Challenge object to the database
     * @param challenge challege to be created in the challenge table
     * @return id of the created object
     */
    public static long create(Challenge challenge) {
        return DaoManager.getSession().getChallengeDao().insert(challenge);
    }
}
