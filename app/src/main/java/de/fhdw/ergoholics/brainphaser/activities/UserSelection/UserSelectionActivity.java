package de.fhdw.ergoholics.brainphaser.activities.UserSelection;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
 * Return: CURRENT_USER
 */

public class UserSelectionActivity extends Activity {

    //Components of the activity
    private ListView userList;
    private Button createUserBtn;
    private User currentUser; //chosen user
    //list of all users
    private List<User> allUsers;
    //connection to the user table
    private UserDataSource userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_selection);

        //loading of the components
        userList=(ListView) findViewById(R.id.userList);
        createUserBtn=(Button) findViewById(R.id.createUserBtn);
        //load the users from the database
/*
        userData = new UserDataSource();
        allUsers = userData.getUsers();*/
        if(allUsers==null ||allUsers.size()<1){
        //TODO: Singleton wirft Null-Exception. Was ist wenn 0 User vorhanden sind? In createUser
            //startActivity(new Intent(getApplicationContext(), CreateUserActivity.class))
            allUsers = new ArrayList<User>();
            allUsers.add(new User("Lorem", "astronaut"));

            allUsers.add(new User("dolor", "bomberman"));
            allUsers.add(new User("sit", "bomberman2"));
            allUsers.add(new User("amet","bride_hispanic_material"));
            allUsers.add(new User("Lorem", "cashier_woman"));
            allUsers.add(new User("dolor","call_center_operator_man"));
            allUsers.add(new User("amet","budist"));
            allUsers.add(new User("dolor","anonymous"));
            allUsers.add(new User("sit", "cook2_man"));

        }
        //Adapter which sets all users into the list
        ListAdapter listAdapter = new UserAdapter(this,allUsers);
        userList.setAdapter(listAdapter);

        //If a user is clicked, he will log in
        userList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //TODO User übergeben oder global speichern?
                        currentUser = allUsers.get(parent.getPositionForView(view));
                        //TODO Challenge Set Activity load
                        Toast.makeText(getBaseContext(), currentUser.getName(), Toast.LENGTH_LONG).show();
                    }
                });

        //Create a user -> load the Create the User Activity
        createUserBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(),CreateUserActivity.class));
                    }
                }
        );
    }
}