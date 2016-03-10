package de.fhdw.ergoholics.brainphaser.activities.Challenge;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.fhdw.ergoholics.brainphaser.BrainPhaserComponent;
import de.fhdw.ergoholics.brainphaser.R;

/**
 * Created by Chris on 3/10/2016.
 */
public class SelfTestFragment extends AnswerFragment implements SelTestDialogFragment.SelfTestDialogListener {
    View mView;
    boolean mAnswerRight;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_challenge_self_test, container, false);
        return mView;
    }

    @Override
    public boolean checkAnswers() {
        createSelfTestDialog();
        return mAnswerRight;
    }

    @Override
    protected void injectComponent(BrainPhaserComponent component) {
        component.inject(this);
    }

    private void createSelfTestDialog() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        SelTestDialogFragment selfTestDialog = new SelTestDialogFragment();
        selfTestDialog.show(fm, "self_test_dialog");
    }

    @Override
    public void onAnswerRight() {
        mAnswerRight=true;
    }

    @Override
    public void onAnswerWrong() {
        mAnswerRight=false;
    }
}
