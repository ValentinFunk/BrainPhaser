package de.fhdw.ergoholics.brainphaser.database;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.fhdw.ergoholics.brainphaser.model.Category;
import de.fhdw.ergoholics.brainphaser.model.Challenge;
import de.fhdw.ergoholics.brainphaser.model.CompletionDao;
import de.fhdw.ergoholics.brainphaser.model.Settings;
import de.fhdw.ergoholics.brainphaser.model.User;
import de.greenrobot.dao.query.Join;
import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by Daniel Hoogen on 25/02/2016.
 */
public class ChallengeDataSource {
    public static List<Challenge> getAll() {
        return DaoManager.getSession().getChallengeDao().loadAll();
    }

    public enum ChallengeType{
        MULTIPLE_CHOICE, TEXT
    }

    public static Challenge getById(long id) {
        return DaoManager.getSession().getChallengeDao().load(id);
    }

    public static long create(Challenge challenge) {
        return DaoManager.getSession().getChallengeDao().insert(challenge);
    }
}
