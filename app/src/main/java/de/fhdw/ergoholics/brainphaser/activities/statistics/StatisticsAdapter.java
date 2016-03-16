package de.fhdw.ergoholics.brainphaser.activities.statistics;

import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.fhdw.ergoholics.brainphaser.BrainPhaserApplication;
import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.database.ChallengeDataSource;
import de.fhdw.ergoholics.brainphaser.logic.UserLogicFactory;
import de.fhdw.ergoholics.brainphaser.logic.statistics.StatisticType;
import de.fhdw.ergoholics.brainphaser.model.User;

/**
 * Created by Daniel Hoogen on 09/03/2016.
 *
 * This adapter adds the StatisticViewHolder objects to the recycler view it is assigned to
 */
public class StatisticsAdapter extends RecyclerView.Adapter<StatisticViewHolder> {
    //Attributes
    private UserLogicFactory mUserLogicFactory;
    private ChallengeDataSource mChallengeDataSource;
    private BrainPhaserApplication mApplication;
    private User mUser;
    private long mCategoryId;
    private boolean mIsLandscape;

    private int mViewNumber;

    //Constructor
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
     * @param parent parent to assign the newly created view to
     * @param viewType ignored
     */
    @Override
    public StatisticViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

        if (mIsLandscape) {
            if (mViewNumber == 1 || mViewNumber == 3)
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

        v.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT));
        mViewNumber++;

        return new StatisticViewHolder(v, mUserLogicFactory, mChallengeDataSource, mApplication, mUser, mCategoryId, parent);
    }

    /**
     * Called to bind the ViewHolder at the given position.
     * @param holder the ViewHolder object to be bound
     * @param position the position where a new ViewHolder is created
     */
    @Override
    public void onBindViewHolder(StatisticViewHolder holder, int position) {
        if (mIsLandscape) {
            if (position==0)
                holder.applyMostPlayedChart(StatisticType.TYPE_MOST_PLAYED);
            else if (position==1)
                holder.applyDueChart();
            else if (position==2)
                holder.applyMostPlayedChart(StatisticType.TYPE_MOST_FAILED);
            else if (position==3)
                holder.applyStageChart();
            else if (position==4)
                holder.applyMostPlayedChart(StatisticType.TYPE_MOST_SUCCEEDED);
        }
        else {
            if (position==0)
                holder.applyDueChart();
            else if (position==1)
                holder.applyStageChart();
            else if (position==2)
                holder.applyMostPlayedChart(StatisticType.TYPE_MOST_PLAYED);
            else if (position==3)
                holder.applyMostPlayedChart(StatisticType.TYPE_MOST_FAILED);
            else if (position==4)
                holder.applyMostPlayedChart(StatisticType.TYPE_MOST_SUCCEEDED);
        }
    }

    /**
     * Returns the count of ViewHolders in the adapter
     * @return the fixed count of ViewHolders
     */
    @Override
    public int getItemCount() {
        return 5;
    }
}
