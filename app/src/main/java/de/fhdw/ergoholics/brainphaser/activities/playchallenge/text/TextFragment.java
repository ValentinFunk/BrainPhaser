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
    private String KEY_ANSWER_CHECKED;

    //Textfield of the answer
    private TextView mAnswerInput;
    private TextInputLayout mAnswerInputLayout;
    private boolean mAnswerChecked = false;

    /**
     * Inject components
     *
     * @param component BrainPhaserComponent
     */
    @Override
    protected void injectComponent(BrainPhaserComponent component) {
        component.inject(this);
    }

    /**
     * Setups the view
     *
     * @param inflater           Inflates the fragment
     * @param container          Container to inflate the fragment
     * @param savedInstanceState Ignored
     * @return Return the inflated view
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_challenge_text, container, false);
        mAnswerInput = (EditText) view.findViewById(R.id.answerText);
        mAnswerInputLayout = (TextInputLayout) view.findViewById(R.id.input_answer_layout);
        mAnswerInput.setOnEditorActionListener(this);
        return view;
    }

    /**
     * Restores the old state of the view
     *
     * @param savedInstanceState State if the answer was checked
     */
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            mAnswerChecked = savedInstanceState.getBoolean(KEY_ANSWER_CHECKED);
            if (mAnswerChecked) {
                mAnswerInput.setFocusable(false); // Don't auto focus
            } else {
                mAnswerInput.requestFocus();
            }
        }

        if (mAnswerChecked) {
            updateViewForAnswer();
        }
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
     * Updates the view to show the answered state
     *
     * @return whether or not the answer was correct.
     */
    private boolean updateViewForAnswer() {
        boolean answerRight = false;
        String givenAnswer = mAnswerInput.getText().toString();
        for (Answer item : mAnswerList) {
            // Case insensitive check without trailing/leading spaces
            if (item.getText().trim().toLowerCase().equals(givenAnswer.trim().toLowerCase())) {
                answerRight = true;
            }
        }
        populateRecyclerViewWithCorrectAnswers(R.id.answerListText, givenAnswer);
        mAnswerInput.setEnabled(false);
        mAnswerInput.clearFocus();
        if (!answerRight) {
            mAnswerInput.setError(getString(R.string.wrong_answer));
            mAnswerInputLayout.setErrorEnabled(true);
        }
        return answerRight;
    }

    /**
     * Checks the given answer
     *
     * @return ContinueMode
     */
    @Override
    public AnswerFragment.ContinueMode goToNextState() {
        if (validateAnswerLength()) {
            final boolean result = updateViewForAnswer();
            View view = getView();
            if (view != null) {
                getView().post(new Runnable() {
                    @Override
                    public void run() {
                        mListener.onAnswerChecked(result, false);
                    }
                });
            }
            mAnswerChecked = true;

            return ContinueMode.CONTINUE_SHOW_FAB;
        } else {
            return ContinueMode.CONTINUE_ABORT;
        }
    }

    /**
     * Saves the current state of the fragment
     *
     * @param outState Bundle that contains the Challenge-Id and
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_ANSWER_CHECKED, mAnswerChecked);
    }
}