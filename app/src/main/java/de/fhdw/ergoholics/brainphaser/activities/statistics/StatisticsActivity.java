package de.fhdw.ergoholics.brainphaser.activities.statistics;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;

import javax.inject.Inject;

import de.fhdw.ergoholics.brainphaser.BrainPhaserApplication;
import de.fhdw.ergoholics.brainphaser.BrainPhaserComponent;
import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.activities.BrainPhaserActivity;
import de.fhdw.ergoholics.brainphaser.activities.Challenge.ChallengeActivity;
import de.fhdw.ergoholics.brainphaser.database.CategoryDataSource;
import de.fhdw.ergoholics.brainphaser.logic.UserLogicFactory;
import de.fhdw.ergoholics.brainphaser.logic.UserManager;
import de.fhdw.ergoholics.brainphaser.model.User;

/**
 * Created by Daniel Hoogen on 09/03/2016.
 */
public class StatisticsActivity extends BrainPhaserActivity {
    private RecyclerView mRecyclerView;
    @Inject
    UserManager mUserManager;
    @Inject
    UserLogicFactory mUserLogicFactory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        //Add toolbar
        Toolbar myChildToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myChildToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        User user = mUserManager.getCurrentUser();
        long categoryId = getIntent().getLongExtra(ChallengeActivity.EXTRA_CATEGORY_ID, CategoryDataSource.CATEGORY_ID_ALL);

        mRecyclerView = (RecyclerView)findViewById(R.id.statisticsRecycler);
        mRecyclerView.setHasFixedSize(true);

        final boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        GridLayoutManager layoutManager = new GridLayoutManager(this, isLandscape ? 3 : 2, GridLayoutManager.VERTICAL, false);
        layoutManager.setSpanSizeLookup(
            new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return position < 3 ? 1 : 2;
                }
        });

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setAdapter(new StatisticsAdapter(mUserLogicFactory, user, categoryId, isLandscape));
    }

    @Override
    protected void injectComponent(BrainPhaserComponent component) {
        component.inject(this);
    }

    // Sort categories when activity is started, to make sure the set is sorted when returning
    // from challenge solving
    @Override
    public void onStart() {
        super.onStart();

        mRecyclerView.getAdapter().notifyDataSetChanged();
    }
}