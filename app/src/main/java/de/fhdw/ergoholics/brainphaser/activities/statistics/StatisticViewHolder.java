package de.fhdw.ergoholics.brainphaser.activities.statistics;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;

import java.util.List;

import de.fhdw.ergoholics.brainphaser.BrainPhaserApplication;
import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.database.ChallengeDataSource;
import de.fhdw.ergoholics.brainphaser.logic.UserLogicFactory;
import de.fhdw.ergoholics.brainphaser.logic.statistics.StatisticType;
import de.fhdw.ergoholics.brainphaser.logic.statistics.StatisticsLogic;
import de.fhdw.ergoholics.brainphaser.model.User;

/**
 * Created by Daniel Hoogen on 09/03/2016
 * <p/>
 * The view holder is responsible for the view interaction with each statistic within a
 * RecyclerView.
 */
public class StatisticViewHolder extends RecyclerView.ViewHolder {
    //Constants
    public static final int TYPE_SMALL = 0;
    public static final int TYPE_LARGE = 1;

    //Attributes
    private View mItemView;
    private ChallengeDataSource mChallengeDataSource;
    private BrainPhaserApplication mApplication;
    private StatisticsLogic mStatisticsLogic;
    private List<Long> mShownChallenges;

    /**
     * This constructor saves the given parameters as member attributes and sets the value of the
     * view number.
     *
     * @param itemView            the item view to be saved as a member attribute
     * @param userLogicFactory    the user logic factory to be saved as a member attribute
     * @param challengeDataSource the challenge data source to be saved as a member attribute
     * @param application         the BrainPhaserApplication to be saved as a member attribute
     * @param user                the user to be saved as a member attribute
     * @param categoryId          the category id to be saved as a member attribute
     */
    public StatisticViewHolder(View itemView, UserLogicFactory userLogicFactory,
                               ChallengeDataSource challengeDataSource,
                               BrainPhaserApplication application, User user, long categoryId) {
        super(itemView);
        mItemView = itemView;
        mChallengeDataSource = challengeDataSource;
        mApplication = application;

        mStatisticsLogic = userLogicFactory.createStatisticsLogic(user, categoryId);
    }

    /**
     * Applies a due chart to the chart in the mItemView view
     */
    public void applyDueChart() {
        PieChart chart = (PieChart) mItemView.findViewById(R.id.statisticsChart);
        mStatisticsLogic.fillChart(chart, StatisticType.TYPE_DUE);
    }

    /**
     * Applies a stage chart to the chart in the mItemView view
     */
    public void applyStageChart() {
        PieChart chart = (PieChart) mItemView.findViewById(R.id.statisticsChart);
        mStatisticsLogic.fillChart(chart, StatisticType.TYPE_STAGE);
    }

    /**
     * Applies a most played / failed / succeeded chart to the chart in the mItemView view
     *
     * @param type the statistic type of the chart to be applied
     */
    public void applyMostPlayedChart(StatisticType type) {
        //Apply chart
        PieChart chart = (PieChart) mItemView.findViewById(R.id.statisticsChart);
        mShownChallenges = mStatisticsLogic.fillChart(chart, type);

        //Add chart selection listener
        chart.setOnChartValueSelectedListener(new ChartValueSelectedListener(this));

        //Select first entry
        if (chart.getData() != null) {
            chart.highlightValue(0, 0);
            TextView text = (TextView) mItemView.findViewById(R.id.challengeView);
            String question = mChallengeDataSource.getById(mShownChallenges.get(0)).getQuestion();

            if (text != null) text.setText(question);
        }

        TextView title = (TextView) mItemView.findViewById(R.id.titleView);
        if (title != null) title.setText(getTitle(type));
    }

    /**
     * Returns a title string depending on the given statistic type
     *
     * @param type the type of the statistic
     * @return the title string for the statistic type
     */
    private String getTitle(StatisticType type) {
        switch (type) {
            case TYPE_MOST_PLAYED:
                return mApplication.getString(R.string.statistics_most_played);
            case TYPE_MOST_FAILED:
                return mApplication.getString(R.string.statistics_most_failed);
            case TYPE_MOST_SUCCEEDED:
                return mApplication.getString(R.string.statistics_most_succeeded);
        }
        return null;
    }

    /**
     * This method is called when a value in a most played / failed / succeeded chart is selected.
     * It shows the challenge text in the text view of the mItemView view.
     *
     * @param e the entry that has been selected
     */
    public void onValueSelected(Entry e) {
        long challengeId = mShownChallenges.get(e.getXIndex());
        TextView text = (TextView) mItemView.findViewById(R.id.challengeView);
        text.setText(mChallengeDataSource.getById(challengeId).getQuestion());
    }

    /**
     * This method is called when the selected value in a most played / failed / succeeded chart is
     * deselected. It applies a standard text to the mTextView view.
     */
    public void onNothingSelected() {
        TextView text = (TextView) mItemView.findViewById(R.id.challengeView);
        text.setText(mApplication.getString(R.string.statistics_no_challenge_selected));
    }
}