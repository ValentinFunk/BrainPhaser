package de.fhdw.ergoholics.brainphaser.activities.Challenge;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.fhdw.ergoholics.brainphaser.BrainPhaserComponent;
import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.activities.BrainPhaserActivity;
import de.fhdw.ergoholics.brainphaser.database.ChallengeDataSource;
import de.fhdw.ergoholics.brainphaser.database.ChallengeType;
import de.fhdw.ergoholics.brainphaser.database.CompletionDataSource;
import de.fhdw.ergoholics.brainphaser.logic.DueChallengeLogic;
import de.fhdw.ergoholics.brainphaser.logic.UserLogicFactory;
import de.fhdw.ergoholics.brainphaser.logic.UserManager;
import de.fhdw.ergoholics.brainphaser.model.Challenge;
import de.fhdw.ergoholics.brainphaser.model.User;

import java.util.List;

import javax.inject.Inject;

public class ChallengeActivity extends BrainPhaserActivity {

    public static final String EXTRA_CATEGORY_ID ="KEY_CURRENT_CATEGORY_ID";
    public static final String KEY_CHALLENGE_ID="KEY_CHALLENGE_ID";
    @Inject
    UserManager mUserManager;
    @Inject
    CompletionDataSource mCompletionDataSource;
    @Inject
    ChallengeDataSource mChallengeDataSource;
    @Inject
    UserLogicFactory mUserLogicFactory;
    private int mChallengeNo = 0;
    private Button mBtnNextChallenge;
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

        //get the button
        mBtnNextChallenge = (Button) findViewById(R.id.btnNextChallenge);
        mQuestionText = (TextView) findViewById(R.id.challengeQuestion);

        //FragementManager manages the fragments in the activity
        mFManager=getSupportFragmentManager();

        Intent i = getIntent();
        long categoryId= i.getLongExtra(EXTRA_CATEGORY_ID,-1);

        final User currentUser = mUserManager.getCurrentUser();
        mDueChallengeLogic = mUserLogicFactory.createDueChallengeLogic(currentUser);
        final List<Long> allChallenges = mDueChallengeLogic.getDueChallenges(categoryId);
        if (true || allChallenges == null || allChallenges.size() < 1) {
            loadFinishScreen();
            return;
        }

        loadChallenge(allChallenges.get(mChallengeNo));
        mAnswerChecked =false;

        mBtnNextChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mAnswerChecked) {//Check the current answer and load the finish screen
                    //Find the current fragment and user
                    AnswerFragment currentFragment = (AnswerFragment) mFManager.findFragmentById(R.id.challenge_fragment);

                    User currentUser = mUserManager.getCurrentUser();

                    //Check if the answer is right
                    if (currentFragment.checkAnswers()) {
                        mCompletionDataSource.updateAfterAnswer(allChallenges.get(mChallengeNo), currentUser.getId(), 1);
                    } else {
                        mCompletionDataSource.updateAfterAnswer(allChallenges.get(mChallengeNo), currentUser.getId(), -1);
                    }

                    //Activate next challenge
                    mBtnNextChallenge.setText(getResources().getString(R.string.next_Challenge));
                    mAnswerChecked =true;
                    //If the challenge is completed load the finish screen
                    if(mChallengeNo==allChallenges.size()-1){
                        loadFinishScreen();
                    }
                }else{//Load the next challenge
                    //increment counter
                    mChallengeNo += 1;
                    //load the next challenge
                    loadChallenge(allChallenges.get(mChallengeNo));
                    //reset values
                    mBtnNextChallenge.setText(getResources().getString(R.string.check_Challenge));
                    mAnswerChecked = false;
                }
            }
        });

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
}