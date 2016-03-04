package de.fhdw.ergoholics.brainphaser.activities.Challenge;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import de.fhdw.ergoholics.brainphaser.BrainPhaserApplication;
import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.activities.CategorySelect.SelectCategoryPage;
import de.fhdw.ergoholics.brainphaser.activities.main.MainActivity;
import de.fhdw.ergoholics.brainphaser.activities.main.Navigation;
import de.fhdw.ergoholics.brainphaser.database.CategoryDataSource;
import de.fhdw.ergoholics.brainphaser.database.ChallengeDataSource;
import de.fhdw.ergoholics.brainphaser.database.ChallengeType;
import de.fhdw.ergoholics.brainphaser.database.CompletionDataSource;
import de.fhdw.ergoholics.brainphaser.logic.DueChallengeLogic;
import de.fhdw.ergoholics.brainphaser.model.Challenge;
import de.fhdw.ergoholics.brainphaser.model.User;

import java.util.List;

public class ChallengeActivity extends AppCompatActivity{

    public static final String EXTRA_CATEGORY_ID ="KEY_CURRENT_CATEGORY_ID";
    public static final String KEY_CHALLENGE_ID="KEY_CHALLENGE_ID";
    private int mChallengeNo = 0;
    private boolean mAnswerChecked;
    private boolean mChallengeDone=false;
    private FragmentManager mFManager;
    private FragmentTransaction mFTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);

        Toolbar myChildToolbar =
            (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myChildToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);


        //FragementManager manages the fragments in the activity
        mFManager=getSupportFragmentManager();

        Intent i = getIntent();
        long categoryId= i.getLongExtra(EXTRA_CATEGORY_ID, CategoryDataSource.CATEGORY_ID_ALL);

        BrainPhaserApplication app = (BrainPhaserApplication)getApplication();
        final User currentUser = app.getCurrentUser();
        final List<Long> allChallenges = DueChallengeLogic.getDueChallenges(currentUser, categoryId);

        loadChallenge(allChallenges.get(mChallengeNo));
        mAnswerChecked =false;

        final Button btnNextChallenge = (Button)findViewById(R.id.btnNextChallenge);
        btnNextChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mChallengeDone){
                    Intent intent = new Intent(getApplicationContext(), SelectCategoryPage.class);
                    intent.putExtra(MainActivity.EXTRA_NAVIGATE_TO, Navigation.NavigationState.NAV_LEARN);
                    startActivity(intent);

                    setResult(Activity.RESULT_OK);
                    finish();
                    return;
                }

                if(!mAnswerChecked) {
                    AnswerFragment currentFragment =(AnswerFragment) mFManager.findFragmentById(R.id.challenge_fragment);
                    BrainPhaserApplication app = (BrainPhaserApplication)getApplication();
                    User currentUser = app.getCurrentUser();

                    if (currentFragment.checkAnswers()) {
                        CompletionDataSource.updateAfterAnswer(allChallenges.get(mChallengeNo), currentUser.getId(), 1);
                    } else {
                        CompletionDataSource.updateAfterAnswer(allChallenges.get(mChallengeNo), currentUser.getId(), -1);
                    }

                    btnNextChallenge.setText(getResources().getString(R.string.next_Challenge));
                    mAnswerChecked =true;
                    if(mChallengeNo>=allChallenges.size()){
                        //Load End Screen
                        mChallengeDone=true;
                        btnNextChallenge.setText(getResources().getString(R.string.end_Challenge));
                    }
                }else{
                    mChallengeNo += 1;
                    loadChallenge(allChallenges.get(mChallengeNo));
                    btnNextChallenge.setText(getResources().getString(R.string.check_Challenge));
                    mAnswerChecked = false;
                }
            }
        });

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
        //Create the QuestionFragment
        QuestionFragment questionFragment =new QuestionFragment();
        //Commit the bundle
        questionFragment.setArguments(bundle);
        //Inflate the QuestionFragment in the question_fragment
        mFTransaction.replace(R.id.challenge_fragment_question, questionFragment);

        Challenge currentChallenge = ChallengeDataSource.getById(challengeId);

        switch (currentChallenge.getChallengeType()){
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
}