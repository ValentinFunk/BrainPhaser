package de.fhdw.ergoholics.brainphaser.activities.statistics;

import android.support.v7.widget.GridLayoutManager;

import java.util.List;

import de.fhdw.ergoholics.brainphaser.logic.statistics.StatisticType;

/**
 * Created by Daniel Hoogen on 17/03/2016.
 * <p>
 * This class calculates the span sizes for the ViewHolders in the recycler view used in the
 * statistics activity
 * </p>
 */
public class StatisticsSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {
    //Attributes
    private final List<StatisticType> mTypes;

    /**
     * This constructor saves the given parameters as member attributes.
     *
     * @param types the types to be saved as a member attribute
     */
    public StatisticsSpanSizeLookup(List<StatisticType> types) {
        mTypes = types;
    }

    /**
     * Returns the span size of a ViewHolder depending on the device orientation and the ViewHolder
     * position
     *
     * @param position the position of the ViewHolder
     */
    @Override
    public int getSpanSize(int position) {
        switch (StatisticsAdapter.VIEW_TYPE_MAP.get(mTypes.get(position))) {
            case StatisticViewHolder.TYPE_LARGE:
                return 2;
            case StatisticViewHolder.TYPE_SMALL:
                return 1;
        }
        return 0;
    }
}