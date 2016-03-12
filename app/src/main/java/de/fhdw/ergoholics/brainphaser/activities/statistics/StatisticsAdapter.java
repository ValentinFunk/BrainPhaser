package de.fhdw.ergoholics.brainphaser.activities.statistics;

import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.fhdw.ergoholics.brainphaser.BrainPhaserApplication;
import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.database.ChallengeDataSource;
import de.fhdw.ergoholics.brainphaser.database.StatisticsDataSource;
import de.fhdw.ergoholics.brainphaser.logic.UserLogicFactory;
import de.fhdw.ergoholics.brainphaser.logic.statistics.StatisticsMode;
import de.fhdw.ergoholics.brainphaser.model.User;

/**
 * Created by Daniel Hoogen on 09/03/2016.
 */
public class StatisticsAdapter extends RecyclerView.Adapter<StatisticViewHolder> {
    private UserLogicFactory mUserLogicFactory;
    private ChallengeDataSource mChallengeDataSource;
    private BrainPhaserApplication mApplication;
    private User mUser;
    private long mCategoryId;
    private boolean mIsLandscape;

    private int mViewNumber;

    public StatisticsAdapter(UserLogicFactory userLogicFactory, ChallengeDataSource challengeDataSource, BrainPhaserApplication application, User user, long categoryId, boolean isLandscape) {
        mUserLogicFactory = userLogicFactory;
        mChallengeDataSource = challengeDataSource;
        mApplication = application;
        mUser = user;
        mCategoryId = categoryId;
        mIsLandscape = isLandscape;

        mViewNumber = 0;
    }

    /**
     * Called to create the ViewHolder at the given position.
     *
     * @param parent   parent to assign the newly created view to
     * @param viewType ignored
     */
    @Override
    public StatisticViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;

        if (mIsLandscape) {
            if (mViewNumber == 0 || mViewNumber == 2)
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_statistic_pie_chart, parent, false);
            else
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_statistic_most_played, parent, false);
        }
        else {
            if (mViewNumber < 2)
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_statistic_pie_chart, parent, false);
            else
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_statistic_most_played, parent, false);
        }

        v.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT));
        mViewNumber++;

        return new StatisticViewHolder(v, mUserLogicFactory, mChallengeDataSource, mApplication, mUser, mCategoryId);
    }

    // Bind data to the view
    @Override
    public void onBindViewHolder(StatisticViewHolder holder, int position) {
        if (mIsLandscape) {
            if (position==0) {
                holder.applyDueChart();
            }
            else if (position==1) {
                holder.applyMostPlayedChart(StatisticsMode.MOST_PLAYED_MODE_ALL);
            }
            else if (position==2) {
                holder.applyStageChart();
            }
            else if (position==3) {
                holder.applyMostPlayedChart(StatisticsMode.MOST_PLAYED_MODE_FAILED);
            }
            else if (position==4) {
                holder.applyMostPlayedChart(StatisticsMode.MOST_PLAYED_MODE_SUCCEEDED);
            }
        }
        else {
            if (position==0) {
                holder.applyDueChart();
            }
            else if (position==1) {
                holder.applyStageChart();
            }
            else if (position==2) {
                holder.applyMostPlayedChart(StatisticsMode.MOST_PLAYED_MODE_ALL);
            }
            else if (position==3) {
                holder.applyMostPlayedChart(StatisticsMode.MOST_PLAYED_MODE_FAILED);
            }
            else if (position==4) {
                holder.applyMostPlayedChart(StatisticsMode.MOST_PLAYED_MODE_SUCCEEDED);
            }
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
