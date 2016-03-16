package de.fhdw.ergoholics.brainphaser.activities.playchallenge;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import de.fhdw.ergoholics.brainphaser.BrainPhaserComponent;
import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.model.Answer;

/**
 * Fragment for a text challenge
 */
public class TextFragment extends AnswerFragment implements TextView.OnEditorActionListener {
    //Textfield of the answer
    private TextView mAnswerInput;
    private TextInputLayout mAnswerInputLayout;

    /**
     * inject components
     * @param component
     */
    @Override
    protected void injectComponent(BrainPhaserComponent component) {
        component.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_challenge_text, container, false);
        mAnswerInput = (EditText) mView.findViewById(R.id.answerText);
        mAnswerInputLayout = (TextInputLayout) mView.findViewById(R.id.input_answer_layout);
        mAnswerInput.setOnEditorActionListener(this);
        return mView;
    }


    /*
     * Validate answer and update GUI with errors
     */
    private boolean validateAnswerLength() {
        boolean isValid = true;
        String answer = mAnswerInput.getText().toString().trim();
        if (answer.length() == 0) {
            mAnswerInput.setError(getString(R.string.empty_answer));
            mAnswerInputLayout.setErrorEnabled(true);
            isValid = false;
        }
        return isValid;
    }

    /*
     * Called when an action is performed on the answer input. Checks the challenge
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) { // Enter is pressed
                checkAnswers();
        }
        return false;
    }

    /**
     * Checks the given answer
     * @return Is the answer right or nah
     */
    @Override
    public void checkAnswers() {
        if (validateAnswerLength()) {
            boolean answerRight = false;
            String givenAnswer = mAnswerInput.getText().toString();
            for (Answer item : mAnswerList) {
                if (item.getText().equals(givenAnswer)) {
                    answerRight = true;
                }
            }
            loadAnswers(R.id.answerListText, givenAnswer);
            mListener.onAnswerChecked(answerRight);
        }
    }
}
