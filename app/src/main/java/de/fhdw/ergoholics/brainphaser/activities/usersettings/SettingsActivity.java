package de.fhdw.ergoholics.brainphaser.activities.usersettings;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import javax.inject.Inject;

import de.fhdw.ergoholics.brainphaser.BrainPhaserComponent;
import de.fhdw.ergoholics.brainphaser.BuildConfig;
import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.activities.BrainPhaserActivity;
import de.fhdw.ergoholics.brainphaser.database.SettingsDataSource;
import de.fhdw.ergoholics.brainphaser.logic.SettingsLogic;
import de.fhdw.ergoholics.brainphaser.logic.UserManager;
import de.fhdw.ergoholics.brainphaser.model.Settings;
import de.fhdw.ergoholics.brainphaser.utility.DividerItemDecoration;

/**
 * Created by funkv on 15.03.2016.
 *
 * Activity used for changing settings of the current user.
 */
public class SettingsActivity extends BrainPhaserActivity implements SettingsAdapter.OnSettingsChangedListener {
    @Inject
    UserManager mUserManager;
    @Inject
    SettingsLogic mSettingsLogic;
    @Inject
    SettingsDataSource mSettingsDataSource;

    private SettingsAdapter mSettingsAdapter;

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

        setContentView(R.layout.activity_settings);

        Toolbar myChildToolbar =
            (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myChildToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        //loading of the components
        RecyclerView settingsList = (RecyclerView) findViewById(R.id.settingsList);
        settingsList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setAutoMeasureEnabled(true);
        settingsList.setLayoutManager(layoutManager);
        settingsList.setNestedScrollingEnabled(false);

        //Create the View
        //Adapter which sets all users into the list
        mSettingsAdapter = new SettingsAdapter(mUserManager.getCurrentUser().getSettings(), this, mSettingsLogic);
        settingsList.setAdapter(mSettingsAdapter);

        // Avoid scrolldown
        settingsList.setFocusable(false);
        (findViewById(R.id.heading)).requestFocus();

        final SettingsActivity self = this;
        Button resetButton = (Button) findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                self.resetSettings();
            }
        });
    }

    /**
     * This method resets the settings of the user to the default settings and shows thm in the
     * activity.
     */
    private void resetSettings() {
        final Settings settings = mUserManager.getCurrentUser().getSettings();
        final Settings prevSettings = mSettingsDataSource.cloneSettings(settings);
        mSettingsDataSource.setToDefaultSettings(settings);
        mSettingsDataSource.update(settings);
        mSettingsAdapter.notifyDataSetChanged();

        final Snackbar snackbar = Snackbar
            .make(findViewById(R.id.main_content), getString(R.string.settings_reset), Snackbar.LENGTH_LONG)
            .setAction(R.string.undo, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Undo
                    mSettingsDataSource.copySettings(settings, prevSettings);
                    mSettingsDataSource.update(settings);
                    mSettingsAdapter.notifyDataSetChanged();
                }
            });

        snackbar.show();
    }

    /**
     * Called when the constructor-passed settings object has been changed.
     *
     * @param adapter adapter that triggered the change
     */
    @Override
    public void onSettingsChanged(SettingsAdapter adapter) {
        mSettingsDataSource.update(adapter.getSettings());

        // Assert correct save
        if (BuildConfig.DEBUG) {
            if (!mUserManager.getCurrentUser().getSettings().getId().equals(adapter.getSettings().getId())) {
                throw new RuntimeException("Saving didn't work, invalid references passed around?");
            }
        }
    }
}
