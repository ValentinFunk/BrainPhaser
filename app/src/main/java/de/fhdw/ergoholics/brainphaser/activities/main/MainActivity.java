package de.fhdw.ergoholics.brainphaser.activities.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.nononsenseapps.filepicker.FilePickerActivity;

import de.fhdw.ergoholics.brainphaser.BrainPhaserApplication;
import de.fhdw.ergoholics.brainphaser.BrainPhaserComponent;
import de.fhdw.ergoholics.brainphaser.BuildConfig;
import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.activities.BrainPhaserActivity;
import de.fhdw.ergoholics.brainphaser.activities.ChallengeImport.ImportChallengeActivity;
import de.fhdw.ergoholics.brainphaser.activities.UserCreation.CreateUserActivity;
import de.fhdw.ergoholics.brainphaser.activities.UserSelection.UserSelectionActivity;
import de.fhdw.ergoholics.brainphaser.logic.UserManager;

import javax.inject.Inject;

/**
 * Created by funkv on 20.02.2016.
 *
 * The activity redirects to user creation on first launch, or loads last selected user if it has
 * been launched before.
 */
public class MainActivity extends BrainPhaserActivity {
    public static String EXTRA_NAVIGATE_TO = "NAVIGATE_TO";
    public static String EXTRA_SHOW_LOGGEDIN_SNACKBAR = "SHOW_SNACKBAR";
    private final static int CODE_FILEPICKER = 0;

    @Inject UserManager mUserManager;
    private ViewPager mViewPager;

    @Override
    protected void injectComponent(BrainPhaserComponent component) {
        component.inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);


        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);

        // Set as Actionbar
        setSupportActionBar(toolbar);

        TabLayout layout = (TabLayout)findViewById(R.id.tabs);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new NavigationTabsPagerAdapter(getSupportFragmentManager(), getApplicationContext()));
        mViewPager = viewPager;

        layout.setupWithViewPager(viewPager);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        if (BuildConfig.DEBUG) {
            MenuItem item = menu.add("Import BPC");
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_switch_user:
                startActivity(new Intent(getApplicationContext(), UserSelectionActivity.class));
                return true;
            case R.id.action_about:
                // TODO
            case R.id.action_settings:
                // TODO
            case 0: // Only in debug mode: File Picker
                Intent i = new Intent(getApplicationContext(), FilePickerActivity.class);
                startActivityForResult(i, CODE_FILEPICKER);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Pass the intent containing the file URI on to the Import Challenge method
        if (requestCode == CODE_FILEPICKER) {
            if (data != null) {
                data.setClass(getApplicationContext(), ImportChallengeActivity.class);
                startActivity(data);
            }
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }



    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        if (intent == null) {
            return;
        }

        Navigation.NavigationState state = (Navigation.NavigationState) intent.getSerializableExtra(EXTRA_NAVIGATE_TO);
        if (state != null) {
            navigateToState(state);
        }

        // If EXTRA_SHOW_LOGGEDIN_SNACKBAR is passed,
        // show a little snackbar that shows the currently logged in user's name
        if (intent.getBooleanExtra(EXTRA_SHOW_LOGGEDIN_SNACKBAR, false)) {
            BrainPhaserApplication app = (BrainPhaserApplication) getApplication();
            View rootView = findViewById(R.id.main_content);
            String text = String.format(getResources().getString(R.string.logged_in_as), mUserManager.getCurrentUser().getName());
            final Snackbar snackbar = Snackbar
                .make(rootView, text, Snackbar.LENGTH_LONG)
                .setAction(R.string.switch_user_short, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), UserSelectionActivity.class);
                        startActivity(intent);
                    }
                });

            // Delay the snackbar a quater second for a smoother experience
            rootView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    snackbar.show();
                }
            }, 250);

            // Update the intent so it doesn't show again on back navigation and thus only when explicitly requested
            intent.putExtra(EXTRA_SHOW_LOGGEDIN_SNACKBAR, false);
        }
    }

    /**
     * Navigate to a given view state by ID
     * @param state Id of the state to navigate to.
     */
    public void navigateToState(Navigation.NavigationState state) {
        if (BuildConfig.DEBUG && state == null) {
            throw new RuntimeException("Attempting to switch to invalid navigation state");
        }

        mViewPager.setCurrentItem(state.ordinal());
    }
}