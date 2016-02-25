package de.fhdw.ergoholics.brainphaser.activities.Challenge;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.model.Challenge;

/**
 * Created by Chris on 2/25/2016.
 */
public class MultipleChoiceFragment extends Fragment {

    private View mView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_challenge_multiple_choice, container,false);
        return mView;
    }

    /**
     * Loads the answers of the challenge into the checkboxes
     * @param challenge The new challenge, which answers will be loaded
     */
    public void changeAnswers(Challenge challenge){
        CheckBox cb1=(CheckBox)mView.findViewById(R.id.checkbox1);
        CheckBox cb2=(CheckBox)mView.findViewById(R.id.checkbox2);
        CheckBox cb3=(CheckBox)mView.findViewById(R.id.checkbox3);
        CheckBox cb4=(CheckBox)mView.findViewById(R.id.checkbox4);
        //TODO Get Answers from DB
        cb1.setText("A");
        cb2.setText("B");
        cb3.setText("C");
        cb4.setText("D");
    }
}
