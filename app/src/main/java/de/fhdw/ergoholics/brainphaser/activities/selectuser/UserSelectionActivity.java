package de.fhdw.ergoholics.brainphaser.activities.selectuser;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.List;

import javax.inject.Inject;

import de.fhdw.ergoholics.brainphaser.BrainPhaserApplication;
import de.fhdw.ergoholics.brainphaser.BrainPhaserComponent;
import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.activities.BrainPhaserActivity;
import de.fhdw.ergoholics.brainphaser.activities.createuser.CreateUserActivity;
import de.fhdw.ergoholics.brainphaser.activities.main.MainActivity;
import de.fhdw.ergoholics.brainphaser.database.CompletionDataSource;
import de.fhdw.ergoholics.brainphaser.database.SettingsDataSource;
import de.fhdw.ergoholics.brainphaser.database.StatisticsDataSource;
import de.fhdw.ergoholics.brainphaser.database.UserDataSource;
import de.fhdw.ergoholics.brainphaser.logic.UserManager;
import de.fhdw.ergoholics.brainphaser.model.Completion;
import de.fhdw.ergoholics.brainphaser.model.Settings;
import de.fhdw.ergoholics.brainphaser.model.Statistics;
import de.fhdw.ergoholics.brainphaser.model.User;
import de.fhdw.ergoholics.brainphaser.utility.DividerItemDecoration;

/**
 * Created by Christian Kost
 * <p/>
 * Activity used to chose an existing user. Can load the create user activity and the category selection
 */
public class UserSelectionActivity extends BrainPhaserActivity implements UserAdapter.ResultListener {
    UserAdapter mListAdapter;
    @Inject
    UserManager mUserManager;
    @Inject
    UserDataSource mUserDataSource;
    @Inject
    SettingsDataSource mSettingsDataSource;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void injectComponent(BrainPhaserComponent component) {
        component.inject(this);
    }

    /**
     * Setup the view, adds listener and loads all users
     *
     * @param savedInstanceState Ignored
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_selection);

        Toolbar myChildToolbar =
                (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myChildToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        //loading of the components
        RecyclerView userList = (RecyclerView) findViewById(R.id.userList);
        userList.setHasFixedSize(true);
        userList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        registerForContextMenu(userList);

        //load the users from the database
        List<User> allUsers = mUserDataSource.getAll();

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        userList.setLayoutManager(layoutManager);

        //Create the View
        //Adapter which sets all users into the list
        mListAdapter = new UserAdapter(allUsers, this, (BrainPhaserApplication) getApplication());
        userList.setAdapter(mListAdapter);

        /**
         * Is the add button selected load Create-User-Activity.
         */
        FloatingActionButton btnAddUser = (FloatingActionButton) findViewById(R.id.addUser);
        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Intent.ACTION_INSERT, Uri.EMPTY, getApplicationContext(), CreateUserActivity.class), 0);
            }
        });
    }

    /**
     * Used to track when the create user activity has finished. If it was cancelled, do nothing,
     * else finish this activity.
     *
     * @param requestCode Ignored
     * @param resultCode  The result of the activity
     * @param data        Ignored
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            setResult(Activity.RESULT_OK);
            finish();
        }
    }

    /**
     * When a user has been selected, finish this activity and load select category activity.
     *
     * @param user To switched user
     */
    @Override
    public void onUserSelected(User user) {
        mUserManager.switchUser(user);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        setResult(Activity.RESULT_OK);
        finish();
    }

    /**
     * On edit a user, finish this activity and load edit user activity
     *
     * @param user To edited user
     */
    @Override
    public void onEditUser(User user) {
        Intent intent = new Intent(Intent.ACTION_EDIT, Uri.EMPTY, getApplicationContext(), CreateUserActivity.class);
        intent.putExtra(CreateUserActivity.KEY_USER_ID, user.getId());
        startActivityForResult(intent, 0);
    }

    /**
     * On delete a user, remove its data and remove the user from the list
     *
     * @param user     to deleted user
     * @param position position of the user in the list
     */
    @Override
    public void onDeleteUser(User user, int position) {
        //Delete Settings
        Settings settings = user.getSettings();
        mSettingsDataSource.delete(settings);

        //Delete Completions
        List<Completion> completions = user.getCompletions();
        for (Completion completion : completions)
            completion.delete();

        //Delete Statistics
        List<Statistics> statistics = user.getStatistics();
        for (Statistics statistic : statistics)
            statistic.delete();

        //Delete User
        user.delete();

        mListAdapter.removeAt(position);
    }
}