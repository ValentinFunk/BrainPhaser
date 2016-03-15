package de.fhdw.ergoholics.brainphaser.activities.Challenge;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ToggleButton;

import de.fhdw.ergoholics.brainphaser.BrainPhaserComponent;
import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.model.Answer;

import java.util.Collections;
import java.util.List;

/**
 * Fragment for a multiple-choice challenge
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
        //inflate the view
        mView = inflater.inflate(R.layout.fragment_challenge_multiple_choice, container, false);
        //get all toggle buttons
        ToggleButton checkBox1 = (ToggleButton) mView.findViewById(R.id.checkbox1);
        ToggleButton checkBox2 = (ToggleButton) mView.findViewById(R.id.checkbox2);
        ToggleButton checkBox3 = (ToggleButton) mView.findViewById(R.id.checkbox3);
        ToggleButton checkBox4 = (ToggleButton) mView.findViewById(R.id.checkbox4);

        //save the toggle buttons in an array
        mCheckBoxArray = new ToggleButton[mAnswerList.size()];
        mCheckBoxArray[0] = checkBox1;
        mCheckBoxArray[1] = checkBox2;
        mCheckBoxArray[2] = checkBox3;
        mCheckBoxArray[3] = checkBox4;
        // shuffle and loads the answers into the buttons
        shuffleAnswers(mAnswerList);
        return mView;
    }

    /**
     * Shuffles and loads the answers of the challenge into the buttons
     *
     * @param answers The answers of the Challenge (Multiple_Choice)
     */
    public void shuffleAnswers(List<Answer> answers) {
        Collections.shuffle(answers);
        for (int i = 0; i < mAnswerList.size(); i++) {
            mCheckBoxArray[i].setTextOff(answers.get(i).getText());
            mCheckBoxArray[i].setTextOn(answers.get(i).getText());
            mCheckBoxArray[i].setText(answers.get(i).getText());
        }
    }

    /**
     * Checks if the given answer is correct, displays it and executes the AnswerListener
     */
    @Override
    public void checkAnswers() {
        Boolean[] booleanArray = new Boolean[mAnswerList.size()];
        Answer answer;

        for (int i = 0; i < mAnswerList.size(); i++) {
            answer = mAnswerList.get(i);
            Drawable bg = ContextCompat.getDrawable(getContext(),R.drawable.multiple_choice_unchecked);

            if (!mCheckBoxArray[i].isChecked() && !answer.getAnswerCorrect()) {
                //If answers is not toggled and not correct
                booleanArray[i] = true;
            } else if (mCheckBoxArray[i].isChecked() && answer.getAnswerCorrect()) {
                //If answer is toggled and correct
                booleanArray[i] = true;
                bg = ContextCompat.getDrawable(getContext(),R.drawable.multiple_choice_checked_right);
            } else if (!mCheckBoxArray[i].isChecked() && answer.getAnswerCorrect() ) {
                //if answers is not toggled and correct
                booleanArray[i] = false;
                bg = ContextCompat.getDrawable(getContext(),R.drawable.multiple_choice_unchecked_wrong);
            } else {
                //if answer is toggled and not correct
                bg = ContextCompat.getDrawable(getContext(), R.drawable.multiple_choice_checked_wrong);
                booleanArray[i] = false;
            }
            //Displays the state of the answer
            mCheckBoxArray[i].setBackgroundDrawable(bg);
        }
        //execute the AnswerListener
        mListener.onAnswerChecked(booleanArray[0] && booleanArray[1] && booleanArray[2] && booleanArray[3]);
    }
}
