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
    public static final long CATEGORY_ID_ALL = -1l;

    //Attributes
    private DaoSession mDaoSession;

    @Inject
    public CategoryDataSource(DaoSession session) {
        mDaoSession = session;
    }

    public List<Category> getAll() {
        return mDaoSession.getCategoryDao().loadAll();
    }

    public long create(Category category) {
        return mDaoSession.getCategoryDao().insert(category);
    }

    public Category getById(long id) {
        return mDaoSession.getCategoryDao().load(id);
    }

}
