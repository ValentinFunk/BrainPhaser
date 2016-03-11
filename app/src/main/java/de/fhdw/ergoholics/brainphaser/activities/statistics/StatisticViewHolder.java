package de.fhdw.ergoholics.brainphaser.activities.statistics;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.mikephil.charting.charts.PieChart;

import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.logic.UserLogicFactory;
import de.fhdw.ergoholics.brainphaser.logic.statistics.StatisticsLogic;
import de.fhdw.ergoholics.brainphaser.model.User;

/**
 * Created by Daniel Hoogen on 09/03/2016
 *
 * The view holder is responsible for the view interaction with each statistic within a
 * RecyclerView.
 */
public class StatisticViewHolder extends RecyclerView.ViewHolder {
    private View mItemView;
    private User mUser;
    private long mCategoryId;

    public StatisticViewHolder(View itemView, User user, long categoryId) {
        super(itemView);
        mItemView = itemView;
        mUser = user;
        mCategoryId = categoryId;
    }

    public void applyDueChart() {
        PieChart chart = (PieChart) mItemView.findViewById(R.id.statisticsChart);
        UserLogicFactory userLogicFactory = new UserLogicFactory();
        StatisticsLogic statisticsLogic = userLogicFactory.createStatisticsLogic(mUser);
        statisticsLogic.fillDueChart(chart, mCategoryId);
    }

    public void applyStageChart() {
        PieChart chart = (PieChart) mItemView.findViewById(R.id.statisticsChart);
        UserLogicFactory userLogicFactory = new UserLogicFactory();
        StatisticsLogic statisticsLogic = userLogicFactory.createStatisticsLogic(mUser);
        statisticsLogic.fillStageChart(chart, mCategoryId);
    }
}
