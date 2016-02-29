package de.fhdw.ergoholics.brainphaser.activities.UserSelection;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import de.fhdw.ergoholics.brainphaser.BrainPhaserApplication;
import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.activities.CategorySelect.SelectCategoryActivity;
import de.fhdw.ergoholics.brainphaser.activities.UserCreation.CreateUserActivity;
import de.fhdw.ergoholics.brainphaser.database.UserDataSource;
import de.fhdw.ergoholics.brainphaser.model.User;

/**
 * Created by Christian on 16.02.2016.
 *
 * Activity used to chose an existing user. Can load the create user activity
 * Persistent data: none
 * Parameters: none
 * Return: CURRENT_USER_ID
 */

public class UserSelectionActivity extends Activity implements UserAdapter.ResultListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_selection);

        //loading of the components
        RecyclerView userList =(RecyclerView) findViewById(R.id.userList);

        //load the users from the database
        List<User> allUsers = UserDataSource.getAll();

        //Adapter which sets all users into the list
        userList.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        userList.setLayoutManager(layoutManager);

        //Create the View
        UserAdapter listAdapter = new UserAdapter(allUsers, this);
        userList.setAdapter(listAdapter);

        /**
         * Is the add button selected finish this activity and load Create-User-Activity
         */
        FloatingActionButton btnAddUser = (FloatingActionButton) findViewById(R.id.addUser);
        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_INSERT, Uri.EMPTY, getApplicationContext(), CreateUserActivity.class));
                setResult(Activity.RESULT_OK);
                finish();
            }
        });

    }

    /**
     * Is a user selected finish this activity and load Challenge-Set-Activity
     * @param user
     */
    @Override
    public void onUserSelected(User user) {
        BrainPhaserApplication app = (BrainPhaserApplication)getApplication();
        app.switchUser(user);
        startActivity(new Intent(getApplicationContext(), SelectCategoryActivity.class));

        setResult(Activity.RESULT_OK);
        finish();
    }
}