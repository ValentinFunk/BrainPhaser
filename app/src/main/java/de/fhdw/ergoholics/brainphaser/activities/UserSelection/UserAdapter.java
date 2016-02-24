package de.fhdw.ergoholics.brainphaser.activities.UserSelection;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.activities.UserCreation.Avatars;
import de.fhdw.ergoholics.brainphaser.model.User;

/**
 * Created by Christian on 16.02.2016.
 * Adapter and ViewHolder for the UserLists
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    /**
     * Interface for ClickListener
     */
    public interface ResultListener {
        void onUserSelected(User user);
    }
    /**
     *User View Holder holds the items in the UserList
     */
    public class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView mUserText;
        private ImageView mUserImage;
        private UserAdapter mAdapter;

        //Constructor
        public UserViewHolder(View itemView, UserAdapter adapter) {
            super(itemView);

            mUserText = (TextView)itemView.findViewById(R.id.userItemText);
            mUserImage = (ImageView)itemView.findViewById(R.id.userItemImage);
            mAdapter = adapter;
        }

        /**
         * Create the item in the View Holder and add an OnClickListener
         * @param username Username
         * @param avatarId AvatarID
         * @param position Position of the item
         */
        public void bindUser(String username, int avatarId, final int position) {
            mUserText.setText(username);
            mUserImage.setImageResource(avatarId);
            //set click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAdapter.onElementSelected(position);
                }
            });
        }
    }


    private ResultListener mResultListener;
    private List<User> mUsers;

    public UserAdapter(List<User> allUsers, ResultListener resultListener) {
        mUsers = allUsers;
        mResultListener = resultListener;
    }

    //User or Add Button TYPE


    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater myInflater = LayoutInflater.from(parent.getContext());
        //Load the list template
        View customView = myInflater.inflate(R.layout.list_item_user, parent, false);
        return new UserViewHolder(customView, this);
    }

    /**
     * Starts the interface. If the clicked item is an user, start onUserSelection otherwise start onAddUser.
     * @param position item's position
     */
    public void onElementSelected(int position) {
            mResultListener.onUserSelected(mUsers.get(position));
        }


    //Create a list item
    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        //bind the user
        User user = mUsers.get(position);
        holder.bindUser(user.getName(), Avatars.getAvatarResourceId(holder.itemView.getContext(), user.getAvatar()), position);
    }

    //Length of View is all users
    @Override
    public int getItemCount() {
        return mUsers.size();
    }
}