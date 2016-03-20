package de.fhdw.ergoholics.brainphaser.activities.usersettings.slidertimeperiod;

import de.fhdw.ergoholics.brainphaser.R;

/**
 * Created by funkv on 15.03.2016.
 *
 * Exposes information about the time units(components) for the TimePeriodSlider, such as name and valid ranges.
 */
public class DateComponent {
    public static int WEEKS = 1;
    public static int DAYS = 2;
    public static int HOURS = 3;
    public static int MINUTES = 4;

    private static ComponentInfo[] info = new ComponentInfo[]{
        null, // Skip, WEEKS is index 1
        new ComponentInfo(R.string.weeks, 51),
        new ComponentInfo(R.string.days, 6),
        new ComponentInfo(R.string.hours, 23),
        new ComponentInfo(R.string.minutes, 59)
    };

    public static ComponentInfo getInfo(int component) {
        return info[component];
    }

    /**
     * Container class wrapping the values for easy access.
     * Bundles information about a single unit picker (e.g. Hours, Minutes, Days).
     */
    static class ComponentInfo {
        private int mResourceId;
        private int mRangeMax;

        /**
         * Create a new instance
         * @param resourceId resource id of the string that represents this time component (i.e. Months, Days, Hours)
         * @param rangeMax maximum valid amount until the next unit would be reached (e.g. 12 for hours, day would be the next unit)
         */
        public ComponentInfo(int resourceId, int rangeMax) {
            this.mResourceId = resourceId;
            this.mRangeMax = rangeMax;
        }

        /**
         * Returns the max range of the component info
         *
         * @return the max range attribute
         */
        public int getRangeMax() {
            return mRangeMax;
        }

        /**
         * Returns the ressource id of the component info
         *
         * @return the ressource id attribute
         */
        public int getResourceId() {
            return mResourceId;
        }
    }
}