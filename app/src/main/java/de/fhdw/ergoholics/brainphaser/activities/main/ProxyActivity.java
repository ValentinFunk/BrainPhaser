package de.fhdw.ergoholics.brainphaser.activities.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import java.io.InputStream;

import javax.inject.Inject;

import de.fhdw.ergoholics.brainphaser.BrainPhaserApplication;
import de.fhdw.ergoholics.brainphaser.BrainPhaserComponent;
import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.activities.BrainPhaserActivity;
import de.fhdw.ergoholics.brainphaser.activities.createuser.CreateUserActivity;
import de.fhdw.ergoholics.brainphaser.database.ChallengeDataSource;
import de.fhdw.ergoholics.brainphaser.logic.UserManager;
import de.fhdw.ergoholics.brainphaser.logic.fileimport.FileImport;

/**
 * Created by funkv on 29.02.2016.
 *
 * The activity redirects to user creation on first launch. On later launches it loads last selected
 * user and redirects to the main activity.
 */
public class ProxyActivity extends BrainPhaserActivity {
    @Inject
    UserManager mUserManager;
    @Inject
    ChallengeDataSource mChallengeDataSource;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void injectComponent(BrainPhaserComponent component) {
        component.inject(this);
    }

    /**
     * This method is called when the activity is created
     *
     * @param savedInstanceState handed over to super constructor
     */
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
            // Import challenges if the database does not include any
            if (mChallengeDataSource.getAll().size() == 0) {
                InputStream is = getResources().openRawResource(R.raw.challenges);
                try {
                    FileImport.importBPC(is, application);
                } catch (Exception e) {
                    throw new RuntimeException("An unexpected error has occured when trying to add " +
                            "example challenges!");
                }
            }

            startActivity(new Intent(Intent.ACTION_INSERT, Uri.EMPTY, getApplicationContext(), CreateUserActivity.class));
            finish();
        }
    }
}
