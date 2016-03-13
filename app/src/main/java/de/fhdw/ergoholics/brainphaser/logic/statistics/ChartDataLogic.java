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
 */
public class ChartDataLogic {
    //Constants
    private static final int NUMBER_PLAYED_LISTED = 5;

    //Attributes
    private User mUser;
    private long mCategoryId;
    private BrainPhaserApplication mApplication;
    private UserLogicFactory mUserLogicFactory;
    private ChallengeDataSource mChallengeDataSource;
    private CompletionDataSource mCompletionDataSource;
    private StatisticsDataSource mStatisticsDataSource;

    private ChartSettings mSettings;

    //Constructor
    public ChartDataLogic(User user, long categoryId, BrainPhaserApplication application, UserLogicFactory userLogicFactory, ChallengeDataSource challengeDataSource, CompletionDataSource completionDataSource, StatisticsDataSource statisticsDataSource) {
        mUser = user;
        mCategoryId = categoryId;
        mApplication = application;
        mUserLogicFactory = userLogicFactory;
        mChallengeDataSource = challengeDataSource;
        mCompletionDataSource = completionDataSource;
        mStatisticsDataSource = statisticsDataSource;

        mSettings = new ChartSettings(application);
    }

    //Methods
    public PieData findDueData() {
        //Calculate numbers for the data to be visualized
        DueChallengeLogic dueChallengeLogic = mUserLogicFactory.createDueChallengeLogic(mUser);

        int dueNumber = dueChallengeLogic.getDueChallenges(mCategoryId).size();
        int notDueNumber = mChallengeDataSource.getByCategoryId(mCategoryId).size() - dueNumber;

        if (dueNumber + notDueNumber > 0) {
            //Create dataset
            ArrayList<Entry> entries = new ArrayList<>();
            ArrayList<String> labels = new ArrayList<>();

            labels.add(mApplication.getString(R.string.challenge_due_text));
            entries.add(new Entry(dueNumber > 0 ? dueNumber : nullValue(notDueNumber), 0));

            labels.add(mApplication.getString(R.string.challeng_not_due_text));
            entries.add(new Entry(notDueNumber > 0 ? notDueNumber : nullValue(dueNumber), 1));

            PieDataSet dataset = new PieDataSet(entries, "");
            mSettings.applyDataSetSettings(dataset);

            //Create data
            PieData data = new PieData(labels, dataset);
            mSettings.applyDataSettings(data);

            return data;
        } else
            return null;
    }

    public PieData findStageData() {
        //Create dataset
        ArrayList <Entry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        //Calculate numbers for the data to be visualized
        int numbers[] = {0,0,0,0,0,0};
        int totalNumber = 0;

        for (int i = 0; i <=5; i++) {
            numbers[i] = mCompletionDataSource.findByUserAndStageAndCategory(mUser, i + 1, mCategoryId).size();
            totalNumber += numbers[i];
        }
        if (totalNumber > 0) {
            for (int i = 0; i <=5; i++) {
                entries.add(new Entry(numbers[i] != 0 ? numbers[i] : nullValue(totalNumber), i));
                labels.add("" + (i+1));
            }
            PieDataSet dataset = new PieDataSet(entries, "");
            mSettings.applyDataSetSettings(dataset);

            //Create data
            PieData data = new PieData(labels, dataset);
            mSettings.applyDataSettings(data);

            return data;
        }
        else
            return null;
    }

    public PieData findMostPlayedData(StatisticType type, List<Long> shownChallenges) {
        //Create dataset
        ArrayList <Entry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        List<Statistics> statistics = mStatisticsDataSource.findByCategoryAndUser(mCategoryId, mUser);

        if (type == StatisticType.TYPE_MOST_FAILED)
            statistics = removeSucceeded(statistics);
        if (type == StatisticType.TYPE_MOST_SUCCEEDED)
            statistics = removeFailed(statistics);

        getMost(entries, labels, statistics, shownChallenges, NUMBER_PLAYED_LISTED);

        if (entries.size() > 1) {
            PieDataSet dataset = new PieDataSet(entries, "");
            mSettings.applyDataSetSettings(dataset);

            //Create data
            PieData data = new PieData(labels, dataset);
            mSettings.applyDataSettings(data);

            return data;
        }
        else
            return null;
    }

    private List<Statistics> removeSucceeded(List<Statistics> statistics) {
        List<Statistics> result = new ArrayList<>();
        for (Statistics statistic : statistics) {
            if (!statistic.getSucceeded())
                result.add(statistic);
        }
        return result;
    }

    private List<Statistics> removeFailed(List<Statistics> statistics) {
        List<Statistics> result = new ArrayList<>();
        for (Statistics statistic : statistics) {
            if (statistic.getSucceeded())
                result.add(statistic);
        }
        return result;
    }

    private void getMost(ArrayList<Entry> entries, ArrayList<String> labels, List<Statistics> statistics, List<Long> shownChallenges, int numberPlayedListed) {
        List<Long> ids = new ArrayList<>();
        List<Integer> amounts = new ArrayList<>();
        for (Statistics statistic : statistics) {
            Long challengeId = statistic.getChallengeId();
            if (ids.contains(challengeId)) {
                int index = ids.indexOf(challengeId);
                Integer amount = amounts.get(index);
                amounts.set(index, amount + 1);
            }
            else {
                ids.add(challengeId);
                amounts.add(1);
            }
        }
        if (ids.size() > 0) {
            for (int i = 0; i < numberPlayedListed; i++) {
                int indexMax = 0;
                for (Long id : ids) {
                    int index = ids.indexOf(id);
                    if (amounts.get(index) > amounts.get(indexMax)) {
                        indexMax = index;
                    }
                }
                if (amounts.get(indexMax) == 0)
                    break;
                entries.add(new Entry(amounts.get(indexMax), i));
                labels.add("");
                shownChallenges.add(ids.get(indexMax));
                amounts.set(indexMax, 0);
            }
        }
    }

    /**
     * Creates a value between 0% and 1% which will be shown as 0% if rounded. This method is
     * necessary due to a bug in the statistics library, which disallows the user to create a pie
     * chart with 0% values or with only 1 date to be shown.
     * @param totalValue the total value of items in the pie chart
     * @return the value which is bigger than 0% but will be shown as 0%
     */
    private float nullValue(float totalValue) {
        return (float) (totalValue * 0.002 * Math.PI);
    }
}
