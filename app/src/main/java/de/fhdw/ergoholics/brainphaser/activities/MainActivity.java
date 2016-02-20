package de.fhdw.ergoholics.brainphaser.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import de.fhdw.ergoholics.brainphaser.BrainPhaserApplication;
import de.fhdw.ergoholics.brainphaser.activities.CategorySelect.SelectCategoryActivity;
import de.fhdw.ergoholics.brainphaser.activities.UserCreation.CreateUserActivity;

/**
 * Created by funkv on 20.02.2016.
 *
 * The activity redirects to user creation on first launch, or loads last selected user if it has
 * been launched before.
 */
public class MainActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        BrainPhaserApplication application = (BrainPhaserApplication)getApplication();
        if (application.logInLastUser()) {
            startActivity(new Intent(getApplicationContext(), SelectCategoryActivity.class));
            finish();
        } else {
            startActivity(new Intent(getApplicationContext(), CreateUserActivity.class));
            finish();
        }
    }
}
