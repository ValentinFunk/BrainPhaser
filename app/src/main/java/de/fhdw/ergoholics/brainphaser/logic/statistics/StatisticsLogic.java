package de.fhdw.ergoholics.brainphaser.logic.statistics;

import android.support.v4.content.ContextCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
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
 * Created by Daniel Hoogen on 05/03/2016.
 *
 * This class contains the logic for creating statistics about due challenges and challenge stages
 */
public class StatisticsLogic {
    //Constants


    //Attributes
    UserLogicFactory mUserLogicFactory;
    User mUser;
    BrainPhaserApplication mApplication ;
    ChallengeDataSource mChallengeDataSource;
    CompletionDataSource mCompletionDataSource;

    ChartSettings mSettings;
    ChartDataLogic mDataLogic;

    //Constructor
    public StatisticsLogic(User user, long categoryId, BrainPhaserApplication application, ChallengeDataSource challengeDataSource, CompletionDataSource completionDataSource, StatisticsDataSource statisticsDataSource, UserLogicFactory userLogicFactory) {
        mApplication = application;
        mUser = user;
        mChallengeDataSource = challengeDataSource;
        mCompletionDataSource = completionDataSource;
        mUserLogicFactory = userLogicFactory;

        mSettings = new ChartSettings(application);
        mDataLogic = new ChartDataLogic(user, categoryId, application, userLogicFactory, challengeDataSource, completionDataSource, statisticsDataSource);
    }

    /**
     * Fills the given pie chart with data for visualizing the shares of due and not due challenges.
     * The data for the current user and the given category is shown.
     * @param chart the chart component to be filled with data and to be formatted
     */
    public void fillDueChart(PieChart chart)
    {
        //Clear the chart for reloading
        chart.clear();

        //Create chart data
        PieData data = mDataLogic.findDueData();

        if (data != null) {
            //Add data to chart
            chart.setData(data);

            //Format the chart
            chart.setCenterText(mApplication.getString(R.string.due_chart_center_text));
            mSettings.applyChartSettings(chart);
        }
        else
            mSettings.applyNoDataSettings(chart);
    }

    /**
     * Fills the given pie chart with data for visualizing the shares of the different stages. The
     * data for user given in the constructor and the given category is shown.
     * @param chart the chart component to be filled with data and to be formatted
     */
    public void fillStageChart(PieChart chart) {
        //Clear the chart for reloading
        chart.clear();

        //Create chart data
        PieData data = mDataLogic.findStageData();

        if (data != null) {
            //Add data to chart
            chart.setData(data);

            //Format the chart
            chart.setCenterText(mApplication.getString(R.string.stage_chart_center_text));
            mSettings.applyChartSettings(chart);
        }
        else
            mSettings.applyNoDataSettings(chart);
    }

    public List<Long> fillMostPlayedChart(PieChart chart, StatisticsMode mode) {
        //Clear the chart for reloading
        chart.clear();

        //Create chart data
        List<Long> shownChallenges = new ArrayList<>();
        PieData data = mDataLogic.findMostPlayedData(mode, shownChallenges);

        if (data != null) {
            //Add data to chart
            chart.setData(data);

            //Format the chart
            chart.setCenterText("");
            mSettings.applyChartSettings(chart);
            chart.getLegend().setEnabled(false);
        }
        else
            mSettings.applyNoDataSettings(chart);

        //Return the list of the challenge ids of the challenges shown in the chart
        return shownChallenges;
    }
}