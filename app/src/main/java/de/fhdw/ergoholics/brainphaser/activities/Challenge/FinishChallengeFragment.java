package de.fhdw.ergoholics.brainphaser.activities.Challenge;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.fhdw.ergoholics.brainphaser.R;

/**
 * Fragment loads the finish screen
 */
public class FinishChallengeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate the view
        View view = inflater.inflate(R.layout.fragment_finish_challenge, container,false);
        return view;
    }

}
