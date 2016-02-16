package de.fhdw.ergoholics.brainphaser.database;

/**
 * Created by Daniel Hoogen on 16/02/2016.
 * This class is the nonpersistent data model of the categories
 */
public class Category {
    int mId;
    String mTitle;
    String mDescription;

    public Category(int id, String title, String description) {
        this.mId = id;
        this.mTitle = title;
        this.mDescription = description;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }
}
