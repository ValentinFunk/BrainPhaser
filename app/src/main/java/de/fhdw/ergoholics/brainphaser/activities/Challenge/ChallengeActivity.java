package de.fhdw.ergoholics.brainphaser.activities.Challenge;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import java.util.ArrayList;

import de.fhdw.ergoholics.brainphaser.BrainPhaserApplication;
import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.activities.CategorySelect.SelectCategoryActivity;
import de.fhdw.ergoholics.brainphaser.database.ChallengeDataSource;
import de.fhdw.ergoholics.brainphaser.database.CompletionDataSource;
import de.fhdw.ergoholics.brainphaser.model.User;

public class ChallengeActivity extends AppCompatActivity{

    public static final String KEY_CHALLENGE_ID="KEY_CHALLENGE_ID";
    private int mChallengeNo = 1;
    private boolean mAnswerChecked;
    private boolean mChallengeDone=false;
    private FragmentManager mFManager;
    private FragmentTransaction mFTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);

        //FragementManager manges the fragments in the activity
        mFManager=getFragmentManager();

        loadChallenge(mChallengeNo);
        mAnswerChecked =false;

        //TODO: Uebergeben
        final ArrayList<Long> allChallenges = new ArrayList<Long>();
        allChallenges.add((long)1);
        allChallenges.add((long)2);


        final Button btnNextChallenge = (Button)findViewById(R.id.btnNextChallenge);
        btnNextChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mChallengeDone){
                    startActivity(new Intent(getApplicationContext(), SelectCategoryActivity.class));
                    setResult(Activity.RESULT_OK);
                    finish();
                    return;
                }

                if(mAnswerChecked ==false) {
                    //TODO Ändern, sodass überprüfen immer möglich ist (Text)
                    MultipleChoiceFragment multipleChoiceFragment = (MultipleChoiceFragment) mFManager.findFragmentById(R.id.challenge_fragment);

                    BrainPhaserApplication app = (BrainPhaserApplication)getApplication();
                    User currentUser = app.getCurrentUser();
                    currentUser=new User((long)1,"Adolf","anonymous");
                    if (multipleChoiceFragment.getCheckedAnswersRight()) {
                        CompletionDataSource.updateAfterAnswer(allChallenges.get(mChallengeNo - 1), currentUser.getId(), 1);
                    } else {
                        CompletionDataSource.updateAfterAnswer(allChallenges.get(mChallengeNo - 1), currentUser.getId(), -1);
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
                    loadChallenge(allChallenges.get(mChallengeNo-1));
                    btnNextChallenge.setText(getResources().getString(R.string.check_Challenge));
                    mAnswerChecked = false;
                }
            }
        });

    }

    /**
     *
     * @param challengeId The challenge's id to be solved
     */
    private void loadChallenge(long challengeId){
        //Bundle to transfer the ChallengeId to the fragments
        Bundle bundle = new Bundle();
        bundle.putLong(KEY_CHALLENGE_ID, challengeId);

        //Start a transaction on the fragments
        mFTransaction=mFManager.beginTransaction();
        //Create the QuestionFragment
        QuestionFragment questionFragment =new QuestionFragment();
        //Commit the bundle
        questionFragment.setArguments(bundle);
        //Inflate the QuestionFragment in the question_fragment
        mFTransaction.replace(R.id.challenge_fragment_question, questionFragment);

        //TODO ChallengeType evaluieren
        ChallengeDataSource.ChallengeType type = ChallengeDataSource.ChallengeType.MULTIPLE_CHOICE;
        switch (type){
            case MULTIPLE_CHOICE:
                //Create a MultipleChoiceFragment
                MultipleChoiceFragment multipleChoiceFragment = new MultipleChoiceFragment();
                //Commit the bundle
                multipleChoiceFragment.setArguments(bundle);
                mFTransaction.add(R.id.challenge_fragment, multipleChoiceFragment);
                //Inflate the MultipleChoiceFragment in the challenge_fragment
                mFTransaction.replace(R.id.challenge_fragment, multipleChoiceFragment);

                break;
            case TEXT:
                //TODO TextFragment integrieren
                //Create a TextFragment
                TextFragment textFragment = new TextFragment();
                //Commit the bundle
                textFragment.setArguments(bundle);
                //Inflate the TextFragment in the challenge_fragment
                mFTransaction.replace(R.id.challenge_fragment, textFragment);
                break;
        }

        //All actions are bundled as on action
        mFTransaction.addToBackStack(null);
        //Commit the changes
        mFTransaction.commit();
        mFManager.executePendingTransactions();
    }
}