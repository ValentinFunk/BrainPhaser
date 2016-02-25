package de.fhdw.ergoholics.brainphaser.activities.Challenge;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.fhdw.ergoholics.brainphaser.R;

/**
 * Created by Chris on 2/25/2016.
 */
public class TextFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_challenge_text, container,false);
    }
}
