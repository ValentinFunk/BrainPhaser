package de.fhdw.ergoholics.brainphaser.activities.playchallenge;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import de.fhdw.ergoholics.brainphaser.BrainPhaserComponent;
import de.fhdw.ergoholics.brainphaser.R;

/**
 * Created by Christian Kost
 * Fragment to let the user decide whether an answer is right or not
 */
public class SelfTestDialogFragment extends AnswerFragment {

    SelfCheckAnswerListener mSelfCheckAnswerListener;

    /**
     * Loads the Listener and sets up the view
     *
     * @param inflater           Inflates the fragment
     * @param container          Container to inflate the fragment
     * @param savedInstanceState Ignored
     * @return Return the inflated view
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //load the interface
        loadSelfCheckAnswerListener();
        //infalte the view
        mView = inflater.inflate(R.layout.fragment_challenge_self_test, container, false);
        Button btnRight = (Button) mView.findViewById(R.id.answerRight);
        Button btnWrong = (Button) mView.findViewById(R.id.answerWrong);
        final FloatingActionButton btnNextChallenge = (FloatingActionButton) getActivity().findViewById(R.id.btnNextChallenge);
        btnNextChallenge.setVisibility(View.INVISIBLE);
        //set on click listener to the button
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //execute AnswerListener and loads the next screen
                mListener.onAnswerChecked(true);
                mSelfCheckAnswerListener.onSelfCheckAnswerChecked();
                btnNextChallenge.setVisibility(View.VISIBLE);
            }
        });
        btnWrong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //execute AnswerListener and loads the next screen
                mListener.onAnswerChecked(false);
                mSelfCheckAnswerListener.onSelfCheckAnswerChecked();
                btnNextChallenge.setVisibility(View.VISIBLE);
            }
        });

        //Loads the possible answers into a list
        loadAnswers(R.id.answerListSelfCheck, null);
        return mView;
    }

    /**
     * Loads the AnswerListener of the opening activity
     */
    private void loadSelfCheckAnswerListener() {
        // The activity that opens these fragments must implement AnswerListener.
        // This method stores the listener when the activity is attached.
        // Verify that the host activity implements the callback interface
        try {
            // Cast to SelfTestDialogListener so we can send events to the host
            mSelfCheckAnswerListener = (SelfCheckAnswerListener) getActivity();
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().toString()
                    + " must implement NoticeDialogListener");
        }
    }

    /**
     * Check answer (given answers is false)
     */
    @Override
    public void checkAnswers() {
        //execute AnswerListener and loads the next screen
        mListener.onAnswerChecked(false);
        mSelfCheckAnswerListener.onSelfCheckAnswerChecked();
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

    /**
     * Interface toggles, when the SelfCheck-Challenge was checked by the user
     */
    public interface SelfCheckAnswerListener {
        void onSelfCheckAnswerChecked();
    }
}