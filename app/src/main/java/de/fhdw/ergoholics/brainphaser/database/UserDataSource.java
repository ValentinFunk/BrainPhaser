package de.fhdw.ergoholics.brainphaser.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chris on 2/15/2016.
 */
public class UserDataSource {

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    private String[] columns = {
            DatabaseHelper.USER_COL_1,
            DatabaseHelper.USER_COL_2,
            DatabaseHelper.USER_COL_3
    };

    public UserDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }
    //TODO: IN Parent Klasse auslagern
    /**
     * Open the database connection
     */
    public void open(){
        database=dbHelper.getWritableDatabase();
    }

    /**
     * Close the database connection
     */
    public void close(){
        dbHelper.close();
    }

    /**
     * Create user in database
     *
     * @param user User to create in Database
     * @return the row ID of the newly inserted row, or -1 if an error occurred
     */
    public long createUser(User user){
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.USER_COL_2,user.getName());
        values.put(DatabaseHelper.USER_COL_3, user.getAvatar());
        return database.insert(DatabaseHelper.USER_TABLE_NAME, null, values);
    }

    /**
     * Delete user in database
     *
     * @param user User to create in Database
     * @return the number of rows affected if a whereClause is passed in, 0
     *         otherwise. To remove all rows and get a count pass "1" as the
     *         whereClause.he row ID of the newly inserted row, or -1 if an error occurred
     */
    public long deleteUser(User user){
        return database.delete(DatabaseHelper.USER_TABLE_NAME, DatabaseHelper.USER_COL_1 + "=" + user.getId(), null);
    }

    /**
     * Reads one user from the database
     * @param id ID of the user
     * @return the user in the database
     */
    public User getUser(int id){
        User user;
        Cursor cursor=database.query(DatabaseHelper.USER_TABLE_NAME,columns,DatabaseHelper.USER_COL_1 + "=" + id,null,null,null,null);
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
        Cursor cursor=database.query(DatabaseHelper.USER_TABLE_NAME,columns,DatabaseHelper.USER_COL_2 + "=" + name,null,null,null,null);
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
        Cursor cursor=database.query(DatabaseHelper.USER_TABLE_NAME,columns,column + "=" + param,null,null,null,null);
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
        Cursor cursor=database.query(DatabaseHelper.USER_TABLE_NAME,columns,null,null,null,null,null);
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
     * @param cursor Curosor on to the table
     * @return User from the database on the cursors position
     */
    private User cursorToUser(Cursor cursor){
        //get the column id
        int idId = cursor.getColumnIndex(DatabaseHelper.USER_COL_1);
        int idName = cursor.getColumnIndex(DatabaseHelper.USER_COL_2);
        int idAvatar = cursor.getColumnIndex(DatabaseHelper.USER_COL_3);
        //get the values of the columns
        String name = cursor.getString(idName);
        int id=cursor.getInt(idId);
        String avatar = cursor.getString(idAvatar);
        return new User(id,name,avatar);
    }

    /**
     * Updates the updated user object in the database
     *
     * @param user the updated user object
     * @return the number of rows affected
     */
    public long updateUser(User user){
        ContentValues values=new ContentValues();
        values.put(DatabaseHelper.USER_COL_2,user.getName());
        values.put(DatabaseHelper.USER_COL_3, user.getAvatar());
        return database.update(DatabaseHelper.USER_TABLE_NAME,values,DatabaseHelper.USER_COL_1 + "=" + user.getId(),null);
    }
}
