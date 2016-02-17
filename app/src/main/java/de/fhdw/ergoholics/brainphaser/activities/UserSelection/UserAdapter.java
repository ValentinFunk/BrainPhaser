package de.fhdw.ergoholics.brainphaser.activities.UserSelection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.activities.UserCreation.Avatars;
import de.fhdw.ergoholics.brainphaser.database.User;

/**
 * Created by Christian on 16.02.2016.
 * Adapter for the List-View
 */
public class UserAdapter extends ArrayAdapter<User> {

    public UserAdapter(Context context, List<User> allUsers) {
        super(context, R.layout.list_item_user ,allUsers);
    }
    @Override
    public View getView(int position, View contentView, ViewGroup parent){

        LayoutInflater myInflater = LayoutInflater.from(getContext());
        View customView = myInflater.inflate(R.layout.list_item_user, parent, false);
        //get the current user in the list
        User user = getItem(position);

        //get the List-Item-Compoments
        TextView userText = (TextView)customView.findViewById(R.id.userItemText);
        ImageView userImage = (ImageView)customView.findViewById(R.id.userItemImage);
        //set the user-data to the list-item-compoments
        userText.setText(user.getName());
        userImage.setImageResource(Avatars.getAvatarResourceId(getContext(), user.getAvatar()));

        return customView;
    }
}
