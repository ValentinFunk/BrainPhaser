package de.fhdw.ergoholics.brainphaser.activities.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import de.fhdw.ergoholics.brainphaser.BrainPhaserApplication;
import de.fhdw.ergoholics.brainphaser.BrainPhaserComponent;
import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.activities.BrainPhaserActivity;
import de.fhdw.ergoholics.brainphaser.activities.createuser.CreateUserActivity;
import de.fhdw.ergoholics.brainphaser.logic.UserManager;

import javax.inject.Inject;

/**
 * Created by funkv on 29.02.2016.
 *
 * The activity redirects to user creation on first launch. On later launches it loads last selected
 * user and redirects to the main activity.
 */
public class ProxyActivity extends BrainPhaserActivity {
    @Inject UserManager mUserManager;

    @Override
    protected void injectComponent(BrainPhaserComponent component) {
        component.inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_proxy);

        BrainPhaserApplication application = (BrainPhaserApplication)getApplication();
        if (mUserManager.logInLastUser()) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra(MainActivity.EXTRA_SHOW_LOGGEDIN_SNACKBAR, true);

            startActivity(intent);
            finish();
        } else {
            startActivity(new Intent(Intent.ACTION_INSERT, Uri.EMPTY, getApplicationContext(), CreateUserActivity.class));
            finish();
        }
    }
}
