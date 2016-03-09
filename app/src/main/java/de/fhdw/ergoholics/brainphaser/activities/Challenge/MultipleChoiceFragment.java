package de.fhdw.ergoholics.brainphaser.activities.Challenge;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ToggleButton;

import java.util.Collections;
import java.util.List;

import de.fhdw.ergoholics.brainphaser.BrainPhaserComponent;
import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.model.Answer;

/**
 * Created by Chris on 2/25/2016.
 */
public class MultipleChoiceFragment extends AnswerFragment {
    private ToggleButton[] mCheckBoxArray;

    @Override
    protected void injectComponent(BrainPhaserComponent component) {
        component.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_challenge_multiple_choice, container, false);
        ToggleButton checkBox1 = (ToggleButton) view.findViewById(R.id.checkbox1);
        ToggleButton checkBox2 = (ToggleButton) view.findViewById(R.id.checkbox2);
        ToggleButton checkBox3 = (ToggleButton) view.findViewById(R.id.checkbox3);
        ToggleButton checkBox4 = (ToggleButton) view.findViewById(R.id.checkbox4);

        loadChallengeAndAnswers();

        //Fill Checkboxes
        mCheckBoxArray = new ToggleButton[mAnswerList.size()];
        mCheckBoxArray[0] = checkBox1;
        mCheckBoxArray[1] = checkBox2;
        mCheckBoxArray[2] = checkBox3;
        mCheckBoxArray[3] = checkBox4;
        shuffleAnswers(mAnswerList);
        return view;

    }

    /**
     * Loads the answers of the challenge into the checkboxes
     *
     * @param answers The answers of the Challenge (Multiple_Choice)
     */
    public void shuffleAnswers(List<Answer> answers) {
        Collections.shuffle(answers);
        for (int i = 0; i < mAnswerList.size(); i++) {
            mCheckBoxArray[i].setText(answers.get(i).getText());
        }
    }

    @Override
    public boolean checkAnswers() {
        Boolean[] booleanArray = new Boolean[mAnswerList.size()];
        Answer answer;

        for (int i = 0; i < mAnswerList.size(); i++) {
            answer = mAnswerList.get(i);
            int backgroundColor = ContextCompat.getColor(getContext(), R.color.colorRight);
            if (mCheckBoxArray[i].isChecked() == false && answer.getAnswerCorrect() == false) {
                booleanArray[i] = true;
            } else if (mCheckBoxArray[i].isChecked() && answer.getAnswerCorrect()) {
                booleanArray[i] = true;
            } else if (mCheckBoxArray[i].isChecked() != answer.getAnswerCorrect() && answer.getAnswerCorrect()) {
                booleanArray[i] = false;
            } else {
                backgroundColor = ContextCompat.getColor(getContext(), R.color.colorWrong);
                booleanArray[i] = false;
            }
            mCheckBoxArray[i].setBackgroundColor(backgroundColor);
        }
        return booleanArray[0] && booleanArray[1] && booleanArray[2] && booleanArray[3];
    }
}
