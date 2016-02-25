package de.fhdw.ergoholics.brainphaser.activities.Challenge;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.database.ChallengeDataSource;
import de.fhdw.ergoholics.brainphaser.model.Challenge;

/**
 * Created by Chris on 2/25/2016.
 */
public class TextFragment extends Fragment {

    private Challenge mChallenge;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_challenge_multiple_choice, container,false);

        Bundle bundle=getArguments();
        int id = bundle.getInt(ChallengeActivity.KEY_CHALLENGE_ID);
        mChallenge= ChallengeDataSource.getById((long) id);
        return view;

    }
}
