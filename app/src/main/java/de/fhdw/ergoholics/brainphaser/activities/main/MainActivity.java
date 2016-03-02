package de.fhdw.ergoholics.brainphaser.activities.main;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import de.fhdw.ergoholics.brainphaser.BrainPhaserApplication;
import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.activities.CategorySelect.SelectCategoryActivity;
import de.fhdw.ergoholics.brainphaser.activities.UserCreation.CreateUserActivity;
import de.fhdw.ergoholics.brainphaser.activities.UserSelection.UserSelectionActivity;

import java.util.Arrays;

/**
 * Created by funkv on 20.02.2016.
 *
 * The activity redirects to user creation on first launch, or loads last selected user if it has
 * been launched before.
 */
public class MainActivity extends AppCompatActivity {
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
        if (application.logInLastUser()) {
            // TODO: Go to
        } else {
            startActivity(new Intent(Intent.ACTION_INSERT, Uri.EMPTY, getApplicationContext(), CreateUserActivity.class));
            finish();
        }

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);

        // Set as Actionbar
        setSupportActionBar(toolbar);

        TabLayout layout = (TabLayout)findViewById(R.id.tabs);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new NavigationTabsPagerAdapter(getSupportFragmentManager()));

        layout.setupWithViewPager(viewPager);
    }
}
