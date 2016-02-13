package de.fhdw.ergoholics.brainphaser;

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
        import android.widget.ImageView;

        import com.makeramen.roundedimageview.RoundedImageView;

/**
 * Created by funkv on 13.02.2016.
 *
 * Documentation (Dialogs): http://developer.android.com/guide/topics/ui/dialogs.html
 * Documentation (GridView): http://developer.android.com/guide/topics/ui/layout/gridview.html
 */
public class AvatarPickerDialogFragment extends DialogFragment implements AdapterView.OnItemClickListener {
    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it.
    */
    public interface AvatarPickerDialogListener {
        // resourceName is the name of the avatar resource that was selected. Name is used as
        // resource ids are not persistent
        public void onAvatarSelected(AvatarPickerDialogFragment dialog, String resourceName);
        public void onCancelled(AvatarPickerDialogFragment dialog);
    }

    // List of all available avatars
    private static Integer[] avatars = {
            R.drawable.anonymous, R.drawable.anonymous2_girl,
            R.drawable.astronaut, R.drawable.basketball_man,
            R.drawable.bomberman, R.drawable.bomberman2,
            R.drawable.boxer_hispanic, R.drawable.bride_hispanic_material,
            R.drawable.budist, R.drawable.call_center_operator_man,
            R.drawable.cashier_woman, R.drawable.cook2_man
    };

    public static Integer[] getAvatars( ) {
        return avatars;
    }

    // Use this interface to deliver action events
    AvatarPickerDialogListener mListener;

    // References to currently selected item
    private String selectedAvatarResourceEntryName;
    private RoundedImageView selectedAvatarImageView = null;

    // Reference to the created AlertDialog instance
    private AlertDialog dialog;

    /*
    The activity that opens this dialog must implement AvatarPickerDialogListener.
    This method overrides onAttach to cast the listener and store it.
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
        populateGrid(view);

        final AvatarPickerDialogFragment self = this;
        builder.setMessage(R.string.dialog_pick_avatar)
                .setPositiveButton(R.string.select, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onAvatarSelected(self, selectedAvatarResourceEntryName);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onCancelled(self);
                    }
                });

        // Create the AlertDialog object
        dialog = builder.create();

        // Disable select button until an avatar is picked
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            }
        });

        return dialog;
    }

    // Called when an avatar is selected in the dialog
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Avatar selected, allow clicking confirmation button
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);

        // Reset border on previously selected avatar when new one is picked
        if (selectedAvatarImageView != null) {
            selectedAvatarImageView.setBorderWidth(0.f);
        }

        // Use device independent pixels for uniform border width
        float borderWidthDip = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16.f, getResources().getDisplayMetrics());

        // Set border on currently selected avatar
        RoundedImageView imageView = (RoundedImageView)view;
        imageView.setBorderWidth(borderWidthDip);


        int accentColor = ContextCompat.getColor(getContext(), R.color.colorAccent);
        imageView.setBorderColor(accentColor);

        // Store selected resource id
        Integer avatarResourceId = avatars[position];
        Resources resources = getResources();
        selectedAvatarResourceEntryName = resources.getResourceEntryName(avatarResourceId);
        selectedAvatarImageView = imageView;
    }

    // Assign the image Adapter to populate the grid and hook up callbacks
    private void populateGrid(View view) {
        GridView gridView = (GridView)view.findViewById(R.id.avatarGrid);
        gridView.setAdapter(new ImageAdapter(getContext()));
        gridView.setOnItemClickListener(this);
    }
}
