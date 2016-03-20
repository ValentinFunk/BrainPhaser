package de.fhdw.ergoholics.brainphaser.activities.statistics;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.fhdw.ergoholics.brainphaser.BrainPhaserApplication;
import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.database.ChallengeDataSource;
import de.fhdw.ergoholics.brainphaser.logic.UserLogicFactory;
import de.fhdw.ergoholics.brainphaser.logic.statistics.StatisticType;
import de.fhdw.ergoholics.brainphaser.model.User;

/**
 * Created by Daniel Hoogen on 09/03/2016.
 * <p/>
 * This adapter adds the StatisticViewHolder objects to the recycler view it is assigned to
 */
public class StatisticsAdapter extends RecyclerView.Adapter<StatisticViewHolder> {
    public static final HashMap<StatisticType, Integer> VIEW_TYPE_MAP = new HashMap<>();

    static {
        VIEW_TYPE_MAP.put(StatisticType.TYPE_DUE, StatisticViewHolder.TYPE_SMALL);
        VIEW_TYPE_MAP.put(StatisticType.TYPE_STAGE, StatisticViewHolder.TYPE_SMALL);
        VIEW_TYPE_MAP.put(StatisticType.TYPE_MOST_PLAYED, StatisticViewHolder.TYPE_LARGE);
        VIEW_TYPE_MAP.put(StatisticType.TYPE_MOST_FAILED, StatisticViewHolder.TYPE_LARGE);
        VIEW_TYPE_MAP.put(StatisticType.TYPE_MOST_SUCCEEDED, StatisticViewHolder.TYPE_LARGE);
    }

    //Attributes
    private UserLogicFactory mUserLogicFactory;
    private ChallengeDataSource mChallengeDataSource;
    private BrainPhaserApplication mApplication;
    private User mUser;
    private long mCategoryId;
    private List<StatisticType> mStatisticItems;

    /**
     * This constructor saves the given parameters as member attributes and sets the value of the
     * view number.
     *
     * @param userLogicFactory    the user logic factory to be saved as a member attribute
     * @param challengeDataSource the challenge data source to be saved as a member attribute
     * @param application         the BrainPhaserApplication to be saved as a member attribute
     * @param user                the user to be saved as a member attribute
     * @param categoryId          the category id to be saved as a member attribute
     */
    public StatisticsAdapter(UserLogicFactory userLogicFactory,
                             ChallengeDataSource challengeDataSource,
                             BrainPhaserApplication application, User user, long categoryId,
                             List<StatisticType> itemsToShow) {
        mUserLogicFactory = userLogicFactory;
        mChallengeDataSource = challengeDataSource;
        mApplication = application;
        mUser = user;
        mCategoryId = categoryId;

        mStatisticItems = new ArrayList<>();
        setStatisticItems(itemsToShow);
    }

    /**
     * Called to create the ViewHolder at the given position.
     *
     * @param parent   parent to assign the newly created view to
     * @param viewType ignored
     */
    @Override
    public StatisticViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        int layout;

        Context parentContext = parent.getContext();

        if (viewType == StatisticViewHolder.TYPE_LARGE) {
            layout = R.layout.list_item_statistic_most_played;

        } else if (viewType == StatisticViewHolder.TYPE_SMALL) {
            layout = R.layout.list_item_statistic_pie_chart;
        } else {
            throw new RuntimeException("Invalid view type!");
        }

        v = LayoutInflater.from(parentContext).inflate(layout, parent, false);

        LinearLayoutCompat.LayoutParams layoutParams = new LinearLayoutCompat.LayoutParams(
                LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT);

        v.setLayoutParams(layoutParams);

        return new StatisticViewHolder(v, mUserLogicFactory, mChallengeDataSource, mApplication,
                mUser, mCategoryId);
    }

    /**
     * Returns the type of the item view at the given position
     *
     * @param position the position of the item view
     * @return the type of the item view
     */
    @Override
    public int getItemViewType(int position) {
        StatisticType type = mStatisticItems.get(position);
        return VIEW_TYPE_MAP.get(type);
    }

    /**
     * Called to bind the ViewHolder at the given position.
     *
     * @param holder   the ViewHolder object to be bound
     * @param position the position of the ViewHolder
     */
    @Override
    public void onBindViewHolder(StatisticViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case StatisticViewHolder.TYPE_SMALL:
                switch (mStatisticItems.get(position)) {
                    case TYPE_DUE:
                        holder.applyDueChart();
                        break;
                    case TYPE_STAGE:
                        holder.applyStageChart();
                        break;
                }
                break;
            case StatisticViewHolder.TYPE_LARGE:
                holder.applyMostPlayedChart(mStatisticItems.get(position));
                break;
        }
    }

    /**
     * Returns the count of ViewHolders in the adapter
     *
     * @return the count of ViewHolders
     */
    @Override
    public int getItemCount() {
        return mStatisticItems.size();
    }

    /**
     * Sets the statistic items member to the parameter object
     *
     * @param statisticItems the statistic items to be shown in the recycler view
     */
    public void setStatisticItems(List<StatisticType> statisticItems) {
        mStatisticItems.clear();
        mStatisticItems.addAll(statisticItems);
    }
}