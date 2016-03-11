package de.fhdw.ergoholics.brainphaser.activities.statistics;

import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.logic.UserLogicFactory;
import de.fhdw.ergoholics.brainphaser.model.User;

/**
 * Created by Daniel Hoogen on 09/03/2016.
 */
public class StatisticsAdapter extends RecyclerView.Adapter<StatisticViewHolder> {
    private UserLogicFactory mUserLogicFactory;
    private User mUser;
    private long mCategoryId;

    public StatisticsAdapter(UserLogicFactory userLogicFactory, User user, long categoryId) {
        mUserLogicFactory = userLogicFactory;
        mUser = user;
        mCategoryId = categoryId;
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

        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_statistic_pie_chart, parent, false);
        v.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT));

        return new StatisticViewHolder(v, mUserLogicFactory, mUser, mCategoryId);
    }

    // Bind data to the view
    @Override
    public void onBindViewHolder(StatisticViewHolder holder, int position) {
        // Categories
        if (position==0) {
            holder.applyDueChart();
        }
        if (position==1) {
            holder.applyStageChart();
        }
        else {

        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
