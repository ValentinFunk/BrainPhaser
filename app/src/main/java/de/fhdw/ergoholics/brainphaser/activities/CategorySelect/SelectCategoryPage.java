package de.fhdw.ergoholics.brainphaser.activities.CategorySelect;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.util.LongSparseArray;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.fhdw.ergoholics.brainphaser.BrainPhaserApplication;
import de.fhdw.ergoholics.brainphaser.BrainPhaserComponent;
import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.activities.BrainPhaserFragment;
import de.fhdw.ergoholics.brainphaser.activities.Challenge.ChallengeActivity;
import de.fhdw.ergoholics.brainphaser.database.CategoryDataSource;
import de.fhdw.ergoholics.brainphaser.logic.DueChallengeLogic;
import de.fhdw.ergoholics.brainphaser.logic.UserLogicFactory;
import de.fhdw.ergoholics.brainphaser.logic.UserManager;
import de.fhdw.ergoholics.brainphaser.model.Category;
import de.fhdw.ergoholics.brainphaser.model.User;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by funkv on 17.02.2016.
 */
public class SelectCategoryPage extends BrainPhaserFragment implements CategoryAdapter.SelectionListener {
    RecyclerView mRecyclerView;
    List<Category> mCategories;
    private LongSparseArray<Integer> mDueChallengeCounts = new LongSparseArray<>();

    private DueChallengeLogic mDueChallengeLogic;
    @Inject UserManager mUserManager;
    @Inject CategoryDataSource mCategoryDataSource;
    @Inject UserLogicFactory mUserLogicFactory;

    @Override
    protected void injectComponent(BrainPhaserComponent component) {
        component.inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDueChallengeLogic = mUserLogicFactory.createDueChallengeLogic(mUserManager.getCurrentUser());
        mCategories = mCategoryDataSource.getAll();
        sortCategories();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_select_category, container, false);
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerView);

        // get 300dpi in px
        float cardWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300.f, getResources().getDisplayMetrics());
        boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        int spans = (int)Math.floor(getResources().getDisplayMetrics().widthPixels / cardWidth);
        int orientation = isLandscape ? StaggeredGridLayoutManager.VERTICAL : StaggeredGridLayoutManager.VERTICAL;

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(spans, orientation);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setAdapter(new CategoryAdapter(mCategories, mDueChallengeCounts, this));

        return rootView;
    }

    /**
     * Reloads the due challenge counts from the database.
     */
    private void refreshChallengeCounts( ) {
        mDueChallengeCounts = mDueChallengeLogic.getDueChallengeCounts(mCategories);
    }

    /**
     * Reloads due counts and sorts categories so that those with more due challenges appear
     * further up in the list.
     */
    private void sortCategories( ) {
        refreshChallengeCounts();
        Collections.sort(mCategories, new Comparator<Category>() {
            @Override
            public int compare(Category lhs, Category rhs) {
                return mDueChallengeCounts.get(rhs.getId()) - mDueChallengeCounts.get(lhs.getId());
            }
        });
    }

    // Sort categories when activity is started, to make sure the set is sorted when returning
    // from challenge solving
    @Override
    public void onStart() {
        super.onStart();

        sortCategories();
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onCategorySelected(Category category) {
        Intent intent = new Intent(getContext(), ChallengeActivity.class);
        intent.putExtra(ChallengeActivity.EXTRA_CATEGORY_ID, category.getId());
        startActivity(intent);
    }

    @Override
    public void onAllCategoriesSelected() {
        // TODO
    }
}
