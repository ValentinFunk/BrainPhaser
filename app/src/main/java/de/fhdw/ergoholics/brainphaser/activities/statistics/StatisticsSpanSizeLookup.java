package de.fhdw.ergoholics.brainphaser.activities.statistics;

import android.support.v7.widget.GridLayoutManager;

/**
 * Created by Daniel Hoogen on 17/03/2016.
 * <p>
 * This class calculates the span sizes for the ViewHolders in the recycler view used in the
 * statistics activity
 * </p>
 */
public class StatisticsSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {
    //Attributes
    private boolean mIsLandscape;

    //Constructor
    public StatisticsSpanSizeLookup(boolean isLandscape) {
        mIsLandscape = isLandscape;
    }

    /**
     * Returns the span size of a ViewHolder depending on the device orientation and the ViewHolder
     * position
     *
     * @param position the position of the ViewHolder
     */
    @Override
    public int getSpanSize(int position) {
        if (mIsLandscape) {
            return (position == 1 || position == 3) ? 1 : 2;
        } else {
            return position < 2 ? 1 : 2;
        }
    }
}