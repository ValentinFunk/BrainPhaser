package de.fhdw.ergoholics.brainphaser.activities.UserSelection;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import de.fhdw.ergoholics.brainphaser.BrainPhaserApplication;
import de.fhdw.ergoholics.brainphaser.BrainPhaserComponent;
import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.activities.BrainPhaserActivity;
import de.fhdw.ergoholics.brainphaser.activities.UserCreation.CreateUserActivity;
import de.fhdw.ergoholics.brainphaser.activities.main.MainActivity;
import de.fhdw.ergoholics.brainphaser.database.UserDataSource;
import de.fhdw.ergoholics.brainphaser.logic.UserManager;
import de.fhdw.ergoholics.brainphaser.model.User;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Christian on 16.02.2016.
 *
 * Activity used to chose an existing user. Can load the create user activity
 * Persistent data: none
 * Parameters: none
 */

public class UserSelectionActivity extends BrainPhaserActivity implements UserAdapter.ResultListener {
    @Inject UserManager mUserManager;
    @Inject UserDataSource mUserDataSource;

    @Override
    protected void injectComponent(BrainPhaserComponent component) {
        component.inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_selection);

        //loading of the components
        RecyclerView userList = (RecyclerView) findViewById(R.id.userList);
        userList.setHasFixedSize(true);
        registerForContextMenu(userList);

        //load the users from the database
        List<User> allUsers = mUserDataSource.getAll();

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        userList.setLayoutManager(layoutManager);

        //Create the View
        //Adapter which sets all users into the list
        UserAdapter listAdapter = new UserAdapter(allUsers, this, (BrainPhaserApplication) getApplication());
        userList.setAdapter(listAdapter);

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

    /*
     * Used to track when the create user activity has finished. If it was cancelled, do nothing,
     * else finish this activity (as the user is automatically logged in).
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            setResult(Activity.RESULT_OK);
            finish();
        }
    }

    /**
     * When a user has been selected, finish this activity and load learning activity
     * @param user
     */
    @Override
    public void onUserSelected(User user) {
        mUserManager.switchUser(user);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);

        setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    public void onEditUser(User user) {
        Intent intent = new Intent(Intent.ACTION_EDIT, Uri.EMPTY, getApplicationContext(), CreateUserActivity.class);
        intent.putExtra(CreateUserActivity.KEY_USER_ID, user.getId());
        startActivityForResult(intent, 0);
    }

    @Override
    public void onDeleteUser(User user) {
        //Todo
    }
}