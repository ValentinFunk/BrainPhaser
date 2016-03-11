package de.fhdw.ergoholics.brainphaser.activities.Challenge;

import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.fhdw.ergoholics.brainphaser.BrainPhaserComponent;
import de.fhdw.ergoholics.brainphaser.R;

/**
 * Created by Chris on 3/10/2016.
 */
public class SelfTestFragment extends AnswerFragment {

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
        mView = inflater.inflate(R.layout.fragment_challenge_self_test_null, container, false);
        return mView;
    }
    @Override
    protected void injectComponent(BrainPhaserComponent component) {
        component.inject(this);
    }
}
