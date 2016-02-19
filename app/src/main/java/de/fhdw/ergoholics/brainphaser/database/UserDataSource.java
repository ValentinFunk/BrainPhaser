package de.fhdw.ergoholics.brainphaser.database;

import android.content.ContentValues;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chris on 2/15/2016.
 * Class to write, view the user table
 * Inherits from DataSource
 * Persistent: User-Data
 */
public class UserDataSource extends DataSource {

    //Array of the columns
    private String[] columns = {
            DatabaseStatics.USER_COL_1,
            DatabaseStatics.USER_COL_2,
            DatabaseStatics.USER_COL_3
    };

    /**
     * Create user in database
     *
     * @param user User to create in Database
     * @return true was successful, false not
     */
    public boolean createUser(User user){
        ContentValues values = new ContentValues();
        values.put(DatabaseStatics.USER_COL_2,user.getName());
        values.put(DatabaseStatics.USER_COL_3, user.getAvatar());
        long rowId = mDatabase.insert(DatabaseStatics.USER_TABLE_NAME, null, values);
        if(rowId!=-1) {
            user.setId((int) rowId);
            return true;
        }
        return false;
    }

    /**
     * Delete user in database
     *
     * @param user User to create in Database
     * @return true was successful, false not
     */
    public boolean deleteUser(User user){
        long rowId = mDatabase.delete(DatabaseStatics.USER_TABLE_NAME, DatabaseStatics.USER_COL_1 + "=" + user.getId(), null);
        if(rowId!=-1) {
            return true;
        }
        return false;
    }

    /**
     * Reads one user from the database
     * @param id ID of the user
     * @return the user in the database
     */
    public User getUser(int id){
        User user;
        Cursor cursor=mDatabase.query(DatabaseStatics.USER_TABLE_NAME, columns, DatabaseStatics.USER_COL_1 + "=" + id, null, null, null, null);
        user = cursorToUser(cursor);
        cursor.close();
        return user;
    }

    /**
     * Reads one user from the database
     * @param name Name of the user
     * @return the user in the database
     */
    public User getUser(String name){
        User user;
        Cursor cursor=mDatabase.query(DatabaseStatics.USER_TABLE_NAME,columns,DatabaseStatics.USER_COL_2 + "= ?" , new String[]{name},null,null,null);
        user = cursorToUser(cursor);
        cursor.close();
        return user;
    }

    /**
     * Searches for users from the database
     * @param param Parameter of the user to be searched
     * @param column Column name of the param
     * @return the user in the database
     */
    public List<User> getUser(String param,String column){
        List<User> allUsers = new ArrayList<User>();
        //cursor on the user table
        Cursor cursor=mDatabase.query(DatabaseStatics.USER_TABLE_NAME,columns,column + "= ?",new String[]{param},null,null,null);
        //reset cursor
        cursor.moveToFirst();
        User user;
        //Read all rows in the user table
        while(!cursor.isAfterLast()) {
            user = cursorToUser(cursor);
            allUsers.add(user);
            cursor.moveToNext();
        }
        cursor.close();
        return allUsers;
    }
    /**
     * Reads all users in the database
     * @return ArrayList of all users
     */
    public List<User> getUsers(){

        List<User> allUsers = new ArrayList<User>();
        //cursor on the user table
        Cursor cursor=mDatabase.query(DatabaseStatics.USER_TABLE_NAME,columns,null,null,null,null,null);
        //reset cursor
        cursor.moveToFirst();
        User user;
        //Read all rows in the user table
        while(!cursor.isAfterLast()) {
            user = cursorToUser(cursor);
            allUsers.add(user);
            cursor.moveToNext();
        }
        cursor.close();
        return allUsers;
    }

    /**
     * Return cursors position as user
     * @param cursor Cursor on to the row on user table
     * @return User from the database on the cursors position
     */
    private User cursorToUser(Cursor cursor){
        //get the column id
        int idId = cursor.getColumnIndex(DatabaseStatics.USER_COL_1);
        int idName = cursor.getColumnIndex(DatabaseStatics.USER_COL_2);
        int idAvatar = cursor.getColumnIndex(DatabaseStatics.USER_COL_3);
        //get the values of the columns
        String name = cursor.getString(idName);
        int id=cursor.getInt(idId);
        String avatar = cursor.getString(idAvatar);
        return new User(id,name,avatar);
    }

    /**
     * Updates the updated user object in the database
     *
     * @param user the already updated user object
     * @return true was successful, false not
     */
    public boolean updateUser(User user){
        ContentValues values=new ContentValues();
        values.put(DatabaseStatics.USER_COL_2,user.getName());
        values.put(DatabaseStatics.USER_COL_3, user.getAvatar());
        long rowNo = mDatabase.update(DatabaseStatics.USER_TABLE_NAME,values,DatabaseStatics.USER_COL_1 + "=" + user.getId(),null);
        if(rowNo!=0) {
            return true;
        }
        return false;
    }
}