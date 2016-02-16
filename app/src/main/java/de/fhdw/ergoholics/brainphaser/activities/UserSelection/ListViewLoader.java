package de.fhdw.ergoholics.brainphaser.activities.UserSelection;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;
import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.database.User;
import de.fhdw.ergoholics.brainphaser.database.UserDataSource;

/**
 * Created by Christian on 16.02.2016.
 */
public class ListViewLoader extends ListActivity{

    private ListView userList;
    private User currentUser; //chosen user
    //list of all users
    private List<User> allUsers;
    //connection to the user table
    private UserDataSource userData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_selection);

        //loading of the ListView
        userList=(ListView) findViewById(R.id.userListView);

        userData = new UserDataSource();
        allUsers = userData.getUsers();
        //Adapter which sets allUsers into the lists
        ListAdapter listAdapter = new UserAdapter(this,allUsers);
        userList.setAdapter(listAdapter);

        userList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                            currentUser = allUsers.get(parent.getPositionForView(view));
                            Toast.makeText(ListViewLoader.this,currentUser.getName(), Toast.LENGTH_LONG).show();
                        }
                });
    }
}