package de.fhdw.ergoholics.brainphaser.activities.selectuser;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import de.fhdw.ergoholics.brainphaser.BrainPhaserApplication;
import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.activities.createuser.Avatars;
import de.fhdw.ergoholics.brainphaser.logic.UserManager;
import de.fhdw.ergoholics.brainphaser.model.User;

/**
 * Created by Christian Kost
 * <p/>
 * Adapter to load all users into a list. Contains a context menu to perform actions on the users
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    @Inject
    UserManager mUserManager;
    //Attributes
    private ResultListener mResultListener;
    private List<User> mUsers;
    private BrainPhaserApplication mApplication;

    /**
     * Constructor to setup the listener, inject components and get all users
     *
     * @param allUsers       list of all users
     * @param resultListener Listener of the activity
     * @param application    App
     */
    public UserAdapter(List<User> allUsers, ResultListener resultListener, BrainPhaserApplication application) {
        mUsers = allUsers;
        mResultListener = resultListener;
        mApplication = application;

        application.getComponent().inject(this);
    }

    /**
     * Get the user manager
     *
     * @return The user manager
     */
    public UserManager getUserManager() {
        return mUserManager;
    }

    /**
     * Creates the list item's view
     *
     * @param parent   To get the context
     * @param viewType Ignored
     * @return The ViewHolder
     */
    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater myInflater = LayoutInflater.from(parent.getContext());
        //Load the list template
        View customView = myInflater.inflate(R.layout.list_item_user, parent, false);
        return new UserViewHolder(customView, this);
    }

    /**
     * Binds list element of given position to the view
     *
     * @param holder   UserViewHolder
     * @param position Positon
     */
    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        //bind the user
        User user = mUsers.get(position);
        holder.bindUser(user);
    }

    /**
     * Get the size of the view
     *
     * @return Size of the user list
     */
    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    /**
     * Pass selection on to the ResultListener
     *
     * @param user Selected user
     */
    public void onUserSelected(User user) {
        mResultListener.onUserSelected(user);
    }

    /**
     * Removes a user from the list
     *
     * @param position User's position in the list
     */
    public void removeAt(int position) {
        mUsers.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mUsers.size());
    }

    /**
     * Interface for ClickListener on a user
     */
    public interface ResultListener {
        void onUserSelected(User user);

        void onEditUser(User user);

        void onDeleteUser(User user, int position);
    }

    /**
     * User View Holder holds the items in the UserList
     */
    public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, View.OnClickListener,
            MenuItem.OnMenuItemClickListener {

        //Attributes
        private TextView mUserText;
        private ImageView mUserImage;
        private UserAdapter mAdapter;

        /**
         * Constructor instantiates the text and images
         *
         * @param itemView Ignored
         * @param adapter  The UserAdapter
         */
        public UserViewHolder(View itemView, UserAdapter adapter) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);

            mUserText = (TextView) itemView.findViewById(R.id.userItemText);
            mUserImage = (ImageView) itemView.findViewById(R.id.userItemImage);
            mAdapter = adapter;
        }

        /**
         * Creates a context menu to delete and edit an user
         *
         * @param menu     The context menu which contains the different items
         * @param v        Ignored
         * @param menuInfo Ignored
         */
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v,
                                        ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem editItem = menu.add(mApplication.getString(R.string.user_context_menu_button_edit));
            editItem.setOnMenuItemClickListener(this);

            if (!mUsers.get(getPosition()).getId().equals(mUserManager.getCurrentUser().getId())) {
                MenuItem deleteItem = menu.add(mApplication.getString(R.string.user_context_menu_button_delete));
                deleteItem.setOnMenuItemClickListener(this);
            }
        }

        /**
         * Actually deletes or loads the edit screen for an user
         *
         * @param item Menu item, which was pressed
         * @return True
         */
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (item.getTitle().equals(mApplication.getString(R.string.user_context_menu_button_edit))) {
                mResultListener.onEditUser(mUsers.get(getPosition()));
            } else if (item.getTitle().equals(mApplication.getString(R.string.user_context_menu_button_delete))) {
                mResultListener.onDeleteUser(mUsers.get(getPosition()), getPosition());
            }

            return true;
        }

        /**
         * Bind the user to the View Holder and add an OnClickListener
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

        /**
         * Ignored
         *
         * @param v Ignored
         */
        @Override
        public void onClick(View v) {
        }
    }

}