package de.fhdw.ergoholics.brainphaser.activities.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import de.fhdw.ergoholics.brainphaser.BrainPhaserApplication;
import de.fhdw.ergoholics.brainphaser.BuildConfig;
import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.activities.UserCreation.CreateUserActivity;
import de.fhdw.ergoholics.brainphaser.activities.UserSelection.UserSelectionActivity;

/**
 * Created by funkv on 20.02.2016.
 *
 * The activity redirects to user creation on first launch, or loads last selected user if it has
 * been launched before.
 */
public class MainActivity extends AppCompatActivity {
    public static String EXTRA_NAVIGATE_TO = "NAVIGATE_TO";
    public static String EXTRA_SHOW_LOGGEDIN_SNACKBAR = "SHOW_SNACKBAR";

    private ViewPager mViewPager;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);

        BrainPhaserApplication application = (BrainPhaserApplication)getApplication();
        if (!application.logInLastUser()) {
            startActivity(new Intent(Intent.ACTION_INSERT, Uri.EMPTY, getApplicationContext(), CreateUserActivity.class));
            finish();
        }

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
            String text = String.format(getResources().getString(R.string.logged_in_as), app.getCurrentUser().getName());
            Snackbar
                .make(rootView, text, Snackbar.LENGTH_LONG)
                .setAction(R.string.switch_user, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), UserSelectionActivity.class);
                        startActivity(intent);
                    }
                }).show();

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