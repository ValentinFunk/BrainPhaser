package de.fhdw.ergoholics.brainphaser.activities.UserCreation;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;

import de.fhdw.ergoholics.brainphaser.R;

/**
 * Activity used to create an user. Queries Username and avatar.
 * <p/>
 * Persistent data: none
 * Parameters: none
 * Return: EXTRA_USERNAME and EXTRA_AVATAR_RESOURCE_NAME
 */
public class CreateUserActivity extends FragmentActivity implements TextView.OnEditorActionListener, AvatarPickerDialogFragment.AvatarPickerDialogListener {
    public final static String EXTRA_USERNAME = "USERNAME";
    public final static String EXTRA_AVATAR_RESOURCE_NAME = "AVATAR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView input = (TextView) findViewById(R.id.input_username);
        input.setOnEditorActionListener(this);

        ImageView avatar = (ImageView) findViewById(R.id.avatar);
        avatar.setImageResource(Avatars.getDefaultAvatarResourceId());
    }

    /*
     * Called when an action is performed on the username input. Opens avatar selection after
     * username confirmation.
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) { // Enter is pressed
            createAvatarSelectDialog();
        }
        return false;
    }

    private void createAvatarSelectDialog() {
        FragmentManager fm = getSupportFragmentManager();
        AvatarPickerDialogFragment avatarPickerDialog = new AvatarPickerDialogFragment();
        avatarPickerDialog.show(fm, "avatar_picker");
    }

    /*
     * Called when an avatar has been selected in the selection dialog.
     */
    @Override
    public void onAvatarSelected(AvatarPickerDialogFragment dialog, String avatarResourceName) {
        TextView input = (TextView) findViewById(R.id.input_username);
        profileCreationFinished(input.getText().toString(), avatarResourceName);
    }

    /*
     * Called when avatar selection has been cancelled
     */
    @Override
    public void onCancelled(AvatarPickerDialogFragment dialog) {
        TextView input = (TextView) findViewById(R.id.input_username);
        profileCreationFinished(input.getText().toString(), Avatars.getDefaultAvatarResourceName());
    }

    /**
     * Called when the profile creation has been finished.
     *
     * @param username
     * @param avatarResourceName
     */
    private void profileCreationFinished(String username, String avatarResourceName) {
        // Update avatar for smooth transition via shared element
        ImageView avatar = (ImageView) findViewById(R.id.avatar);
        avatar.setImageResource(Avatars.getAvatarResourceId(getApplicationContext(), avatarResourceName));

        Intent resultData = new Intent();
        resultData.putExtra(EXTRA_USERNAME, username);
        resultData.putExtra(EXTRA_AVATAR_RESOURCE_NAME, avatarResourceName);
        setResult(Activity.RESULT_OK, resultData);

        finish();
    }
}
