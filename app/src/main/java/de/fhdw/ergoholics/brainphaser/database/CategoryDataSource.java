package de.fhdw.ergoholics.brainphaser.database;

import de.fhdw.ergoholics.brainphaser.model.Category;

import java.util.List;

/**
 * Created by funkv on 20.02.2016.
 */
public class CategoryDataSource {
    private static final CategoryDataSource instance = new CategoryDataSource();
    public static CategoryDataSource getInstance() {
        return instance;
    }

    public List<Category> getAll() {
        return DaoManager.getSession().getCategoryDao().loadAll();
    }

    public long create(Category category) {
        return DaoManager.getSession().getCategoryDao().insert(category);
    }

    public Category getById(long id) {
        return DaoManager.getSession().getCategoryDao().load(id);
    }

}
