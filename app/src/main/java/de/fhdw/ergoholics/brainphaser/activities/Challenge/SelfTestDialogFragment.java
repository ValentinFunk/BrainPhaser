package de.fhdw.ergoholics.brainphaser.activities.Challenge;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import de.fhdw.ergoholics.brainphaser.BrainPhaserComponent;
import de.fhdw.ergoholics.brainphaser.R;

/**
 * Created by Chris on 3/10/2016.
 */
public class SelfTestDialogFragment extends AnswerFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_challenge_self_test, container, false);
        Button btnRight = (Button)mView.findViewById(R.id.answerRight);
        Button btnWrong = (Button)mView.findViewById(R.id.answerWrong);

        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onAnswerChecked(true);
            }
        });
        btnWrong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onAnswerChecked(false);
            }
        });

        loadAnswers(R.id.answerListSelfCheck,null);
        return mView;
    }

    @Override
    public void checkAnswers() {
        return;
    }

    @Override
    protected void injectComponent(BrainPhaserComponent component) {
        component.inject(this);
    }
}