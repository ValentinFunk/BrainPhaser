package de.fhdw.ergoholics.brainphaser.activities.playchallenge.text;

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
import de.fhdw.ergoholics.brainphaser.activities.playchallenge.AnswerFragment;
import de.fhdw.ergoholics.brainphaser.model.Answer;

/**
 * Created by Christian Kost
 * <p/>
 * Fragment for a text challenge. Compares the given text with the answers of the challenge and loads all answers on goToNextState.
 */
public class TextFragment extends AnswerFragment implements TextView.OnEditorActionListener {
    //Textfield of the answer
    private TextView mAnswerInput;
    private TextInputLayout mAnswerInputLayout;

    /**
     * Inject components
     *
     * @param component BrainPhaserComponent
     */
    @Override
    protected void injectComponent(BrainPhaserComponent component) {
        component.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_challenge_text, container, false);
        mAnswerInput = (EditText) view.findViewById(R.id.answerText);
        mAnswerInputLayout = (TextInputLayout) view.findViewById(R.id.input_answer_layout);
        mAnswerInput.setOnEditorActionListener(this);
        return view;
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
            getActivity().findViewById(R.id.btnNextChallenge).callOnClick(); // Same as FAB click
        }
        return false;
    }

    /**
     * Checks the given answer
     */
    @Override
    public AnswerFragment.ContinueMode goToNextState() {
        if (validateAnswerLength()) {
            boolean answerRight = false;
            String givenAnswer = mAnswerInput.getText().toString();
            for (Answer item : mAnswerList) {
                // Case insensitive check without trailing/leading spaces
                if (item.getText().trim().toLowerCase().equals(givenAnswer.trim().toLowerCase())) {
                    answerRight = true;
                }
            }
            populateRecyclerViewWithCorrectAnswers(R.id.answerListText, givenAnswer);

            final boolean result = answerRight;
            getView().post(new Runnable() {
                @Override
                public void run() {
                    mListener.onAnswerChecked(result, false);
                }
            });

            mAnswerInput.setEnabled(false);
            mAnswerInput.clearFocus();

            if (!answerRight) {
                mAnswerInput.setError(getString(R.string.wrong_answer));
                mAnswerInputLayout.setErrorEnabled(true);
            }

            return ContinueMode.CONTINUE_SHOW_FAB;
        } else {
            return ContinueMode.CONTINUE_ABORT;
        }
    }
}