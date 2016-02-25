package de.fhdw.ergoholics.brainphaser.activities.Challenge;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.model.Challenge;

/**
 * Created by Chris on 2/25/2016.
 */
public class QuestionFragment extends Fragment {

    private View mView;
    private TextView mQuestionText;
    enum ChallengeType{
        MULTIPLE_CHOICE, TEXT
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView =inflater.inflate(R.layout.fragment_challenge_question,container,false);
        mQuestionText =(TextView)mView.findViewById(R.id.challengeQuestion);

        return mView;
    }

    /**
     * Loads the question of the challenge into the text
     * @param challenge The new challenge, which question will be loaded
     */
    public void changeQuestion(Challenge challenge){
        //Set question text
        mQuestionText.setText(challenge.getQuestion());
    }
}
