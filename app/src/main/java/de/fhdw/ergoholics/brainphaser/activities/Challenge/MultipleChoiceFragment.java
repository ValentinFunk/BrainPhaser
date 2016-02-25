package de.fhdw.ergoholics.brainphaser.activities.Challenge;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.database.ChallengeDataSource;
import de.fhdw.ergoholics.brainphaser.model.Challenge;

/**
 * Created by Chris on 2/25/2016.
 */
public class MultipleChoiceFragment extends Fragment{



    private Challenge mChallenge;
    CheckBox mCheckBox1;
    CheckBox mCheckBox2;
    CheckBox mCheckBox3;
    CheckBox mCheckBox4;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_challenge_multiple_choice, container,false);
        mCheckBox1 =(CheckBox) view.findViewById(R.id.checkbox1);
        mCheckBox2 =(CheckBox) view.findViewById(R.id.checkbox2);
        mCheckBox3 =(CheckBox) view.findViewById(R.id.checkbox3);
        mCheckBox4 =(CheckBox) view.findViewById(R.id.checkbox4);

        Bundle bundle=getArguments();
        int id = bundle.getInt(ChallengeActivity.KEY_CHALLENGE_ID);
        mChallenge= ChallengeDataSource.getById((long) id);

        changeAnswers(mChallenge);
        return view;
    }

    /**
     * Loads the answers of the challenge into the checkboxes
     * @param challenge The new challenge, which answers will be loaded
     */
    public void changeAnswers(Challenge challenge){
        //TODO Get Answers from DB
        mCheckBox1.setText("A");
        mCheckBox2.setText("B");
        mCheckBox3.setText("C");
        mCheckBox4.setText("D");
    }
}
