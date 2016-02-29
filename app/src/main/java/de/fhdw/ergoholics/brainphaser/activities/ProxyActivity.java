package de.fhdw.ergoholics.brainphaser.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import de.fhdw.ergoholics.brainphaser.BrainPhaserApplication;
import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.activities.UserCreation.CreateUserActivity;

/**
 * Created by funkv on 29.02.2016.
 *
 * The activity redirects to user creation on first launch. On later launches it loads last selected
 * user and redirects to the main activity.
 */
public class ProxyActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_proxy);

        BrainPhaserApplication application = (BrainPhaserApplication)getApplication();
        if (application.logInLastUser()) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        } else {
            startActivity(new Intent(Intent.ACTION_INSERT, Uri.EMPTY, getApplicationContext(), CreateUserActivity.class));
            finish();
        }
    }
}
