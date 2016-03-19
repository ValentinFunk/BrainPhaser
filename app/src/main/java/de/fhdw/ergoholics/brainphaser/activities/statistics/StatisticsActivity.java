package de.fhdw.ergoholics.brainphaser.activities.statistics;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import de.fhdw.ergoholics.brainphaser.BrainPhaserApplication;
import de.fhdw.ergoholics.brainphaser.BrainPhaserComponent;
import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.activities.BrainPhaserActivity;
import de.fhdw.ergoholics.brainphaser.activities.playchallenge.ChallengeActivity;
import de.fhdw.ergoholics.brainphaser.database.CategoryDataSource;
import de.fhdw.ergoholics.brainphaser.database.ChallengeDataSource;
import de.fhdw.ergoholics.brainphaser.database.CompletionDataSource;
import de.fhdw.ergoholics.brainphaser.database.StatisticsDataSource;
import de.fhdw.ergoholics.brainphaser.logic.UserLogicFactory;
import de.fhdw.ergoholics.brainphaser.logic.UserManager;
import de.fhdw.ergoholics.brainphaser.logic.statistics.ChartDataLogic;
import de.fhdw.ergoholics.brainphaser.logic.statistics.StatisticType;
import de.fhdw.ergoholics.brainphaser.model.User;

/**
 * Created by Daniel Hoogen on 09/03/2016.
 * <p/>
 * This is the activity showing statistics for the user in a specified category
 */
public class StatisticsActivity extends BrainPhaserActivity {
    //Constants
    private static final StatisticType[] TYPES_LANDSCAPE = new StatisticType[]{
            StatisticType.TYPE_MOST_PLAYED,
            StatisticType.TYPE_DUE,
            StatisticType.TYPE_MOST_FAILED,
            StatisticType.TYPE_STAGE,
            StatisticType.TYPE_MOST_SUCCEEDED
    };
    private static final StatisticType[] TYPES_PORTRAIT = new StatisticType[]{
            StatisticType.TYPE_DUE,
            StatisticType.TYPE_STAGE,
            StatisticType.TYPE_MOST_PLAYED,
            StatisticType.TYPE_MOST_FAILED,
            StatisticType.TYPE_MOST_SUCCEEDED
    };

    //Attributes
    @Inject
    UserManager mUserManager;
    @Inject
    UserLogicFactory mUserLogicFactory;
    @Inject
    ChallengeDataSource mChallengeDataSource;
    @Inject
    BrainPhaserApplication mApplication;
    @Inject
    CompletionDataSource mCompletionDataSource;
    @Inject
    StatisticsDataSource mStatisticsDataSource;

    private StatisticsAdapter mAdapter;
    private StatisticType[] mShownTypes;
    private long mCategoryId;
    private boolean mIsLandscape;

    /**
     * This method injects the activity to the BrainPhaseComponent object.
     *
     * @param component the component the activity is injected to
     */
    @Override
    protected void injectComponent(BrainPhaserComponent component) {
        component.inject(this);
    }

    /**
     * This method is called when the activity is created
     *
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
        if (ab != null) ab.setDisplayHomeAsUpEnabled(true);

        User user = mUserManager.getCurrentUser();
        mCategoryId = getIntent().getLongExtra(ChallengeActivity.EXTRA_CATEGORY_ID,
                CategoryDataSource.CATEGORY_ID_ALL);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.statisticsRecycler);
        recyclerView.setHasFixedSize(true);

        int deviceOrientation = getResources().getConfiguration().orientation;

        mIsLandscape = (deviceOrientation == Configuration.ORIENTATION_LANDSCAPE);
        GridLayoutManager layoutManager = new GridLayoutManager(this, mIsLandscape ? 3 : 2,
                GridLayoutManager.VERTICAL, false);
        layoutManager.setSpanSizeLookup(new StatisticsSpanSizeLookup(mIsLandscape));

        recyclerView.setLayoutManager(layoutManager);

        //Look up which statistics can be shown
        setShownTypes();

        //Add adapter
        mAdapter = new StatisticsAdapter(mUserLogicFactory, mChallengeDataSource,
                (BrainPhaserApplication) getApplication(), user, mCategoryId, mShownTypes);

        recyclerView.setAdapter(mAdapter);
    }

    /**
     * This method looks up which charts can be created and sets the member shown types to an array
     * of the types of these charts
     */
    private void setShownTypes() {
        StatisticType[] types = mIsLandscape ? TYPES_LANDSCAPE : TYPES_PORTRAIT;

        ChartDataLogic chartDataLogic = new ChartDataLogic(mUserManager.getCurrentUser(),
                mCategoryId, mApplication, mChallengeDataSource, mCompletionDataSource,
                mStatisticsDataSource, mUserLogicFactory);

        ArrayList<StatisticType> typesToAdd = new ArrayList<>();
        for (StatisticType type : types) {
            switch (type) {
                case TYPE_DUE:
                    if (chartDataLogic.findDueData() != null) {
                        typesToAdd.add(type);
                    }
                    break;
                case TYPE_STAGE:
                    if (chartDataLogic.findStageData() != null) {
                        typesToAdd.add(type);
                    }
                    break;
                default:
                    if (chartDataLogic.findMostPlayedData(type, new ArrayList<Long>()) != null) {
                        typesToAdd.add(type);
                    }
                    break;
            }
        }

        mShownTypes = getTypeArray(typesToAdd);
    }

    /**
     * This method is called when the activity is started. It resets the shown item views.
     */
    @Override
    protected void onStart() {
        super.onStart();

        setShownTypes();
        mAdapter.setStatisticItems(mShownTypes);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * This method is called when the activity is resumed. It resets the shown item views.
     */
    @Override
    protected void onResume() {
        super.onResume();

        setShownTypes();
        mAdapter.setStatisticItems(mShownTypes);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * This method converts a given list of StatisticType objects to an array and returns it
     *
     * @param list the list that will be converted
     * @return the array containing the list's StatisticType objects
     */
    private StatisticType[] getTypeArray(List<StatisticType> list) {
        StatisticType[] finalTypes = new StatisticType[list.size()];

        for (int i = 0; i < finalTypes.length; i++) finalTypes[i] = list.get(i);

        return finalTypes;
    }
}