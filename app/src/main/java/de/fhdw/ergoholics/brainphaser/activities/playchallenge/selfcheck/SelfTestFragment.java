package de.fhdw.ergoholics.brainphaser.activities.playchallenge.selfcheck;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import de.fhdw.ergoholics.brainphaser.BrainPhaserComponent;
import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.activities.playchallenge.AnswerFragment;

/**
 * Created by Christian Kost
 * <p/>
 * Fragment for a self-check challenge.
 */
public class SelfTestFragment extends AnswerFragment {
    private final static String KEY_CHECKING_ANSWER = "CHECKING_ANSWER";

    private boolean mCheckingAnswer = false;

    /**
     * Sets up the view depending on the state
     *
     * @param inflater           Inflates the fragment
     * @param container          Container to inflate the fragment
     * @param savedInstanceState Reloads the old state of the fragment
     * @return Return the inflated view
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mCheckingAnswer = savedInstanceState.getBoolean(KEY_CHECKING_ANSWER, false);
        }

        // Inflate the view depending on the state the fragment is currently in.
        if (mCheckingAnswer) {
            // shows all valid answers and asks user to confirm whether or not the selected answer is correct.
            View view = inflater.inflate(R.layout.fragment_challenge_self_test, container, false);

            Button btnRight = (Button) view.findViewById(R.id.answerRight);
            Button btnWrong = (Button) view.findViewById(R.id.answerWrong);
            //set on click listener to the button
            btnRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //execute AnswerListener and loads the next screen
                    mListener.onAnswerChecked(true, true);
                }
            });
            btnWrong.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //execute AnswerListener and loads the next screen
                    mListener.onAnswerChecked(false, true);
                }
            });

            return view;
        } else {
            // Shows only the hint to think of an answer and click on the FAB to continue.
            return inflater.inflate(R.layout.fragment_challenge_self_test_null, container, false);
        }
    }

    /**
     * Loads the list of answers
     */
    @Override
    public void onStart() {
        super.onStart();

        if (mCheckingAnswer) {
            //Loads the possible answers into a list
            populateRecyclerViewWithCorrectAnswers(R.id.answerListSelfCheck, null);
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
        outState.putBoolean(KEY_CHECKING_ANSWER, mCheckingAnswer);
    }

    /**
     * Reloads the fragment in checkAnswer mode.
     */
    @Override
    public AnswerFragment.ContinueMode goToNextState() {
        this.mCheckingAnswer = true;
        if (!this.isDetached()) {
            // Reload this fragment
            getFragmentManager().beginTransaction()
                    .detach(this)
                    .attach(this)
                    .commit();
        }
        return ContinueMode.CONTINUE_HIDE_FAB;
    }

    /**
     * Inject components
     *
     * @param component BrainPhaserComponent
     */
    @Override
    protected void injectComponent(BrainPhaserComponent component) {
        component.inject(this);
    }
}