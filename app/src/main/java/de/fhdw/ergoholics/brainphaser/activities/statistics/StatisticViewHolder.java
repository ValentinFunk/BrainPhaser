package de.fhdw.ergoholics.brainphaser.activities.statistics;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.List;

import de.fhdw.ergoholics.brainphaser.BrainPhaserApplication;
import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.database.ChallengeDataSource;
import de.fhdw.ergoholics.brainphaser.logic.UserLogicFactory;
import de.fhdw.ergoholics.brainphaser.logic.statistics.StatisticsLogic;
import de.fhdw.ergoholics.brainphaser.logic.statistics.StatisticsMode;
import de.fhdw.ergoholics.brainphaser.model.User;

/**
 * Created by Daniel Hoogen on 09/03/2016
 *
 * The view holder is responsible for the view interaction with each statistic within a
 * RecyclerView.
 */
public class StatisticViewHolder extends RecyclerView.ViewHolder {
    private View mItemView;
    private UserLogicFactory mUserLogicFactory;
    private ChallengeDataSource mChallengeDataSource;
    private BrainPhaserApplication mApplication;
    private StatisticsLogic mStatisticsLogic;
    private List<Long> mShownChallenges;

    public StatisticViewHolder(View itemView, UserLogicFactory userLogicFactory, ChallengeDataSource challengeDataSource,
                               BrainPhaserApplication application, User user, long categoryId) {
        super(itemView);
        mItemView = itemView;
        mUserLogicFactory = userLogicFactory;
        mChallengeDataSource = challengeDataSource;
        mApplication = application;

        mStatisticsLogic = mUserLogicFactory.createStatisticsLogic(user, categoryId);
    }

    public void applyDueChart() {
        PieChart chart = (PieChart) mItemView.findViewById(R.id.statisticsChart);
        mStatisticsLogic.fillDueChart(chart);
    }

    public void applyStageChart() {
        PieChart chart = (PieChart) mItemView.findViewById(R.id.statisticsChart);
        mStatisticsLogic.fillStageChart(chart);
    }

    public void applyMostPlayedChart(StatisticsMode mode) {
        //Apply chart
        PieChart chart = (PieChart) mItemView.findViewById(R.id.statisticsChart);
        mShownChallenges = mStatisticsLogic.fillMostPlayedChart(chart, mode);

        //Add chart selection listener
        chart.setOnChartValueSelectedListener(
            new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                    long challengeId = mShownChallenges.get(e.getXIndex());
                    TextView text = (TextView) mItemView.findViewById(R.id.challengeView);
                    text.setText(mChallengeDataSource.getById(challengeId).getQuestion());
                }

                @Override
                public void onNothingSelected() {
                    TextView text = (TextView) mItemView.findViewById(R.id.challengeView);
                    text.setText(mApplication.getString(R.string.statistics_no_challenge_selected));
                }
            }
        );

        //Select first entry
        if (chart.getData() != null) {
            chart.highlightValue(0, 0);
            TextView text = (TextView) mItemView.findViewById(R.id.challengeView);
            text.setText(mChallengeDataSource.getById(mShownChallenges.get(0)).getQuestion());
        }

        //Set texts
        String titleText = "";

        switch (mode) {
            case MOST_PLAYED_MODE_ALL:
                titleText = mApplication.getString(R.string.statistics_most_played);
                break;
            case MOST_PLAYED_MODE_FAILED:
                titleText = mApplication.getString(R.string.statistics_most_failed);
                break;
            case MOST_PLAYED_MODE_SUCCEEDED:
                titleText = mApplication.getString(R.string.statistics_most_succeeded);
                break;
        }

        TextView title = (TextView) mItemView.findViewById(R.id.titleView);
        title.setText(titleText);
    }
}
