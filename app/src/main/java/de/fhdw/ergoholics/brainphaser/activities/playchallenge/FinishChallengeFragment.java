package de.fhdw.ergoholics.brainphaser.activities.playchallenge;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.fhdw.ergoholics.brainphaser.R;

/**
 * Created by Christian Kost
 * Fragment loads the finish screen
 */
public class FinishChallengeFragment extends Fragment {

    /**
     * Loads the finished view
     *
     * @param inflater           Inflates the fragment
     * @param container          Container to inflate the fragment
     * @param savedInstanceState Ignored
     * @return Return the inflated view
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate the view
        View view = inflater.inflate(R.layout.fragment_finish_challenge, container, false);
        return view;
    }

}
