package de.fhdw.ergoholics.brainphaser.activities.main;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;

import de.fhdw.ergoholics.brainphaser.activities.CategorySelect.SelectCategoryActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by funkv on 29.02.2016.
 */
public class NavigationTabsPagerAdapter extends FragmentStatePagerAdapter {
    private Context mContext;
    public NavigationTabsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);

        mContext = context;
    }

    @Override
    public int getCount() {
        return Navigation.getNavigationItems().size();
    }

    @Override
    public Fragment getItem(int position) {
        return Navigation.getNavigationItems().get(position).getFragment();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        int resourceId = Navigation.getNavigationItems().get(position).getTitleResource();
        return mContext.getString(resourceId);
    }
}
