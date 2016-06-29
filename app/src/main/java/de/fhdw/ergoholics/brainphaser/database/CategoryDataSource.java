package de.fhdw.ergoholics.brainphaser.database;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.fhdw.ergoholics.brainphaser.model.Category;
import de.fhdw.ergoholics.brainphaser.model.DaoSession;

/**
 * Created by funkv on 20.02.2016.
 */
@Singleton
public class CategoryDataSource {
    //Constants
    public static final long CATEGORY_ID_ALL = -1L;

    //Attributes
    private DaoSession mDaoSession;


    /**
     * Constructor defines the daosession
     *
     * @param session the DaoSession
     */
    @Inject
    public CategoryDataSource(DaoSession session) {
        mDaoSession = session;
    }

    /**
     * Gets all categories from the database
     *
     * @return List of all categories
     */
    public List<Category> getAll() {
        return mDaoSession.getCategoryDao().loadAll();
    }

    /**
     * Creates a new category
     *
     * @param category category to create
     * @return id of the created category
     */
    public long create(Category category) {
        return mDaoSession.getCategoryDao().insert(category);
    }

    /**
     * Gets a category by its id
     *
     * @param id Id of the category
     * @return Category
     */
    public Category getById(long id) {
        return mDaoSession.getCategoryDao().load(id);
    }

}
