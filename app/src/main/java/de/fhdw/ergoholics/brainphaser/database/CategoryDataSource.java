package de.fhdw.ergoholics.brainphaser.database;

import de.fhdw.ergoholics.brainphaser.model.Category;
import de.fhdw.ergoholics.brainphaser.model.DaoSession;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by funkv on 20.02.2016.
 */
@Singleton
public class CategoryDataSource {
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
