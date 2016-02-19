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
import de.fhdw.ergoholics.brainphaser.database.User;

/**
 * Created by Christian on 16.02.2016.
 * Adapter and ViewHolder for the UserLists
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    public interface ResultListener {
        void onUserSelected(User user);
        void onUserAdd();
    }
    //User View holds the items
    public class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView mUserText;
        private ImageView mUserImage;
        private UserAdapter mAdapter;

        public UserViewHolder(View itemView, UserAdapter adapter) {
            super(itemView);

            mUserText = (TextView)itemView.findViewById(R.id.userItemText);
            mUserImage = (ImageView)itemView.findViewById(R.id.userItemImage);
            mAdapter = adapter;
        }

        //set text and image
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


    ResultListener mResultListener;
    List<User> mUsers;
    public UserAdapter(List<User> allUsers, ResultListener resultListener) {
        mUsers = allUsers;
        mResultListener = resultListener;
    }

    private static final int VIEWTYPE_ADDUSER = 1;
    private static final int VIEWTYPE_USER = 2;
    //User or Add Button TYPE
    @Override
    public int getItemViewType(int position) {
        if (position > mUsers.size()) {
            return VIEWTYPE_ADDUSER;
        }
        return VIEWTYPE_USER;
    }


    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater myInflater = LayoutInflater.from(parent.getContext());
        //Load the list template
        View customView = myInflater.inflate(R.layout.list_item_user, parent, false);
        return new UserViewHolder(customView, this);
    }

    //is the clicked item an user start onUserSelection otherwise start onAddUser
    public void onElementSelected(int position) {
        if (getItemViewType(position) == VIEWTYPE_USER) {
            mResultListener.onUserSelected(mUsers.get(position));
        } else if (getItemViewType(position) == VIEWTYPE_ADDUSER) {
            mResultListener.onUserAdd();
        }
    }

    //Create a list item
    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        if (getItemViewType(position) == VIEWTYPE_USER) {
            User user = mUsers.get(position);
            //bind the user
            holder.bindUser(user.getName(), Avatars.getAvatarResourceId(holder.itemView.getContext(), user.getAvatar()), position);
        } else {
            //bind the add
            String addUser = holder.itemView.getResources().getString(R.string.add_user);
            holder.bindUser(addUser, R.drawable.btn_add, position);
        }
    }

    //Length of View is all users + an add item
    @Override
    public int getItemCount() {
        return mUsers.size() + 1;
    }
}
