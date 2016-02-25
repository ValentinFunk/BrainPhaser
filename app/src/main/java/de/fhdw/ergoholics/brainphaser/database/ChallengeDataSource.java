package de.fhdw.ergoholics.brainphaser.database;

import java.util.List;

import de.fhdw.ergoholics.brainphaser.model.Category;
import de.fhdw.ergoholics.brainphaser.model.Challenge;

/**
 * Created by Daniel Hoogen on 25/02/2016.
 */
public class ChallengeDataSource {
    public static List<Challenge> getAll() {
        return DaoManager.getSession().getChallengeDao().loadAll();
    }

    public static Challenge getById(long id) {
        return DaoManager.getSession().getChallengeDao().load(id);
    }

    public static long create(Challenge challenge) {
        return DaoManager.getSession().getChallengeDao().insert(challenge);
    }
}
