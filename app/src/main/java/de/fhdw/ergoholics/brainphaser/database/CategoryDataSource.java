package de.fhdw.ergoholics.brainphaser.database;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel Hoogen on 16/02/2016.
 * Class for writing and reading Category objects to/from the category table
 * Inherits from DataSource
 * Persistent: Category-Data
 */
public class CategoryDataSource extends DataSource {

    //Array of the columns of the category table
    private String[] columns = {
            DatabaseStatics.CATEGORY_COL_1,
            DatabaseStatics.CATEGORY_COL_2,
            DatabaseStatics.CATEGORY_COL_3
    };

    /**
     * Create a category in the database
     *
     * @param category Category to be created in the database
     * @return true if the insert was successful, false if not
     */
    public boolean createCategory(Category category){
        ContentValues values = new ContentValues();
        values.put(DatabaseStatics.CATEGORY_COL_2, category.getTitle());
        values.put(DatabaseStatics.CATEGORY_COL_3, category.getDescription());
        long rowId = mDatabase.insert(DatabaseStatics.CATEGORY_TABLE_NAME, null, values);
        if(rowId!=-1) {
            category.setId((int) rowId);
            return true;
        }
        return false;
    }

    /**
     * Delete category from database
     *
     * @param category Category to be removed from database
     * @return true if the delete was successful, false if not
     */
    public boolean deleteCategory(Category category){
        long rowId = mDatabase.delete(DatabaseStatics.CATEGORY_TABLE_NAME, DatabaseStatics.CATEGORY_COL_1 + "=" + category.getId(), null);
        if(rowId!=-1) {
            return true;
        }
        return false;
    }

    /**
     * Reads the category with the given id from the database
     *
     * @param id ID of the category
     * @return the category in the database
     */
    public Category getCategory(int id){
        Category category;
        Cursor cursor=mDatabase.query(DatabaseStatics.CATEGORY_TABLE_NAME,columns,DatabaseStatics.CATEGORY_COL_1 + "=" + id,null,null,null,null);
        category = cursorToCategory(cursor);
        cursor.close();
        return category;
    }

    /**
     * Reads a category with the given title from the database
     *
     * @param title of the category
     * @return the category in the database
     */
    public Category getCategory(String title){
        Category category;
        Cursor cursor = mDatabase.query(DatabaseStatics.CATEGORY_TABLE_NAME,columns,DatabaseStatics.CATEGORY_COL_2 + "=" + title,null,null,null,null);
        category = cursorToCategory(cursor);
        cursor.close();
        return category;
    }

    /**
     * Builds and returns Category objects from the rows in the database
     *
     * @return ArrayList of all categorys
     */

    public List<Category> getCategorys(){

        //Create empty list
        List<Category> allCategories = new ArrayList<Category>();

        //Create cursor on the category table
        Cursor cursor = mDatabase.query(DatabaseStatics.CATEGORY_TABLE_NAME,columns,null,null,null,null,null);

        //Reset cursor
        cursor.moveToFirst();
        Category category;

        //Build all category objects and add them to the list
        while(!cursor.isAfterLast()) {
            category = cursorToCategory(cursor);
            allCategories.add(category);
            cursor.moveToNext();
        }
        cursor.close();
        return allCategories;
    }

    /**
     * Return the cursor's position as category
     *
     * @param cursor Cursor that points on a row in the category table
     * @return Category from the database on the cursors position
     */
    private Category cursorToCategory(Cursor cursor){
        //retrieve the column IDs
        int idId = cursor.getColumnIndex(DatabaseStatics.CATEGORY_COL_1);
        int idTitle = cursor.getColumnIndex(DatabaseStatics.CATEGORY_COL_2);
        int idDescription = cursor.getColumnIndex(DatabaseStatics.CATEGORY_COL_3);

        //retrieve the column values
        int id = cursor.getInt(idId);
        String title = cursor.getString(idTitle);
        String description = cursor.getString(idDescription);

        return new Category(id,title,description);
    }

    /**
     * Updates the category with the id of the given Category object
     *
     * @param category the Category object to be updated
     * @return true if the update was successful, false if not
     */
    public boolean updateCategory(Category category){
        ContentValues values=new ContentValues();
        values.put(DatabaseStatics.CATEGORY_COL_2,category.getTitle());
        values.put(DatabaseStatics.CATEGORY_COL_3, category.getDescription());
        long rowNo = mDatabase.update(DatabaseStatics.CATEGORY_TABLE_NAME,values,DatabaseStatics.CATEGORY_COL_1 + "=" + category.getId(),null);
        if(rowNo!=0) {
            return true;
        }
        return false;
    }
}
