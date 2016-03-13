package de.fhdw.ergoholics.brainphaser.activities.Challenge;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import de.fhdw.ergoholics.brainphaser.BrainPhaserComponent;
import de.fhdw.ergoholics.brainphaser.R;

/**
 * Fragment to let the user decide whether an answer is right or not
 */
public class SelfTestDialogFragment extends AnswerFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //infalte the view
        mView = inflater.inflate(R.layout.fragment_challenge_self_test, container, false);
        Button btnRight = (Button)mView.findViewById(R.id.answerRight);
        Button btnWrong = (Button)mView.findViewById(R.id.answerWrong);
        //set on click listener to the button
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //execute AnswerListener and unloads the fragment
                mListener.onAnswerChecked(true);
                changeFragment();
            }
        });
        btnWrong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //execute AnswerListener and unloads the fragment
                mListener.onAnswerChecked(false);
                changeFragment();
            }
        });

        //Loads the possible answers into a list
        loadAnswers(R.id.answerListSelfCheck,null);
        return mView;
    }

    /**
     * unloads the fragment and loads an empty fragment
     */
    private void changeFragment() {
        Bundle bundle = new Bundle();
        bundle.putLong(ChallengeActivity.KEY_CHALLENGE_ID, mChallenge.getId());
        //Load End Screen
        FragmentTransaction fTransaction = getFragmentManager().beginTransaction();
        fTransaction.disallowAddToBackStack();
        SelfTestFragment selfTestFragment = new SelfTestFragment();
        //Commit the bundle
        selfTestFragment.setArguments(bundle);
        //Inflate the MultipleChoiceFragment in the challenge_fragment
        fTransaction.replace(R.id.challenge_fragment, selfTestFragment);
        //Commit the changes
        fTransaction.commit();
        getFragmentManager().executePendingTransactions();
    }

    /**
     * Check answer
     */
    @Override
    public void checkAnswers() {
        return;
    }

    /**
     * Inject components
     * @param component
     */
    @Override
    protected void injectComponent(BrainPhaserComponent component) {
        component.inject(this);
    }
}