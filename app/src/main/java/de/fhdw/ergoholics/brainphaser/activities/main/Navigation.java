package de.fhdw.ergoholics.brainphaser.activities.main;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseArray;

import de.fhdw.ergoholics.brainphaser.BuildConfig;
import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.activities.CategorySelect.SelectCategoryActivity;

/**
 * Created by funkv on 02.03.2016.
 *
 * In this class Navigation Items are mapped to their Fragments.
 * All Top-Nav items are defined here.
 */
public class Navigation {
    /**
     * Class contains the title and
     */
    public abstract static class NavigationItem {
        private int mTitleResource;

        public NavigationItem(Integer titleResource) {
            mTitleResource = titleResource;
        }

        /**
         * Factory method to create a new Fragment that contains the page of this navigation item.
         * @return Fragment object that will be displayed when the tab is navigated to.
         */
        public abstract Fragment getFragment( );

        public int getTitleResource( ) {
            return mTitleResource;
        }
    }

    /**
     * List of all valid navigation state IDs. Implements Parcelable so that these can be passed
     * through Bundles/Intents.
     */
    public enum NavigationState {
        NAV_LEARN,
        NAV_STATISTICS,
        NAV_HELP
    }

    /**
     * Available navigation items are defined here.
     */
    private static final SparseArray<NavigationItem> navigationItems;
    static {
        navigationItems = new SparseArray<>();

        navigationItems.put(NavigationState.NAV_LEARN.ordinal(), new NavigationItem(R.string.nav_learn) {
            @Override
            public Fragment getFragment() {
                return new SelectCategoryActivity();
            }
        });
    }

    public static SparseArray<NavigationItem> getNavigationItems( ) {
        return navigationItems;
    }
}
