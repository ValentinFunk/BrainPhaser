package de.fhdw.ergoholics.brainphaser.activities.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import de.fhdw.ergoholics.brainphaser.activities.CategorySelect.SelectCategoryActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by funkv on 29.02.2016.
 */
public class NavigationTabsPagerAdapter extends FragmentStatePagerAdapter {
    private abstract class NavigationItem {
        private String mTitle;

        public NavigationItem(String title) {
            mTitle = title;
        }

        public abstract Fragment getFragment( );

        public String getTitle( ) {
            return mTitle;
        }
    }

    private List<NavigationItem> navItems = new ArrayList<>();

    public NavigationTabsPagerAdapter(FragmentManager fm) {
        super(fm);

        navItems.add(new NavigationItem("Lernen") {
            @Override
            public Fragment getFragment() {
                return new SelectCategoryActivity();
            }
        });
    }

    @Override
    public int getCount() {
        return navItems.size();
    }

    @Override
    public Fragment getItem(int position) {
        return navItems.get(position).getFragment();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return navItems.get(position).getTitle();
    }
}
