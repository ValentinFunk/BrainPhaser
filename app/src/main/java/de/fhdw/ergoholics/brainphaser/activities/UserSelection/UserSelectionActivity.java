package de.fhdw.ergoholics.brainphaser.activities.UserSelection;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;
import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.activities.UserCreation.CreateUserActivity;
import de.fhdw.ergoholics.brainphaser.database.User;
import de.fhdw.ergoholics.brainphaser.database.UserDataSource;

/**
 * Created by Christian on 16.02.2016.
 *
 * Activity used to chose an existing user. Can load the create user activity
 * Persistent data: none
 * Parameters: none
 * Return: CURRENT_USER_ID
 */

public class UserSelectionActivity extends Activity implements UserAdapter.ResultListener {
    public final static String CURRENT_USER_ID = "USER_ID";
    //Components of the activity
    private RecyclerView mUserList;

    //list of all users
    private List<User> mAllUsers;
    //connection to the user table
    private UserDataSource mUserData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_selection);

        //loading of the components
        mUserList =(RecyclerView) findViewById(R.id.userList);
        //load the users from the database
        mUserData = new UserDataSource();
        mAllUsers = mUserData.getUsers();
        //if no users are available go to create user activity
        if(mAllUsers ==null || mAllUsers.size()<1){
            startActivity(new Intent(getApplicationContext(), CreateUserActivity.class));
        }
        //Adapter which sets all users into the list
        mUserList.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mUserList.setLayoutManager(layoutManager);

        //Create the View
        UserAdapter listAdapter = new UserAdapter(mAllUsers, this);
        mUserList.setAdapter(listAdapter);

    }

    /**
     * Called when a user has been selected.
     *
     * @param user the user that was selected
     */
    private void profileSelectionFinished(User user) {
        Intent resultData = new Intent();
        resultData.putExtra(CURRENT_USER_ID, user.getId());
        setResult(Activity.RESULT_OK, resultData);
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
        startActivity(new Intent(getApplicationContext(), CreateUserActivity.class));
    }
}