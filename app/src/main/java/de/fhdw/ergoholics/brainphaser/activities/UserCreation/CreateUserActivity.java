package de.fhdw.ergoholics.brainphaser.activities.UserCreation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

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

    public final static int MAX_USERNAME_LENGTH = 30;

    private TextView mUsernameInput;
    private TextInputLayout mUsernameInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUsernameInput = (TextView) findViewById(R.id.input_username);
        mUsernameInputLayout = (TextInputLayout) findViewById(R.id.input_username_layout);
        mUsernameInput.setOnEditorActionListener(this);
        // Watch for changes and trigger validation
        mUsernameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                validateUsernameDuplicate();
                validateUsernameLength();
            }
        });

        ImageView avatar = (ImageView) findViewById(R.id.avatar);
        avatar.setImageResource(Avatars.getDefaultAvatarResourceId());
    }

    /*
     * Validate username and update GUI with errors
     */
    private boolean validateUsernameLength() {
        boolean isValid = true;
        String username = mUsernameInput.getText().toString().trim();
        if (username.length() == 0) {
            mUsernameInput.setError(getString(R.string.empty_username));
            mUsernameInputLayout.setErrorEnabled(true);
            isValid = false;
        } else if (username.length() > MAX_USERNAME_LENGTH) {
            mUsernameInput.setError(getString(R.string.too_long_username));
            mUsernameInputLayout.setErrorEnabled(true);
            isValid = false;
        } else {
            mUsernameInputLayout.setErrorEnabled(false);
            isValid = true;
        }
        return isValid;
    }

    private boolean validateUsernameDuplicate() {
        // TODO: Query datasource for username
        String username = mUsernameInput.getText().toString();
        return true;
    }

    /*
     * Called when an action is performed on the username input. Opens avatar selection after
     * username confirmation.
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) { // Enter is pressed
            if (validateUsernameDuplicate() && validateUsernameLength()) {
                createAvatarSelectDialog();
            }
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
     * @param username Username that was entered
     * @param avatarResourceName Resource name of the user's selected avatar
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
