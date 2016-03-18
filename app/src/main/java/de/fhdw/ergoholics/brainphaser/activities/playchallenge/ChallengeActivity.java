package de.fhdw.ergoholics.brainphaser.activities.playchallenge;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import de.fhdw.ergoholics.brainphaser.BrainPhaserComponent;
import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.activities.BrainPhaserActivity;
import de.fhdw.ergoholics.brainphaser.activities.playchallenge.multiplechoice.MultipleChoiceFragment;
import de.fhdw.ergoholics.brainphaser.database.CategoryDataSource;
import de.fhdw.ergoholics.brainphaser.database.ChallengeDataSource;
import de.fhdw.ergoholics.brainphaser.database.ChallengeType;
import de.fhdw.ergoholics.brainphaser.database.CompletionDataSource;
import de.fhdw.ergoholics.brainphaser.database.StatisticsDataSource;
import de.fhdw.ergoholics.brainphaser.logic.DueChallengeLogic;
import de.fhdw.ergoholics.brainphaser.logic.UserLogicFactory;
import de.fhdw.ergoholics.brainphaser.logic.UserManager;
import de.fhdw.ergoholics.brainphaser.model.Challenge;
import de.fhdw.ergoholics.brainphaser.model.Statistics;
import de.fhdw.ergoholics.brainphaser.model.User;

/**
 * Created by Christian Kost
 * Activity used to handle challenges. Loads fragments depending on the challenge type. Implements the interface to determine when an challenge was checked and when a self check challenge was checked.
 */
public class ChallengeActivity extends BrainPhaserActivity implements AnswerFragment.AnswerListener, SelfTestDialogFragment.SelfCheckAnswerListener {

    //Strings used for Bundles
    public static final String EXTRA_CATEGORY_ID = "KEY_CURRENT_CATEGORY_ID";
    public static final String KEY_CHALLENGE_ID = "KEY_CHALLENGE_ID";
    public static final String KEY_ANSWER_CHECKED = "KEY_ANSWER_CHECKED";
    public static final String KEY_ACTIVE_CHALLENGES = "KEY_ACTIVE_CHALLENGES";

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

    private DueChallengeLogic mDueChallengeLogic;
    //current state stuff
    private List<Long> mAllChallenges;
    private int mChallengeNo = 0;
    private boolean mAnswerChecked;

    private Challenge mCurrentChallenge;

    //fragment stuff
    private FragmentManager mFManager;
    private FragmentTransaction mFTransaction;

    //View Stuff
    private FloatingActionButton mBtnNextChallenge;
    private TextView mQuestionText;
    private ProgressBar mProgress;
    private TextView mCategoryText;
    private TextView mTypeText;
    private TextView mClassText;

    /**
     * Inject components
     *
     * @param component BrainPhaserComponent
     */
    @Override
    protected void injectComponent(BrainPhaserComponent component) {
        component.inject(this);
    }

