package de.fhdw.ergoholics.brainphaser.activities.UserCreation;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.makeramen.roundedimageview.RoundedImageView;

import de.fhdw.ergoholics.brainphaser.R;

/**
 * Created by funkv on 13.02.2016.
 * <p/>
 * A Dialog Fragment that allows user to select an avatar out of a predefined list.
 * <p/>
 * Documentation (Dialogs): http://developer.android.com/guide/topics/ui/dialogs.html
 * Documentation (GridView): http://developer.android.com/guide/topics/ui/layout/gridview.html
 */
public class AvatarPickerDialogFragment extends DialogFragment implements AdapterView.OnItemClickListener {
    /**
     * The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it.
     */
    public interface AvatarPickerDialogListener {
        /**
         * Called when an avatar has been selected by the user.
         *
         * @param dialog       The dialog that trigerred the action.
         * @param resourceName The resource name of the selected avatar.
         */
        public void onAvatarSelected(AvatarPickerDialogFragment dialog, String resourceName);

        /**
         * Called when the avatar selection has been aborted by the user.
         *
         * @param dialog The dialog that triggered the action.
         */
        public void onCancelled(AvatarPickerDialogFragment dialog);
    }

    // Use this interface to deliver action events
    AvatarPickerDialogListener mListener;

    // References to currently selected item
    private String mSelectedAvatarResourceEntryName;
    private RoundedImageView mSelectedAvatarImageView = null;

    // Reference to the created AlertDialog instance
    private AlertDialog mCreatedDialog;

    /**
     * The activity that opens this dialog must implement AvatarPickerDialogListener.
     * This method stores the listener when the activity is attached.
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Verify that the host activity implements the callback interface
        try {
            // Cast to AvatarPickerDialogListener so we can send events to the host
            mListener = (AvatarPickerDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    /**
     * Called to create the Dialog. Creates a custom AlertDialog.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.avatar_picker, null);
        builder.setView(view);

        GridView gridView = (GridView) view.findViewById(R.id.avatarGrid);
        gridView.setAdapter(new AvatarImageAdapter(getContext()));
        gridView.setOnItemClickListener(this);

        final AvatarPickerDialogFragment self = this;
        builder.setMessage(R.string.dialog_pick_avatar)
                .setPositiveButton(R.string.select, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onAvatarSelected(self, mSelectedAvatarResourceEntryName);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onCancelled(self);
                    }
                });

        // Create the AlertDialog object
        mCreatedDialog = builder.create();

        // Disable select button until an avatar is picked
        mCreatedDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                mCreatedDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            }
        });

        return mCreatedDialog;
    }

    // Called when an avatar is selected in the dialog
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Avatar selected, allow clicking confirmation button
        mCreatedDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);

        // Reset border on previously selected avatar when new one is picked
        if (mSelectedAvatarImageView != null) {
            mSelectedAvatarImageView.setBorderWidth(0.f);
        }

        // Use device independent pixels for uniform border width
        float borderWidthDip = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16.f, getResources().getDisplayMetrics());

        // Set border on currently selected avatar
        RoundedImageView imageView = (RoundedImageView) view;
        imageView.setBorderWidth(borderWidthDip);


        int accentColor = ContextCompat.getColor(getContext(), R.color.colorAccent);
        imageView.setBorderColor(accentColor);

        // Store selected resource id
        Integer avatarResourceId = Avatars.getAvatarResources()[position];
        mSelectedAvatarResourceEntryName = Avatars.getAvatarResourceName(getContext(), avatarResourceId);
        mSelectedAvatarImageView = imageView;
    }
}
