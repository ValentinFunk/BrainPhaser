package de.fhdw.ergoholics.brainphaser.activities.UserSelection;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import de.fhdw.ergoholics.brainphaser.BrainPhaserApplication;
import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.activities.UserCreation.Avatars;
import de.fhdw.ergoholics.brainphaser.logic.UserManager;
import de.fhdw.ergoholics.brainphaser.model.User;

import javax.inject.Inject;

/**
 * Created by Christian on 16.02.2016. Adapter and ViewHolder for the UserLists
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    /**
     * Interface for ClickListener
     */
    public interface ResultListener {
        void onUserSelected(User user);
    }

    /**
     * User View Holder holds the items in the UserList
     */
    public class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView mUserText;
        private ImageView mUserImage;
        private UserAdapter mAdapter;

        //Constructor
        public UserViewHolder(View itemView, UserAdapter adapter) {
            super(itemView);

            mUserText = (TextView) itemView.findViewById(R.id.userItemText);
            mUserImage = (ImageView) itemView.findViewById(R.id.userItemImage);
            mAdapter = adapter;
        }

        /**
         * Bind the item to the View Holder and add an OnClickListener
         *
         * @param user User to bind to the view
         */
        public void bindUser(final User user) {
            String username = user.getName();
            int avatarId = Avatars.getAvatarResourceId(itemView.getContext(), user.getAvatar());

            mUserText.setText(username);
            mUserImage.setImageResource(avatarId);

            // Highlight currently logged in user
            if (user.getId().equals(mAdapter.getUserManager().getCurrentUser().getId())) {
                mUserText.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.colorAccent));
            } else {
                int[] attrs = {android.R.attr.textColor};
                TypedArray ta = itemView.getContext().obtainStyledAttributes(android.R.style.TextAppearance_Large, attrs);
                mUserText.setTextColor(ta.getColor(0, Color.BLACK));

                ta.recycle();
            }

            //set click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAdapter.onUserSelected(user);
                }
            });
        }
    }


    private ResultListener mResultListener;
    private List<User> mUsers;

    @Inject UserManager mUserManager;

    public UserManager getUserManager( ) {
        return mUserManager;
    }

    public UserAdapter(List<User> allUsers, ResultListener resultListener, BrainPhaserApplication application) {
        mUsers = allUsers;
        mResultListener = resultListener;

        application.getComponent().inject(this);
    }

    // Creates the list item's view
    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater myInflater = LayoutInflater.from(parent.getContext());
        //Load the list template
        View customView = myInflater.inflate(R.layout.list_item_user, parent, false);
        return new UserViewHolder(customView, this);
    }

    /**
     * Pass selection on to the listener
     *
     * @param user selected user
     */
    public void onUserSelected(User user) {
        mResultListener.onUserSelected(user);
    }


    // Binds list element of given position to the view
    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        //bind the user
        User user = mUsers.get(position);
        holder.bindUser(user);
    }

    // Amount of items = amount of users
    @Override
    public int getItemCount() {
        return mUsers.size();
    }
}