    /**
     * Setup the activity. Instantiates the views with its listeners. Loads the challenge afterwards. If no challenges are due the function loads the finish screen.
     *
     * @param savedInstanceState Ignored
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);

        Toolbar myChildToolbar =
                (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myChildToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        //get the views
        mBtnNextChallenge = (FloatingActionButton) findViewById(R.id.btnNextChallenge);
        mQuestionText = (TextView) findViewById(R.id.challengeQuestion);
        mProgress = (ProgressBar) findViewById(R.id.progress_bar);
        mCategoryText = (TextView) findViewById(R.id.challenge_category);
        mTypeText = (TextView) findViewById(R.id.challenge_type);
        mClassText = (TextView) findViewById(R.id.challenge_class);

        //FragementManager manages the fragments in the activity
        mFManager = getSupportFragmentManager();

        //Get the CategoryID as an Intent. If no Category is given the id will be -1 (for all categories)
        Intent i = getIntent();
        long categoryId = i.getLongExtra(EXTRA_CATEGORY_ID, -1);

        //Load the user's due challenges
        final User currentUser = mUserManager.getCurrentUser();
        mDueChallengeLogic = mUserLogicFactory.createDueChallengeLogic(currentUser);
        mAllChallenges = mDueChallengeLogic.getDueChallenges(categoryId);
        mAnswerChecked = false;

        //Load the ending screen if no challenges are due
        if (mAllChallenges == null || mAllChallenges.size() < 1) {
            loadFinishScreen();
            return;
        }

        //setup progressbar
        mProgress.setMax(mAllChallenges.size());
        mProgress.setProgress(mChallengeNo);
        //load the challenge
        loadChallenge(mAllChallenges.get(mChallengeNo));


        mBtnNextChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            /**
             * Check the current answer and load the finish screen or the next challenge
             */
            public void onClick(View view) {
                if (!mAnswerChecked) {
                    //Find the current fragment and user
                    AnswerFragment currentFragment = (AnswerFragment) mFManager.findFragmentById(R.id.challenge_fragment);
                    //checks the answer
                    currentFragment.checkAnswers();
                } else {
                    //loads the next screen (challenge or finish screen)
                    loadNextScreen();
                }
            }
        });

    }

    /**
     * Loads the next screen (next challenge or the finish screen)
     */
    private void loadNextScreen() {
        //If the all challenge are completed load the finish screen
        if (mChallengeNo == mAllChallenges.size() - 1) {
            loadFinishScreen();
            return;
        }
        //increment counter
        mChallengeNo += 1;
        //set the progress in the progessbar
        mProgress.setProgress(mChallengeNo);
        //load the next challenge
        loadChallenge(mAllChallenges.get(mChallengeNo));
        mAnswerChecked = false;
    }

    /**
     * Restores the current state (is answer was checked, current challenge no, due challenges)
     *
     * @param savedInstanceState Current state of the due challenges, the current challenge and if the challenge is done
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mChallengeNo = savedInstanceState.getInt(KEY_CHALLENGE_ID);
        mAnswerChecked = savedInstanceState.getBoolean(KEY_ANSWER_CHECKED);
        mAllChallenges = new ArrayList<>();
        ArrayList<Integer> activeChallenges = savedInstanceState.getIntegerArrayList(KEY_ACTIVE_CHALLENGES);
        for (int item : activeChallenges) {
            mAllChallenges.add((long) item);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * Saves the current state (is answer was checked, current challenge no, due challenges)
     *
     * @param savedInstanceState Current state of the due challenges, the current challenge and if the challenge is done
     */
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(KEY_ANSWER_CHECKED, mAnswerChecked);
        savedInstanceState.putInt(KEY_CHALLENGE_ID, mChallengeNo);
        ArrayList<Integer> activeChallenges = new ArrayList<>();
        for (long item : mAllChallenges) {
            activeChallenges.add((int) item);
        }
        savedInstanceState.putIntegerArrayList(KEY_ACTIVE_CHALLENGES, activeChallenges);
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Loads the finish screen and unloads all other screens
     */
    private void loadFinishScreen() {
        //Load End Screen
        mFTransaction = mFManager.beginTransaction();
        mFTransaction.disallowAddToBackStack();

        ((LinearLayout) findViewById(R.id.challenge_layout)).removeAllViews();

        //Create the finish-challenge
        FinishChallengeFragment finishChallengeFragment = new FinishChallengeFragment();

        //Inflate the finish-challenge in the question_fragment
        mFTransaction.replace(R.id.challenge_layout, finishChallengeFragment);

        //Commit the changes
        mFTransaction.commit();
        mFManager.executePendingTransactions();
        mBtnNextChallenge.setVisibility(View.INVISIBLE);
    }

    /**
     * Loads the current challenge into its fragment depending on the challenge-type
     *
     * @param challengeId The challenge's id to be loaded
     */
    private void loadChallenge(long challengeId) {
        //Bundle to transfer the ChallengeId to the fragments
        Bundle bundle = new Bundle();
        bundle.putLong(KEY_CHALLENGE_ID, challengeId);

        //Start a transaction on the fragments
        mFTransaction = mFManager.beginTransaction();
        mFTransaction.disallowAddToBackStack();

        mCurrentChallenge = mChallengeDataSource.getById(challengeId);
        changeQuestion();

        switch (mCurrentChallenge.getChallengeType()) {
            case ChallengeType.MULTIPLE_CHOICE:
                //Create a MultipleChoiceFragment
                MultipleChoiceFragment multipleChoiceFragment = new MultipleChoiceFragment();
                //Commit the bundle
                multipleChoiceFragment.setArguments(bundle);
                //Inflate the MultipleChoiceFragment in the challenge_fragment
                mFTransaction.replace(R.id.challenge_fragment, multipleChoiceFragment);
                break;
            case ChallengeType.TEXT:
                //Create a TextFragment
                TextFragment textFragment = new TextFragment();
                //Commit the bundle
                textFragment.setArguments(bundle);
                //Inflate the TextFragment in the challenge_fragment
                mFTransaction.replace(R.id.challenge_fragment, textFragment);
                break;
            case ChallengeType.SELF_TEST:
                //Create a SelfCheckFragment
                SelfTestFragment selfTestFragment = new SelfTestFragment();
                selfTestFragment.setArguments(bundle);
                mFTransaction.replace(R.id.challenge_fragment, selfTestFragment);
                break;
        }
        //Commit the changes
        mFTransaction.commit();
        mFManager.executePendingTransactions();
        initializeMetaData();
    }

    /**
     * Loads the question of the challenge into the text
     */
    public void changeQuestion() {
        //Set question text
        mQuestionText.setText(mCurrentChallenge.getQuestion());
    }

    /**
     * After the answer was checked save the answer depending on whether the answer is right or wrong
     *
     * @param answer Answer right or wrong
     */
    @Override
    public void onAnswerChecked(boolean answer) {
        User currentUser = mUserManager.getCurrentUser();
        //The current Challenge was checked
        mAnswerChecked = true;

        // Save the user completion for due calculation
        mCompletionDataSource.updateAfterAnswer(mAllChallenges.get(mChallengeNo), currentUser.getId(), answer ? CompletionDataSource.ANSWER_RIGHT : CompletionDataSource.ANSWER_WRONG);

        //Create statistics entry
        Statistics statistics = new Statistics(null, answer, new Date(), currentUser.getId(), mAllChallenges.get(mChallengeNo));
        mStatisticsDataSource.create(statistics);
    }

    /**
     * Loads the current challenge's metadata into texts
     */
    private void initializeMetaData() {
        String category = mCategoryDataSource.getById(mCurrentChallenge.getCategoryId()).getTitle();
        mCategoryText.setText(category);
        int stageColor;
        long stage = mCompletionDataSource.findByChallengeAndUser(mCurrentChallenge.getId(), mUserManager.getCurrentUser().getId()).getStage();

        switch ((int) stage) {
            case 2:
                stageColor = ContextCompat.getColor(this.getBaseContext(), R.color.colorStage2);
                break;
            case 3:
                stageColor = ContextCompat.getColor(this.getBaseContext(), R.color.colorStage3);
                break;
            case 4:
                stageColor = ContextCompat.getColor(this.getBaseContext(), R.color.colorStage4);
                break;
            case 5:
                stageColor = ContextCompat.getColor(this.getBaseContext(), R.color.colorStage5);
                break;
            case 6:
                stageColor = ContextCompat.getColor(this.getBaseContext(), R.color.colorStage6);
                break;
            default:
                stage = 1;
                stageColor = ContextCompat.getColor(this.getBaseContext(), R.color.colorStage1);
                break;
        }
        Drawable classBackground = mClassText.getBackground();
        classBackground.setColorFilter(stageColor, PorterDuff.Mode.MULTIPLY);
        mClassText.setText("Klasse " + stage);

        String type;
        int typeColor;
        switch (mCurrentChallenge.getChallengeType()) {
            case ChallengeType.TEXT:
                typeColor = ContextCompat.getColor(this.getBaseContext(), R.color.colorText);
                type = "Text";
                break;
            case ChallengeType.MULTIPLE_CHOICE:
                typeColor = ContextCompat.getColor(this.getBaseContext(), R.color.colorMultipleChoice);
                type = "Multiple-Choice";
                break;
            case ChallengeType.SELF_TEST:
                typeColor = ContextCompat.getColor(this.getBaseContext(), R.color.colorSelfCheck);
                type = "Selbst-Check";
                break;
            default:
                typeColor = ContextCompat.getColor(this.getBaseContext(), R.color.colorPrimary);
                type = "";
                break;
        }
        Drawable typeBackground = mTypeText.getBackground();
        typeBackground.setColorFilter(typeColor, PorterDuff.Mode.MULTIPLY);
        mTypeText.setText(type);
    /**
     * Loads the finish screen and unloads all other screens
     */
    private void loadFinishScreen() {
        // Remove anchor by resetting layout params since anchor element has been removed from the screen
        CoordinatorLayout.LayoutParams lp = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.WRAP_CONTENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT);
        mFloatingActionButton.setLayoutParams(lp);
        mFloatingActionButton.setVisibility(View.INVISIBLE);

        LinearLayout contentLayout = (LinearLayout) findViewById(R.id.challenge_layout);
        contentLayout.removeAllViews();

        View view = getLayoutInflater().inflate(R.layout.fragment_finish_challenge, contentLayout, false);
        contentLayout.addView(view);
    }

    /**
     * Load the next screen after the self challenge answer was checked
     */
    @Override
    public void onSelfCheckAnswerChecked() {
        loadNextScreen();
    }
}