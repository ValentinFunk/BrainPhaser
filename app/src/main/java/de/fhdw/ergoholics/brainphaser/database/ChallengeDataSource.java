package de.fhdw.ergoholics.brainphaser.database;

import java.util.ArrayList;
import java.util.List;

import de.fhdw.ergoholics.brainphaser.model.Category;
import de.fhdw.ergoholics.brainphaser.model.Challenge;
import de.fhdw.ergoholics.brainphaser.model.ChallengeDao;
import de.fhdw.ergoholics.brainphaser.model.User;
import de.greenrobot.dao.query.QueryBuilder;

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

    /**
     * Returns all challenges that have never been completed by the given user before
     * @param user the user whose not completed challenges will be returned
     * @return list of uncompleted challenges
     */
    public static List<Challenge> getUncompletedChallenges(User user) {
        List<Challenge> notCompleted = new ArrayList<>();
        long userId = user.getId();

        List<Challenge> challenges = DaoManager.getSession().getChallengeDao().queryBuilder().list();

        for (Challenge challenge : challenges) {
            if (CompletionDataSource.getByChallengeAndUser(challenge.getId(), userId)==null) {
                notCompleted.add(challenge);
            }
        }

        return notCompleted;
    }

    /**
     * Returns all challenges that have the given category id
     * @param categoryId the id of the category whose challenges will be returned
     * @return list of challenges
     */
    public static List<Challenge> getByCategoryId(long categoryId) {
        if (categoryId==CategoryDataSource.CATEGORY_ID_ALL)
            return getAll();
        else {
            QueryBuilder challenges = DaoManager.getSession().getChallengeDao().queryBuilder()
                    .where(ChallengeDao.Properties.CategoryId.eq(categoryId));
            return challenges.list();
        }
    }
}
