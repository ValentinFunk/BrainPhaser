package de.fhdw.ergoholics.brainphaser.database;

import de.fhdw.ergoholics.brainphaser.model.Challenge;

/**
 * Created by Daniel on 25/02/2016.
 */
public class ChallengeDataSource {
    public static Challenge getById(long id) {
        return DaoManager.getSession().getChallengeDao().load(id);
    }
}
