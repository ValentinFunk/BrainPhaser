package de.fhdw.ergoholics.brainphaser.activities.Challenge;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import de.fhdw.ergoholics.brainphaser.BrainPhaserComponent;
import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.activities.BrainPhaserActivity;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class ChallengeActivity extends BrainPhaserActivity implements AnswerFragment.AnswerListener {

    public static final String EXTRA_CATEGORY_ID ="KEY_CURRENT_CATEGORY_ID";
    public static final String KEY_CHALLENGE_ID="KEY_CHALLENGE_ID";
    public static final String KEY_ANSWER_CHECKED ="KEY_ANSWER_CHECKED";
    public static final String KEY_ACTIVE_CHALLENGES="KEY_ACTIVE_CHALLENGES";

    @Inject
    UserManager mUserManager;
    @Inject
    CompletionDataSource mCompletionDataSource;
    @Inject
    StatisticsDataSource mStatisticsDataSource;
    @Inject
    ChallengeDataSource mChallengeDataSource;
    @Inject
    UserLogicFactory mUserLogicFactory;
    private List<Long> mAllChallenges;
    private int mChallengeNo = 0;
    private FloatingActionButton mBtnNextChallenge;
    private ProgressBar mProgress;

    private boolean mAnswerChecked;
    private FragmentManager mFManager;
    private FragmentTransaction mFTransaction;
    private DueChallengeLogic mDueChallengeLogic;
    private TextView mQuestionText;
    private Challenge mCurrentChallenge;

    @Override
    protected void injectComponent(BrainPhaserComponent component) {
        component.inject(this);
    }

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
        mBtnNextChallenge = (FloatingActionButton)findViewById(R.id.btnNextChallenge);
        mQuestionText = (TextView) findViewById(R.id.challengeQuestion);
        mProgress = (ProgressBar) findViewById(R.id.progress_bar);

        //FragementManager manages the fragments in the activity
        mFManager=getSupportFragmentManager();

        //Get the CategoryID as an Intent. If no Category is given the id will be -1 (for all categories)
        Intent i = getIntent();
        long categoryId = i.getLongExtra(EXTRA_CATEGORY_ID, -1);

        //Load the user's due challenges
        final User currentUser = mUserManager.getCurrentUser();
        mDueChallengeLogic = mUserLogicFactory.createDueChallengeLogic(currentUser);
        mAllChallenges = mDueChallengeLogic.getDueChallenges(categoryId);
        mAnswerChecked =false;

        //Load the ending screen if no challenges are due
        if (mAllChallenges == null || mAllChallenges.size() < 1) {
            loadFinishScreen();
            return;
        }

        mProgress.setMax(mAllChallenges.size());
        mProgress.setProgress(mChallengeNo);
        loadChallenge(mAllChallenges.get(mChallengeNo));


        mBtnNextChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mAnswerChecked) {//Check the current answer and load the finish screen
                    //Find the current fragment and user
                    AnswerFragment currentFragment = (AnswerFragment) mFManager.findFragmentById(R.id.challenge_fragment);
                    currentFragment.checkAnswers();
                } else {//Load the next challenge
                    //If the challenge is completed load the finish screen
                    if (mChallengeNo == mAllChallenges.size() - 1) {
                        loadFinishScreen();
                        return;
                    }
                    //increment counter
                    mChallengeNo += 1;
                    mProgress.setProgress(mChallengeNo);
                    //load the next challenge
                    loadChallenge(mAllChallenges.get(mChallengeNo));
                    mAnswerChecked = false;
                }
            }
        });

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mChallengeNo=savedInstanceState.getInt(KEY_CHALLENGE_ID);
        mAnswerChecked=savedInstanceState.getBoolean(KEY_ANSWER_CHECKED);
        mAllChallenges=new ArrayList<>();
        ArrayList<Integer> activeChallenges = savedInstanceState.getIntegerArrayList(KEY_ACTIVE_CHALLENGES);
        for (int item:activeChallenges) {
            mAllChallenges.add((long)item);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(KEY_ANSWER_CHECKED, mAnswerChecked);
        savedInstanceState.putInt(KEY_CHALLENGE_ID, mChallengeNo);
        ArrayList<Integer> activeChallenges = new ArrayList<>();
        for (long item: mAllChallenges) {
            activeChallenges.add((int)item);
        }
        savedInstanceState.putIntegerArrayList(KEY_ACTIVE_CHALLENGES, activeChallenges);
        super.onSaveInstanceState(savedInstanceState);
    }

    private void loadFinishScreen(){
        //Load End Screen
        mFTransaction=mFManager.beginTransaction();
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
     * Loads the current challenge depending and its fragment depending on the challenge-type
     * @param challengeId The challenge's id to be loaded
     */
    private void loadChallenge(long challengeId){
        //Bundle to transfer the ChallengeId to the fragments
        Bundle bundle = new Bundle();
        bundle.putLong(KEY_CHALLENGE_ID, challengeId);

        //Start a transaction on the fragments
        mFTransaction=mFManager.beginTransaction();
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
    }

    /**
     * Loads the question of the challenge into the text
     */
    public void changeQuestion() {
        //Set question text
        mQuestionText.setText(mCurrentChallenge.getQuestion());
    }

    @Override
    public void onAnswerChecked(boolean answer) {
        User currentUser = mUserManager.getCurrentUser();
        mAnswerChecked = true;
        if(answer) {
            //Write completion entry
            mCompletionDataSource.updateAfterAnswer(mAllChallenges.get(mChallengeNo), currentUser.getId(), CompletionDataSource.ANSWER_RIGHT);

            //Create statistics entry
            Statistics statistics = new Statistics(null, true, new Date(), currentUser.getId(), mAllChallenges.get(mChallengeNo));
            mStatisticsDataSource.create(statistics);
        }else{
            //Write completion entry
            mCompletionDataSource.updateAfterAnswer(mAllChallenges.get(mChallengeNo), currentUser.getId(), CompletionDataSource.ANSWER_WRONG);

            //Create statistics entry
            Statistics statistics = new Statistics(null, false, new Date(), currentUser.getId(), mAllChallenges.get(mChallengeNo));
            mStatisticsDataSource.create(statistics);
        }
    }
}
