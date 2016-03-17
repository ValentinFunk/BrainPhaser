package de.fhdw.ergoholics.brainphaser.activities.usersettings;

import android.os.Bundle;
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
    @Inject UserManager mUserManager;
    @Inject SettingsLogic mSettingsLogic;
    @Inject SettingsDataSource mSettingsDataSource;
    private SettingsAdapter mSettingsAdapter;

    @Override
    protected void injectComponent(BrainPhaserComponent component) {
        component.inject(this);
    }

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

    private void resetSettings() {
        Settings settings = mUserManager.getCurrentUser().getSettings();
        mSettingsDataSource.setToDefaultSettings(settings);
        mSettingsDataSource.update(settings);
        mSettingsAdapter.notifyDataSetChanged();
    }

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
