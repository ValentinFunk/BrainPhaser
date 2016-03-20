package de.fhdw.ergoholics.brainphaser.activities.playchallenge;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import javax.inject.Inject;

import de.fhdw.ergoholics.brainphaser.BrainPhaserComponent;
import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.activities.BrainPhaserActivity;
import de.fhdw.ergoholics.brainphaser.database.CategoryDataSource;
import de.fhdw.ergoholics.brainphaser.database.ChallengeDataSource;
import de.fhdw.ergoholics.brainphaser.database.ChallengeStage;
import de.fhdw.ergoholics.brainphaser.database.ChallengeType;
import de.fhdw.ergoholics.brainphaser.database.CompletionDataSource;
import de.fhdw.ergoholics.brainphaser.database.StatisticsDataSource;
import de.fhdw.ergoholics.brainphaser.logic.CompletionLogic;
import de.fhdw.ergoholics.brainphaser.logic.DueChallengeLogic;
import de.fhdw.ergoholics.brainphaser.logic.UserLogicFactory;
import de.fhdw.ergoholics.brainphaser.logic.UserManager;
import de.fhdw.ergoholics.brainphaser.model.Challenge;
import de.fhdw.ergoholics.brainphaser.model.Statistics;
import de.fhdw.ergoholics.brainphaser.model.User;

/**
 * Created by Christian Kost
 * <p/>
 * Activity used to handle challenges. Loads fragments depending on the
 * challenge type. Implements the interface to determine when an challenge was checked and when a
 * self check challenge was checked.
 */
public class ChallengeActivity extends BrainPhaserActivity implements AnswerFragment.AnswerListener {
    //Strings used for Bundles
    public static final String EXTRA_CATEGORY_ID = "KEY_CURRENT_CATEGORY_ID";

    public static final String KEY_CHALLENGE_ID = "KEY_CHALLENGE_ID";
    public static final String KEY_NEXT_ON_FAB = "KEY_NEXT_ON_FAB";
    public static final String KEY_ALL_DUE_CHALLENGES = "KEY_ALL_DUE_CHALLENGES";

    @Inject
    UserManager mUserManager;
    @Inject
    CompletionDataSource mCompletionDataSource;
    @Inject
    StatisticsDataSource mStatisticsDataSource;
    @Inject
    ChallengeDataSource mChallengeDataSource;
    @Inject
    CategoryDataSource mCategoryDataSource;
    @Inject
    UserLogicFactory mUserLogicFactory;
    @Inject
    AnswerFragmentFactory mAnswerFragmentFactory;
    @Inject
    CompletionLogic mCompletionLogic;

    private ArrayList<Long> mAllDueChallenges;

    //current state stuff
    private int mChallengeNo = 0;
    private Challenge mCurrentChallenge;
    private boolean mLoadNextChallengeOnFabClick = false;

    //View Stuff
    private FloatingActionButton mFloatingActionButton;
    private TextView mQuestionText;
    private ProgressBar mProgress;

    private TextView mCategoryText;
    private TextView mTypeText;
    private TextView mClassText;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void injectComponent(BrainPhaserComponent component) {
        component.inject(this);
    }

    /**
     * Setup the activity. Instantiates the views with its listeners. Loads the challenge
     * afterwards. If no challenges are due the function loads the finish screen.
     *
     * @param savedInstanceState Current state of the due challenges, the current challenge and if
     *                           the challenge is done
     */
    @Override
    @SuppressWarnings("unchecked")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Content View
        setContentView(R.layout.activity_challenge);

