package de.fhdw.ergoholics.brainphaser.activities.Settings;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import javax.inject.Inject;

import de.fhdw.ergoholics.brainphaser.BrainPhaserComponent;
import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.activities.BrainPhaserActivity;
import de.fhdw.ergoholics.brainphaser.logic.UserManager;
import de.fhdw.ergoholics.brainphaser.utility.DividerItemDecoration;

/**
 * Created by funkv on 15.03.2016.
 *
 * Activity used for changing settings of the current user.
 */
public class SettingsActivity extends BrainPhaserActivity implements SettingsAdapter.OnSettingsChangedListener {
    @Inject UserManager mUserManager;
    private SettingsAdapter mSettingsAdapter;

    @Override
    protected void injectComponent(BrainPhaserComponent component) {
        component.inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        //loading of the components
        RecyclerView settingsList = (RecyclerView) findViewById(R.id.settingsList);
        settingsList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setAutoMeasureEnabled(true);
        settingsList.setLayoutManager(layoutManager);

        //Create the View
        //Adapter which sets all users into the list
        mSettingsAdapter = new SettingsAdapter(mUserManager.getCurrentUser().getSettings(), this);
        settingsList.setAdapter(mSettingsAdapter);

        // Prevent scrollin gthere
        settingsList.setFocusable(false);
        findViewById(R.id.heading).requestFocus();
    }

    @Override
    public void onStageChanged(int stage, int seconds) {

    }
}
