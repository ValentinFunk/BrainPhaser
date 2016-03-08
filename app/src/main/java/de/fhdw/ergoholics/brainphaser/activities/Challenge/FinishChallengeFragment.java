package de.fhdw.ergoholics.brainphaser.activities.Challenge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.activities.main.MainActivity;

/**
 * Created by Chris on 3/3/2016.
 */
public class FinishChallengeFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_finish_challenge, container,false);

        Button finishButton = (Button) view.findViewById(R.id.finishChallengeBtn);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity activity = getActivity();

                Intent intent = new Intent(activity.getApplicationContext(), MainActivity.class);
                startActivity(intent);

                activity.setResult(Activity.RESULT_OK);
                activity.finish();
            }
        });
        return view;
    }

}
