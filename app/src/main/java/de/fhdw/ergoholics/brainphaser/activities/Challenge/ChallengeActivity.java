package de.fhdw.ergoholics.brainphaser.activities.Challenge;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.model.Challenge;

public class ChallengeActivity extends AppCompatActivity{

    public static final String KEY_CHALLENGE_ID="KEY_CHALLENGE_ID";

    private enum ChallengeType{
        MULTIPLE_CHOICE, TEXT
    }

    private FragmentManager mFManager;
    private FragmentTransaction mFTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);

        //FragementManager manges the fragments in the activity
        mFManager=getFragmentManager();

        mChallenge=new Challenge((long)2,2,"Dies ist meine Frage",(long)3);

        loadChallenge(mChallenge);

        Button btnNextChallenge = (Button)findViewById(R.id.btnNextChallenge);
        btnNextChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mChallenge = new Challenge((long) 2, 2, "Dies ist meine 2. Frage", (long) 3);
                loadChallenge(mChallenge);
            }
        });

    }

    /**
     *
     * @param challenge The challenge to be solved
     */
    private void loadChallenge(Challenge challenge){
        QuestionFragment questionFragment;
        MultipleChoiceFragment multipleChoiceFragment;
        //Start a transaction on the fragments
        mFTransaction=mFManager.beginTransaction();
        //Create the QuestionFragment
        questionFragment =new QuestionFragment(this);
        //Inflate the QuestionFragment in the question_fragment
        mFTransaction.replace(R.id.challenge_fragment_question, questionFragment);

        //TODO ChallengeType evaluieren
        ChallengeType type=ChallengeType.MULTIPLE_CHOICE;
        switch (type){
            case MULTIPLE_CHOICE:
                //Create a MultipleChoiceFragment
                multipleChoiceFragment = new MultipleChoiceFragment(this);
                //Inflate the MultipleChoiceFragment in the challenge_fragment
                mFTransaction.replace(R.id.challenge_fragment, multipleChoiceFragment);

                break;
            case TEXT:
                //Create a TextFragment
                TextFragment tFragment = new TextFragment();
                //Inflate the TextFragment in the challenge_fragment
                mFTransaction.replace(R.id.challenge_fragment, tFragment);
                break;
        }

        //All actions are bundled as on action
        mFTransaction.addToBackStack(null);
        //Commit the changes
        mFTransaction.commit();
        mFManager.executePendingTransactions();
    }


}
