package de.fhdw.ergoholics.brainphaser.activities.Challenge;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.database.ChallengeDataSource;

public class ChallengeActivity extends AppCompatActivity{

    public static final String KEY_CHALLENGE_ID="KEY_CHALLENGE_ID";
    private int mChallengeId;

    private FragmentManager mFManager;
    private FragmentTransaction mFTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);

        //FragementManager manges the fragments in the activity
        mFManager=getFragmentManager();

        mChallengeId = 1;
        loadChallenge(mChallengeId);

        Button btnNextChallenge = (Button)findViewById(R.id.btnNextChallenge);
        btnNextChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mChallengeId += 1;
                loadChallenge(mChallengeId);
            }
        });

    }

    /**
     *
     * @param challengeId The challenge's id to be solved
     */
    private void loadChallenge(int challengeId){
        //Bundle to transfer the ChallengeId to the fragments
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_CHALLENGE_ID, challengeId);

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
