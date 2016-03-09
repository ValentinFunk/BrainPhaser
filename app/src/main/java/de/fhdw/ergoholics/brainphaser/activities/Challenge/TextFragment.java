package de.fhdw.ergoholics.brainphaser.activities.Challenge;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import de.fhdw.ergoholics.brainphaser.BrainPhaserComponent;
import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.model.Answer;

/**
 * Created by Chris on 2/25/2016.
 */
public class TextFragment extends AnswerFragment {
    @Override
    protected void injectComponent(BrainPhaserComponent component) {
        component.inject(this);
    }

    View mView;
    //Textfield of the answer
    EditText mAnswerText;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_challenge_text, container, false);
        mAnswerText = (EditText) mView.findViewById(R.id.answerText);
        loadChallengeAndAnswers();
        return mView;

    }

    /**
     * Checks the given answer
     * @return Is the answer right or nah
     */
    @Override
    public boolean checkAnswers() {
        boolean answerRight=false;
        for (Answer item:mAnswerList) {
            if(item.getText().equals(mAnswerText.getText().toString())){
                answerRight=true;
            }
        }

        //loading of the components
        RecyclerView answerList = (RecyclerView) mView.findViewById(R.id.answerList);
        answerList.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        answerList.setLayoutManager(layoutManager);
        //Create the View
        //Adapter which sets all answers into the list
        AnswerAdapter listAdapter = new AnswerAdapter(mAnswerList);
        answerList.setAdapter(listAdapter);

        return answerRight;
    }
}