        // Set up actionbar
        Toolbar myChildToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myChildToolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        //get the views
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.btnNextChallenge);
        mQuestionText = (TextView) findViewById(R.id.challengeQuestion);
        mProgress = (ProgressBar) findViewById(R.id.progress_bar);
        mCategoryText = (TextView) findViewById(R.id.challenge_category);
        mTypeText = (TextView) findViewById(R.id.challenge_type);
        mClassText = (TextView) findViewById(R.id.challenge_class);

        //Get the CategoryID from the intent. If no Category is given the id will be -1 (for all categories)
        Intent i = getIntent();
        long categoryId = i.getLongExtra(EXTRA_CATEGORY_ID, -1);

        //Load the user's due challenges for the selected category
        if (savedInstanceState != null) {
            mAllDueChallenges = (ArrayList<Long>) savedInstanceState.getSerializable(KEY_ALL_DUE_CHALLENGES);
            mLoadNextChallengeOnFabClick = savedInstanceState.getBoolean(KEY_NEXT_ON_FAB);
            mChallengeNo = savedInstanceState.getInt(KEY_CHALLENGE_ID, 0);
        } else {
            final User currentUser = mUserManager.getCurrentUser();
            DueChallengeLogic dueChallengeLogic = mUserLogicFactory.createDueChallengeLogic(currentUser);
            mAllDueChallenges = new ArrayList<>(dueChallengeLogic.getDueChallenges(categoryId));
            Collections.shuffle(mAllDueChallenges);
        }

        //Load the empty state screen if no challenges are due
        if (mAllDueChallenges == null || mAllDueChallenges.size() < 1) {
            loadFinishScreen();
            return;
        }

        //load the challenge if no saved instance state, else subviews are responsible for loading state
        mCurrentChallenge = mChallengeDataSource.getById(mAllDueChallenges.get(mChallengeNo));
        if (savedInstanceState == null) {
            loadChallenge();
        } else {
            initializeMetaData();
            mQuestionText.setText(mCurrentChallenge.getQuestion());
        }

        //setup progressbar
        mProgress.setMax(mAllDueChallenges.size() * 100);
        mProgress.setProgress(mChallengeNo * 100);

        // Setup Floating Action Button
        if (mLoadNextChallengeOnFabClick) {
            mFloatingActionButton.setImageResource(R.drawable.ic_navigate_next_white_24dp);
        } else {
            mFloatingActionButton.setImageResource(R.drawable.ic_check_white_24dp);
        }

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                floatingActionButtonClicked();
            }
        });
    }

    /**
     * Loads the current challenge into its fragment depending on the challenge-type
     */
    private void loadChallenge() {
        //Bundle to transfer the ChallengeId to the fragments
        Bundle fragmentArguments = new Bundle();
        fragmentArguments.putLong(KEY_CHALLENGE_ID, mCurrentChallenge.getId());

        //Start a transaction on the fragments
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.disallowAddToBackStack();

        mQuestionText.setText(mCurrentChallenge.getQuestion());

        AnswerFragment fragment = mAnswerFragmentFactory.createFragmentForType(mCurrentChallenge.getChallengeType());
        fragment.setArguments(fragmentArguments);
        transaction.replace(R.id.challenge_fragment, fragment, "" + mCurrentChallenge.getChallengeType());

        //Commit the changes
        transaction.commit();
        getSupportFragmentManager().executePendingTransactions();

        // Load new meta data
        initializeMetaData();
    }

    /**
     * Loads the current challenge's metadata into texts
     */
    private void initializeMetaData() {
        String category = mCategoryDataSource.getById(mCurrentChallenge.getCategoryId()).getTitle();
        mCategoryText.setText(category);

        // Set background color and text for the current stage
        int stage = mCompletionDataSource.findByChallengeAndUser(mCurrentChallenge.getId(), mUserManager.getCurrentUser().getId()).getStage();
        int stageColor = ContextCompat.getColor(getBaseContext(), ChallengeStage.getColorResource(stage));
        Drawable classBackground = mClassText.getBackground();
        classBackground.setColorFilter(stageColor, PorterDuff.Mode.MULTIPLY);
        mClassText.setText(getString(R.string.stage_num, stage));

        // Set background color and text for the current challenge type
        int challengeType = mCurrentChallenge.getChallengeType();
        int challengeTypeColor = ContextCompat.getColor(getBaseContext(), ChallengeType.getColorResource(challengeType));
        Drawable typeBackground = mTypeText.getBackground();
        typeBackground.setColorFilter(challengeTypeColor, PorterDuff.Mode.MULTIPLY);
        mTypeText.setText(ChallengeType.getNameResource(challengeType));
    }

    /**
     * On Click for mFloatingActionButton
     * <p/>
     * Delegate to Fragment or load the next question
     */
    private void floatingActionButtonClicked() {
        if (mLoadNextChallengeOnFabClick) {
            loadNextScreen();
            mLoadNextChallengeOnFabClick = false;
            return;
        }

        // Find the current fragment
        AnswerFragment currentFragment = (AnswerFragment) getSupportFragmentManager().findFragmentById(R.id.challenge_fragment);

        // Instruct the Fragment to begin answer checking
        AnswerFragment.ContinueMode continueMode = currentFragment.goToNextState();
        switch (continueMode) {
            case CONTINUE_HIDE_FAB:
                mFloatingActionButton.setVisibility(View.GONE);
                break;
            case CONTINUE_SHOW_FAB:
                mLoadNextChallengeOnFabClick = true;
                mFloatingActionButton.setImageResource(R.drawable.ic_navigate_next_white_24dp);
                break;
            case CONTINUE_ABORT:
                break;
        }
    }

    /**
     * After the answer was checked save the answer depending on whether the answer is right or
     * wrong
     *
     * @param answer Answer right or wrong
     */
    @Override
    public void onAnswerChecked(boolean answer, boolean switchToNext) {
        User currentUser = mUserManager.getCurrentUser();

        // Save the user completion for due calculation
        mCompletionLogic.updateAfterAnswer(mAllDueChallenges.get(mChallengeNo), currentUser.getId(), answer ? CompletionLogic.ANSWER_RIGHT : CompletionLogic.ANSWER_WRONG);

        //Create statistics entry
        Statistics statistics = new Statistics(null, answer, new Date(), currentUser.getId(), mAllDueChallenges.get(mChallengeNo));
        mStatisticsDataSource.create(statistics);

        if (switchToNext) {
            loadNextScreen();
        }
    }

    /**
     * Loads the next screen (next challenge or the finish screen)
     */
    private void loadNextScreen() {
        //If the all challenge are completed load the finish screen
        if (mChallengeNo == mAllDueChallenges.size() - 1) {
            loadFinishScreen();
            return;
        }

        //increment counter
        mChallengeNo += 1;
        mCurrentChallenge = mChallengeDataSource.getById(mAllDueChallenges.get(mChallengeNo));

        //set the progress in the progessbar
        ObjectAnimator animation = ObjectAnimator.ofInt(mProgress, "progress", mChallengeNo * 100);
        animation.setDuration(300); // 0.3 seconds
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();

        //load the next challenge
        loadChallenge();

        mFloatingActionButton.setVisibility(View.VISIBLE);
        mFloatingActionButton.setImageResource(R.drawable.ic_check_white_24dp);
    }

    /**
     * Loads the finish screen and unloads all other screens
     */
    private void loadFinishScreen() {
        // Remove anchor by resetting layout params since anchor element has been removed from the screen
        CoordinatorLayout.LayoutParams lp = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.WRAP_CONTENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT);
        mFloatingActionButton.setLayoutParams(lp);
        mFloatingActionButton.setVisibility(View.INVISIBLE);

        NestedScrollView contentLayout = (NestedScrollView) findViewById(R.id.challenge_rootcontainer);
        if (contentLayout != null) {
            contentLayout.removeAllViews();
            View view = getLayoutInflater().inflate(R.layout.fragment_finish_challenge, contentLayout, false);
            contentLayout.addView(view);
        }
    }

    /**
     * Saves the current state (is answer was checked, current challenge no, due challenges)
     *
     * @param savedInstanceState Current state of the due challenges, the current challenge and if
     *                           the challenge is done
     */
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putInt(KEY_CHALLENGE_ID, mChallengeNo);
        savedInstanceState.putBoolean(KEY_NEXT_ON_FAB, mLoadNextChallengeOnFabClick);
        savedInstanceState.putSerializable(KEY_ALL_DUE_CHALLENGES, mAllDueChallenges);
    }
}