package de.fhdw.ergoholics.brainphaser.database;

/**
 * Created by Daniel Hoogen on 16/02/2016.
 * This class is the nonpersistent data model of the challenge sets
 */
public class Category {
    int mId;
    String mTitle;
    String mDescription;

    public Category(int mId, String mTitle, String mDescription) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.mDescription = mDescription;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }
}
