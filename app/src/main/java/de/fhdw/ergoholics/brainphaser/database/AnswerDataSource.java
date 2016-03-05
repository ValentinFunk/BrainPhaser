package de.fhdw.ergoholics.brainphaser.database;

import java.util.List;

import de.fhdw.ergoholics.brainphaser.model.Answer;

/**
 * Created by Daniel Hoogen on 25/02/2016.
 *
 * Data Source class for custom access to answer table entries in the database
 */
public class AnswerDataSource {
    private static final AnswerDataSource instance = new AnswerDataSource();
    public static AnswerDataSource getInstance() {
        return instance;
    }

    /**
     * Return all Answer objects in the answer table
     * @return List object containing all Answer objects
     */
    public List<Answer> getAll() {
        return DaoManager.getSession().getAnswerDao().loadAll();
    }

    /**
     * Returns the Answer object with the given id
     * @param id answer id in the database
     * @return Answer object with the given id
     */
    public Answer getById(long id) {
        return DaoManager.getSession().getAnswerDao().load(id);
    }

    /**
     * Adds an Answer object to the database
     * @param answer answer to be created in the challenge table
     * @return id of the created object
     */
    public long create(Answer answer) {
        return DaoManager.getSession().getAnswerDao().insert(answer);
    }
}