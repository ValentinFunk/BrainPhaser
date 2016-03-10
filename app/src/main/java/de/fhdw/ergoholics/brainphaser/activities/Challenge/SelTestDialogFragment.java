package de.fhdw.ergoholics.brainphaser.activities.Challenge;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;

import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.activities.UserCreation.AvatarImageAdapter;

/**
 * Created by Chris on 3/10/2016.
 */
public class SelTestDialogFragment extends DialogFragment {

    public interface SelfTestDialogListener{
        public void onAnswerRight();
        public void onAnswerWrong();
    }

    // Use this interface to deliver action events
    SelfTestDialogListener mListener;
    // Reference to the created AlertDialog instance
    private AlertDialog mCreatedDialog;

    /**
     * The activity that opens this dialog must implement SelfTestDialogListener.
     * This method stores the listener when the activity is attached.
     */
    public void onAttach(Fragment fragment) {
        Activity activity = fragment.getActivity();
        // Verify that the host activity implements the callback interface
        try {
            // Cast to SelfTestDialogListener so we can send events to the host
            mListener = (SelfTestDialogListener) fragment;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(fragment.toString()
                    + " must implement NoticeDialogListener");
        }
    }


    /**
     * Called to create the Dialog. Creates a custom AlertDialog.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //stores the listener
        onAttach(getActivity().getSupportFragmentManager().findFragmentById(R.id.challenge_fragment));

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.self_test_dialog, null);
        builder.setView(view);

        //final SelTestDialogFragment self = this;
        builder.setMessage("TODO")
                .setPositiveButton("TODO RICHTIG", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onAnswerRight();
                    }
                })
                .setNegativeButton("TODO Falsch", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onAnswerWrong();
                    }
                });

        // Create the AlertDialog object
        mCreatedDialog = builder.create();
        return mCreatedDialog;
    }

}
