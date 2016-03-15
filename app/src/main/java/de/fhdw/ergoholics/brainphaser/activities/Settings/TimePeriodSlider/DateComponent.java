package de.fhdw.ergoholics.brainphaser.activities.Settings.TimePeriodSlider;

import android.util.SparseArray;

import de.fhdw.ergoholics.brainphaser.R;

/**
 * Created by funkv on 15.03.2016.
 *
 * Exposes information about the time units(components) for the TimePeriodSlider, such as name and valid ranges.
 */
class DateComponent {
    private static SparseArray<ComponentInfo> info = new SparseArray<>();
    static {
        info.put(DateComponent.MONTHS, new ComponentInfo(R.string.months, 12, 60 * 60 * 24 * 30));
        info.put(DateComponent.WEEKS, new ComponentInfo(R.string.weeks, 3, 60 * 60 * 24 * 7));
        info.put(DateComponent.DAYS, new ComponentInfo(R.string.days, 6, 60 * 60 * 24));
        info.put(DateComponent.HOURS, new ComponentInfo(R.string.hours, 23, 60 * 60));
        info.put(DateComponent.MINUTES, new ComponentInfo(R.string.minutes, 59, 60));
    }

    public static ComponentInfo getInfo(int component) {
        return info.get(component);
    }

    public static int MONTHS = 0;
    public static int WEEKS = 1;
    public static int DAYS = 2;
    public static int HOURS = 3;
    public static int MINUTES = 4;

    /**
     * Container class wrapping the values for easy access.
     * Bundles information about a single unit picker (e.g. Hours, Minutes, Days).
     */
    static class ComponentInfo {
        private int mResourceId;
        private int mRangeMax;
        private long mSecondFactor;

        /**
         * Create a new instance
         * @param resourceId resource id of the string that represents this time component (i.e. Months, Days, Hours)
         * @param rangeMax maximum valid amount until the next unit would be reached (e.g. 12 for hours, day would be the next unit)
         * @param secondFactor how many seconds represent one unit of this class (e.g. 60 for Minutes)
         */
        public ComponentInfo(int resourceId, int rangeMax, long secondFactor) {
            this.mResourceId = resourceId;
            this.mRangeMax = rangeMax;
            this.mSecondFactor = secondFactor;
        }

        public int getRangeMax() {
            return mRangeMax;
        }

        public int getResourceId() {
            return mResourceId;
        }

        public long getSecondFactor() {
            return mSecondFactor;
        }
    }
}
