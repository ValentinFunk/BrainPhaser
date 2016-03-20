package de.fhdw.ergoholics.brainphaser.logic.statistics;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import java.util.ArrayList;
import java.util.List;

import de.fhdw.ergoholics.brainphaser.BrainPhaserApplication;
import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.database.ChallengeDataSource;
import de.fhdw.ergoholics.brainphaser.database.CompletionDataSource;
import de.fhdw.ergoholics.brainphaser.database.StatisticsDataSource;
import de.fhdw.ergoholics.brainphaser.logic.DueChallengeLogic;
import de.fhdw.ergoholics.brainphaser.logic.UserLogicFactory;
import de.fhdw.ergoholics.brainphaser.model.Statistics;
import de.fhdw.ergoholics.brainphaser.model.User;

/**
 * Created by Daniel Hoogen on 12/03/2016.
 * <p/>
 * This class contains the logic for creating datasets to be visualized in statistics
 */
public class ChartDataLogic {
    //Constants
    private static final int NUMBER_PLAYED_LISTED = 3;

    //Attributes
    private User mUser;
    private long mCategoryId;
    private BrainPhaserApplication mApplication;
    private UserLogicFactory mUserLogicFactory;
    private ChallengeDataSource mChallengeDataSource;
    private CompletionDataSource mCompletionDataSource;
    private StatisticsDataSource mStatisticsDataSource;

    private ChartSettings mSettings;

    /**
     * Constructor which saves the given parameters as member attributes and creates the chart
     * settings member.
     *
     * @param user                 the user to be saved as a member attribute
     * @param categoryId           the category id to be saved as a member attribute
     * @param application          the BrainPhaserApplication to be saved as a member attribute
     * @param challengeDataSource  the challenge data source to be saved as a member attribute
     * @param completionDataSource the completion data source to be saved as a member attribute
     * @param statisticsDataSource the statistics data source to be saved as a member attribute
     * @param userLogicFactory     the user logic factory  to be saved as a member attribute
     */
    public ChartDataLogic(User user, long categoryId,
                          BrainPhaserApplication application,
                          ChallengeDataSource challengeDataSource,
                          CompletionDataSource completionDataSource,
                          StatisticsDataSource statisticsDataSource,
                          UserLogicFactory userLogicFactory) {
        mUser = user;
        mCategoryId = categoryId;
        mApplication = application;
        mChallengeDataSource = challengeDataSource;
        mCompletionDataSource = completionDataSource;
        mStatisticsDataSource = statisticsDataSource;
        mUserLogicFactory = userLogicFactory;

        mSettings = new ChartSettings(application);
    }

    /**
     * Creates a PieData object containing entries with the numbers of due and not due challenges
     *
     * @return PieData object containing the numbers of the due and not due challenges
     */
    public PieData findDueData() {
        //Calculate numbers for the data to be visualized
        DueChallengeLogic dueChallengeLogic = mUserLogicFactory.createDueChallengeLogic(mUser);

        //Retrieve due numbers
        int dueNumber = dueChallengeLogic.getDueChallenges(mCategoryId).size();
        int notDueNumber = mChallengeDataSource.getByCategoryId(mCategoryId).size() - dueNumber;

        if (dueNumber + notDueNumber > 0) {
            //Create lists
            ArrayList<Entry> entries = new ArrayList<>();
            ArrayList<String> labels = new ArrayList<>();

            //Add entries
            labels.add(mApplication.getString(R.string.challenge_due_text));
            entries.add(new Entry(dueNumber > 0 ? dueNumber : nullValue(notDueNumber), 0));

            labels.add(mApplication.getString(R.string.challeng_not_due_text));
            entries.add(new Entry(notDueNumber > 0 ? notDueNumber : nullValue(dueNumber), 1));

            //Create dataset
            PieDataSet dataset = new PieDataSet(entries, "");
            mSettings.applyDataSetSettings(dataset, StatisticType.TYPE_DUE);

            //Create data
            PieData data = new PieData(labels, dataset);
            mSettings.applyDataSettings(data);

            //Return the PieData object
            return data;
        } else
            return null;
    }

    /**
     * Creates a PieData object containing entries with the numbers of challenges in each stage
     *
     * @return PieData object containing the numbers of the challenges in each stage
     */
    public PieData findStageData() {
        //Create lists
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        //Retrieve stage numbers
        int numbers[] = {0, 0, 0, 0, 0, 0};
        int totalNumber = 0;

        for (int i = 0; i <= 5; i++) {
            numbers[i] = mCompletionDataSource.findByUserAndStageAndCategory(mUser, i + 1,
                    mCategoryId).size();
            totalNumber += numbers[i];
        }
        if (totalNumber > 0) {
            //Add entries
            for (int i = 0; i <= 5; i++) {
                entries.add(new Entry(numbers[i] != 0 ? numbers[i] : nullValue(totalNumber), i));
                labels.add("" + (i + 1));
            }
            //Create dataset
            PieDataSet dataset = new PieDataSet(entries, "");
            mSettings.applyDataSetSettings(dataset, StatisticType.TYPE_STAGE);

            //Create data
            PieData data = new PieData(labels, dataset);
            mSettings.applyDataSettings(data);

            //Return the PieData object
            return data;
        } else
            return null;
    }

