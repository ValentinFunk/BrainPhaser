package de.fhdw.ergoholics.brainphaser.database;

import de.fhdw.ergoholics.brainphaser.model.Category;

import java.util.List;

/**
 * Created by funkv on 20.02.2016.
 */
public class CategoryDataSource {
    public static final long CATEGORY_ID_ALL = -1l;

    public static List<Category> getAll() {
        return DaoManager.getSession().getCategoryDao().loadAll();
    }

    public static long create(Category category) {
        return DaoManager.getSession().getCategoryDao().insert(category);
    }

    public static Category getById(long id) {
        return DaoManager.getSession().getCategoryDao().load(id);
    }

}
