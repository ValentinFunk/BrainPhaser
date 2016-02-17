package de.fhdw.ergoholics.brainphaser.database;

/**
 * Created by Chris on 2/15/2016.
 * User class
 */
public class User {

    private int mId;
    private String mName;
    private String mAvatar;

    //Constructors
    public User(int id, String name, String avatar) {
        this.mId = id;
        this.mName = name;
        this.mAvatar = avatar;
    }

    public User(String name, String avatar) {
        this.mId = -1;
        this.mName = name;
        this.mAvatar = avatar;
    }
    //Get and Set methods
    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getAvatar() {
        return mAvatar;
    }

    public void setAvatar(String avatar) {
        this.mAvatar = avatar;
    }




}