    /**
     * Creates a PieData object containing entries of the most played / failed or succeded
     * challenges. Which of these entries are added depends on the given mode. The ids of the
     * challenges are also added to the shownChallenges list.
     *
     * @param type            the type
     * @param shownChallenges a List object the ids of the challenges are added to
     * @return PieData object containing the numbers of the played / failed or succeeded challenges
     */
    public PieData findMostPlayedData(StatisticType type, List<Long> shownChallenges) {
        //Create lists
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        //Retrieve numbers
        List<Statistics> statistics;
        statistics = mStatisticsDataSource.findByCategoryAndUser(mCategoryId, mUser);

        switch (type) {
            case TYPE_MOST_PLAYED:
                //Do nothing
                break;
            case TYPE_MOST_FAILED:
                statistics = removeSucceeded(statistics);
                break;
            case TYPE_MOST_SUCCEEDED:
                statistics = removeFailed(statistics);
                break;
            default:
                //Unexpected type: return null
                return null;
        }

        //Add entries
        shownChallenges.addAll(getMost(entries, labels, statistics, NUMBER_PLAYED_LISTED));

        if (entries.size() > 1) {
            //Create dataset
            PieDataSet dataset = new PieDataSet(entries, "");
            mSettings.applyDataSetSettings(dataset, type);

            //Create data
            PieData data = new PieData(labels, dataset);
            mSettings.applyDataSettings(data);

            //Return the PieData object
            return data;
        } else {
            return null;
        }
    }

    /**
     * Returns a list which contains the failed Statistic objects of the given statistics list
     *
     * @param statistics the list of statistics objects which will be evaluated
     * @return list of failed Statistic objects
     */
    private List<Statistics> removeSucceeded(List<Statistics> statistics) {
        List<Statistics> result = new ArrayList<>();
        for (Statistics statistic : statistics) {
            //Add only failed challenges to the result
            if (!statistic.getSucceeded()) result.add(statistic);
        }
        return result;
    }

    /**
     * Returns a list which contains the succeeded Statistic objects of the given statistics list
     *
     * @param statistics the list of statistics objects which will be evaluated
     * @return list of succeeded Statistic objects
     */
    private List<Statistics> removeFailed(List<Statistics> statistics) {
        List<Statistics> result = new ArrayList<>();
        for (Statistics statistic : statistics) {
            //Add only succeeded challenges to the result
            if (statistic.getSucceeded()) result.add(statistic);
        }
        return result;
    }

    /**
     * Returns a list with the challenges which occur most often in the statistics list. The
     * numberPlayedListed parameter defines how many entries will be added. Additionally Entry
     * objects and labels are created and added to the entries and labels List objects
     *
     * @param entries            the entry list the created Entry objects are added to
     * @param labels             the label list the created label strings are added to
     * @param statistics         the list of statistics objects which will be evaluated
     * @param numberPlayedListed the number of challenges to be added to the shownChallenges list
     * @return list of shown challenges
     */
    private List<Long> getMost(ArrayList<Entry> entries, ArrayList<String> labels,
                               List<Statistics> statistics, int numberPlayedListed) {
        //Create a List object for storing the ids of the shown challenges
        List<Long> shownChallenges = new ArrayList<>();

        //Create List objects for storing challenge ids and the numbers how often they were played
        List<Long> ids = new ArrayList<>();
        List<Integer> amounts = new ArrayList<>();

        //Add the challenge ids to the corresponding list and count the occurences in the other one
        for (Statistics statistic : statistics) {
            Long challengeId = statistic.getChallengeId();
            if (ids.contains(challengeId)) {
                int index = ids.indexOf(challengeId);
                Integer amount = amounts.get(index);
                amounts.set(index, amount + 1);
            } else {
                ids.add(challengeId);
                amounts.add(1);
            }
        }

        //Add the challenges to the shownChallenges list which occur most often
        if (ids.size() > 0) {
            for (int i = 0; i < numberPlayedListed; i++) {
                int indexMax = 0;
                //Find the challenge with the most occurences
                for (Long id : ids) {
                    int index = ids.indexOf(id);
                    if (amounts.get(index) > amounts.get(indexMax)) {
                        indexMax = index;
                    }
                }
                if (amounts.get(indexMax) == 0) break;

                //Create an entry and add it to the corresponding list
                entries.add(new Entry(amounts.get(indexMax), i));
                labels.add("");

                //Add the challenge id to the list of shown challenges
                shownChallenges.add(ids.get(indexMax));

                //Set the count of the added challenge id to 0
                amounts.set(indexMax, 0);
            }
        }

        //Return the list of shown challenges
        return shownChallenges;
    }

    /**
     * Creates a value which is an irrational number and about 0.63% of the given total value
     *
     * @param totalValue the total value of items in the pie chart
     * @return an irrational number of about 0.63% of the total number
     */
    private float nullValue(float totalValue) {
        return (float) (totalValue * 0.002 * Math.PI);
    }
}