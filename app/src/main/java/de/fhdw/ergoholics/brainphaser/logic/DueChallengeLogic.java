package de.fhdw.ergoholics.brainphaser.logic;

import android.support.v4.util.LongSparseArray;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.fhdw.ergoholics.brainphaser.database.CategoryDataSource;
import de.fhdw.ergoholics.brainphaser.database.ChallengeDataSource;
import de.fhdw.ergoholics.brainphaser.database.CompletionDataSource;
import de.fhdw.ergoholics.brainphaser.database.SettingsDataSource;
import de.fhdw.ergoholics.brainphaser.model.Category;
import de.fhdw.ergoholics.brainphaser.model.Challenge;
import de.fhdw.ergoholics.brainphaser.model.Completion;
import de.fhdw.ergoholics.brainphaser.model.User;

/**
 * Created by Daniel Hoogen on 25/02/2016.
 * <p/>
 * This class contains the logic for reading due challenges from the databases
 */
public class DueChallengeLogic {
    //Attributes
    private User mUser;

    private CompletionDataSource mCompletionDataSource;
    private ChallengeDataSource mChallengeDataSource;

    /**
     * Constructor which saves the given parameters as member attributes.
     *
     * @param user                 the user to be saved as a member attribute
     * @param completionDataSource the completion data source to be saved as a member attribute
     * @param challengeDataSource  the completion data source to be saved as a member attribute
     */
    public DueChallengeLogic(User user, CompletionDataSource completionDataSource,
                             ChallengeDataSource challengeDataSource) {
        mCompletionDataSource = completionDataSource;
        mChallengeDataSource = challengeDataSource;
        mUser = user;
    }

    /**
     * Returns the amount of due challenges for each category id.
     *
     * @param categories the list of categories
     * @return array that maps category counts to category ids. (Key = CategoryId, Value = Count)
     */
    public LongSparseArray<Integer> getDueChallengeCounts(List<Category> categories) {
        LongSparseArray<Integer> dueChallengeCounts = new LongSparseArray<>();
        for (Category category : categories) {
            int challengesDueCount = getDueChallenges(category.getId()).size();
            dueChallengeCounts.put(category.getId(), challengesDueCount);
        }
        return dueChallengeCounts;
    }

    /**
     * Returns a list with the ids of all due challenges of the given user in the category with the
     * given id. If the given category id is CategoryDataSource.CATEGORY_ID_ALL, the due challenges
     * of all categories will be returned. Challenges without entries in the completed table will
     * be returned and the missing entries will be created.
     *
     * @param categoryId the id of the category whose due challenges will be returned
     * @return List object containing the ids of all due challenges
     */
    public List<Long> getDueChallenges(long categoryId) {
        //Create list that will be returned in the end
        List<Long> dueChallenges = new ArrayList<>();

        //Get due challenges that have entries in the database
        addDueChallengesByCategory(dueChallenges, categoryId);

        //Create missing entries
        createMissingCompletionsByCategory(dueChallenges, categoryId);

        //Return list
        return dueChallenges;
    }

    /**
     * Adds the ids of all due challenges of the given user in the category with the given id to
     * the given list. If the given category id is CategoryDataSource.CATEGORY_ID_ALL, the due
     * challenges of all categories will be added.
     *
     * @param dueChallenges the list object the due challenges will be added to
     * @param categoryId    the id of the category whose due challenges will be returned
     */
    private void addDueChallengesByCategory(List<Long> dueChallenges, long categoryId) {
        //Create objects
        Date now = new Date();
        Date timebox;

        //Check for each stage if their challenges are due
        for (int stage = 1; stage <= 6; stage++) {
            //Get the users completions in the stage
            List<Completion> completedInStage;
            completedInStage = mCompletionDataSource.findByUserAndStage(mUser, stage);

            //Get the timebox for this stage
            timebox = SettingsDataSource.getTimeboxByStage(mUser.getSettings(), stage);

            //Check for all challenges if they are due and in the correct category
            for (Completion completed : completedInStage) {
                Date lastCompleted = completed.getLastCompleted();

                if ((now.getTime() - lastCompleted.getTime() >= timebox.getTime()) &&
                        (categoryId == CategoryDataSource.CATEGORY_ID_ALL
                                || categoryId == completed.getChallenge().getCategoryId())) {
                    dueChallenges.add(completed.getChallengeId());
                }
            }
        }
    }

    /**
     * Returns all challenges that have never been completed by the given user before
     *
     * @return list of uncompleted challenges
     */
    public List<Challenge> getUncompletedChallenges() {
        List<Challenge> notCompleted = new ArrayList<>();

        List<Challenge> challenges = mChallengeDataSource.getAll();

        for (Challenge challenge : challenges) {
            if (mCompletionDataSource.findByChallengeAndUser(challenge.getId(), mUser.getId()) == null) {
                notCompleted.add(challenge);
            }
        }

        return notCompleted;
    }

    /**
     * Adds the ids of all due challenges without entries in the completed table to the given list.
     * The missing entries will also be created in the completed table.
     *
     * @param dueChallenges the list object the due challenges will be added to
     * @param categoryId    the id of the category whose due challenges will be added
     */
    private void createMissingCompletionsByCategory(List<Long> dueChallenges, long categoryId) {
        //Get uncompleted challenges
        List<Challenge> notCompletedYet = getUncompletedChallenges();

        /*
         * If categoryId is CategoryDataSource.CATEGORY_ID_ALL or matches the challenge's id, the
         * challenge will be added to the list and an entry will be created.
         */
        for (Challenge challenge : notCompletedYet) {
            if (categoryId == CategoryDataSource.CATEGORY_ID_ALL
                    || challenge.getCategoryId() == categoryId) {
                long userId = mUser.getId();
                long challengeId = challenge.getId();

                Completion completed;
                completed = new Completion(null, 1, new Date(0L), userId, challengeId);
                mCompletionDataSource.create(completed);
                dueChallenges.add(challenge.getId());
            }
        }
    }
}