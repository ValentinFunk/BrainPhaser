package de.fhdw.ergoholics.brainphaser;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements TextView.OnEditorActionListener, AvatarPickerDialogFragment.AvatarPickerDialogListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView input = (TextView)findViewById(R.id.input_username);
        input.setOnEditorActionListener(this);
    }

    private void createAvatarSelectDialog( ) {
        FragmentManager fm = getSupportFragmentManager();
        AvatarPickerDialogFragment avatarPickerDialog = new AvatarPickerDialogFragment();
        avatarPickerDialog.show(fm, "avatar_picker");
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            createAvatarSelectDialog( );
        }
        return false;
    }

    @Override
    public void onAvatarSelected(AvatarPickerDialogFragment dialog, String resourceName) {
        Resources resources = getResources();
        int resId = resources.getIdentifier(resourceName, "drawable", getPackageName());

        // TODO: Discard and handle correctly
        ImageView avatar = (ImageView)findViewById(R.id.avatar);
        avatar.setImageResource(resId);
    }

    @Override
    public void onCancelled(AvatarPickerDialogFragment dialog) {
        // TODO: Use default anonymous avatar
    }
}
