package de.fhdw.ergoholics.brainphaser.activities.playchallenge;

import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.fhdw.ergoholics.brainphaser.BrainPhaserComponent;
import de.fhdw.ergoholics.brainphaser.R;

/**
 * Fragment for a self-check challenge. Loads the SelfTestDialogFragment on checkAnswers.
 */
public class SelfTestFragment extends AnswerFragment {

    /**
     * Loads the SelfTestDialogFragment
     */
    @Override
    public void checkAnswers() {
        Bundle bundle = new Bundle();
        bundle.putLong(ChallengeActivity.KEY_CHALLENGE_ID, mChallenge.getId());
        //Load End Screen
        FragmentTransaction fTransaction=getFragmentManager().beginTransaction();
        fTransaction.disallowAddToBackStack();
        SelfTestDialogFragment selfTestDialogFragment = new SelfTestDialogFragment();
        //Commit the bundle
        selfTestDialogFragment.setArguments(bundle);
        //Inflate the MultipleChoiceFragment in the challenge_fragment
        fTransaction.replace(R.id.challenge_fragment, selfTestDialogFragment);
        //Commit the changes
        fTransaction.commit();
        getFragmentManager().executePendingTransactions();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate the view
        mView = inflater.inflate(R.layout.fragment_challenge_self_test_null, container, false);
        return mView;
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