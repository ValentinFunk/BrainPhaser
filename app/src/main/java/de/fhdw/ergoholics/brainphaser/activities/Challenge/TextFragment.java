package de.fhdw.ergoholics.brainphaser.activities.Challenge;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.model.Answer;

/**
 * Created by Chris on 2/25/2016.
 */
public class TextFragment extends AnswerFragment {


    EditText mAnserText;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_challenge_multiple_choice, container, false);
        mAnserText = (EditText) view.findViewById(R.id.answerText);
        loadChallengeAndAnswers();
        return view;

    }

    @Override
    public boolean checkAnswers() {
        boolean answerRight=false;
        for (Answer item:mAnswerList) {
            if(item.getText().equals( mAnserText.getText().toString())){
                answerRight=true;
            }
        }
        return answerRight;
    }
}
