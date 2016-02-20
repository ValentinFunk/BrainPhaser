package de.fhdw.ergoholics.brainphaser.activities.UserSelection;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import de.fhdw.ergoholics.brainphaser.BrainPhaserApplication;
import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.activities.UserCreation.CreateUserActivity;
import de.fhdw.ergoholics.brainphaser.database.UserDatasource;
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
        List<User> allUsers = UserDatasource.getAll();

        //if no users are available go to create user activity
        if(allUsers ==null || allUsers.size()<1){
            startActivity(new Intent(getApplicationContext(), CreateUserActivity.class));
        }
        //Adapter which sets all users into the list
        userList.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        userList.setLayoutManager(layoutManager);

        //Create the View
        UserAdapter listAdapter = new UserAdapter(allUsers, this);
        userList.setAdapter(listAdapter);

    }

    /**
     * Called when a user has been selected.
     *
     * @param user the user that was selected
     */
    private void profileSelectionFinished(User user) {
        BrainPhaserApplication app = (BrainPhaserApplication)getApplication();
        app.switchUser(user);

        setResult(Activity.RESULT_OK);
        finish();
    }

    /**
     * Is a user selected finish this activity and load Challenge-Set-Activity
     * @param user
     */
    @Override
    public void onUserSelected(User user) {
        //Chosen user will be intent
        profileSelectionFinished(user);
    }

    /**
     * Is the add button selected finish this activity and load Create-User-Activity
     */
    @Override
    public void onUserAdd() {
        startActivity(new Intent(Intent.ACTION_INSERT, Uri.EMPTY, getApplicationContext(), CreateUserActivity.class));
    }
}