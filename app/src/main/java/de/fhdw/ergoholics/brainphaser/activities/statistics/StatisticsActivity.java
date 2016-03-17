package de.fhdw.ergoholics.brainphaser.activities.statistics;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import javax.inject.Inject;

import de.fhdw.ergoholics.brainphaser.BrainPhaserApplication;
import de.fhdw.ergoholics.brainphaser.BrainPhaserComponent;
import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.activities.BrainPhaserActivity;
import de.fhdw.ergoholics.brainphaser.activities.playchallenge.ChallengeActivity;
import de.fhdw.ergoholics.brainphaser.database.CategoryDataSource;
import de.fhdw.ergoholics.brainphaser.database.ChallengeDataSource;
import de.fhdw.ergoholics.brainphaser.logic.UserLogicFactory;
import de.fhdw.ergoholics.brainphaser.logic.UserManager;
import de.fhdw.ergoholics.brainphaser.model.User;

/**
 * Created by Daniel Hoogen on 09/03/2016.
 *
 * This is the activity showing statistics for the user in a specified category
 */
public class StatisticsActivity extends BrainPhaserActivity {
    //Attributes
    @Inject
    UserManager mUserManager;
    @Inject
    UserLogicFactory mUserLogicFactory;
    @Inject
    ChallengeDataSource mChallengeDataSource;

    /**
     * This method is called when the activity is created
     * @param savedInstanceState ignored
     */
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
        if (ab != null)
            ab.setDisplayHomeAsUpEnabled(true);

        User user = mUserManager.getCurrentUser();
        long categoryId = getIntent().getLongExtra(ChallengeActivity.EXTRA_CATEGORY_ID, CategoryDataSource.CATEGORY_ID_ALL);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.statisticsRecycler);
        recyclerView.setHasFixedSize(true);

        final boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        GridLayoutManager layoutManager = new GridLayoutManager(this, isLandscape ? 3 : 2, GridLayoutManager.VERTICAL, false);
        layoutManager.setSpanSizeLookup(
                new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        if (isLandscape) {
                            return (position == 1 || position == 3) ? 1 : 2;
                        } else
                            return position < 2 ? 1 : 2;
                    }
                });

        recyclerView.setLayoutManager(layoutManager);

        StatisticsAdapter adapter = new StatisticsAdapter(mUserLogicFactory, mChallengeDataSource, (BrainPhaserApplication) getApplication(), user, categoryId, isLandscape);
        recyclerView.setAdapter(adapter);

        //adapter.recreateViews();
    }

    /**
     * This method injects the activity to the BrainPhaseComponent object
     * @param component the component the activity is injected to
     */
    @Override
    protected void injectComponent(BrainPhaserComponent component) {
        component.inject(this);
    }
}