package de.fhdw.ergoholics.brainphaser.database;

import java.util.List;

import de.fhdw.ergoholics.brainphaser.model.Answer;

/**
 * Created by Daniel Hoogen on 25/02/2016.
 */
public class AnswerDataSource {
    public static List<Answer> getAll() {
        return DaoManager.getSession().getAnswerDao().loadAll();
    }

    public static Answer getById(long id) {
        return DaoManager.getSession().getAnswerDao().load(id);
    }

    public static long create(Answer answer) {
        return DaoManager.getSession().getAnswerDao().insert(answer);
    }
}