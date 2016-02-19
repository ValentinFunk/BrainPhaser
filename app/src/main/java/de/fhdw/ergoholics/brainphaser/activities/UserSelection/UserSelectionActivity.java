package de.fhdw.ergoholics.brainphaser.activities.UserSelection;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
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

public class UserSelectionActivity extends Activity {
    public final static String CURRENT_USER_ID = "USER_ID";
    //Components of the activity
    private ListView mUserList;
    private ImageButton mCreateUserBtn;

    //list of all users
    private List<User> mAllUsers;
    //connection to the user table
    private UserDataSource mUserData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_selection);

        //loading of the components
        mUserList =(ListView) findViewById(R.id.userList);
        mCreateUserBtn =(ImageButton) findViewById(R.id.createUserBtn);
        //load the users from the database
        mUserData = new UserDataSource();
        mAllUsers = mUserData.getUsers();
        //if no users are available go to create user activity
        if(mAllUsers ==null || mAllUsers.size()<1){
            startActivity(new Intent(getApplicationContext(), CreateUserActivity.class));
        }
        //Adapter which sets all users into the list
        ListAdapter listAdapter = new UserAdapter(this, mAllUsers);
        mUserList.setAdapter(listAdapter);

        //If a user is clicked, he will log in
        mUserList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        User CurrentUser; //chosen user
                        CurrentUser = mAllUsers.get(parent.getPositionForView(view));
                        //TODO User übergeben oder global speichern?   Challenge Set Activity load
                        Toast.makeText(getBaseContext(), CurrentUser.getName(), Toast.LENGTH_LONG).show();
                        //Chosen user will be intent
                        //profileSelectionFinished(mAllUsers.get(parent.getPositionForView(view)));

                    }
                });

        //Create a user -> load the Create the User Activity
        mCreateUserBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(), CreateUserActivity.class));
                    }
                }
        );
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
}