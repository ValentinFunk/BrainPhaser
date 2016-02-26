package de.fhdw.ergoholics.brainphaser.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.fhdw.ergoholics.brainphaser.database.ChallengeDataSource;
import de.fhdw.ergoholics.brainphaser.database.CompletedDataSource;
import de.fhdw.ergoholics.brainphaser.database.DaoManager;
import de.fhdw.ergoholics.brainphaser.database.SettingsDataSource;
import de.fhdw.ergoholics.brainphaser.model.Category;
import de.fhdw.ergoholics.brainphaser.model.Challenge;
import de.fhdw.ergoholics.brainphaser.model.Completed;
import de.fhdw.ergoholics.brainphaser.model.Settings;
import de.fhdw.ergoholics.brainphaser.model.User;

/**
 * Created by Daniel Hoogen on 25/02/2016.
 */
public class DueChallengeLogic {
    public List<Long> getDueChallenges(User user, Category category) {
        //Create list that will be returned later
        List<Long> dueActivities = new ArrayList<>();

        //Get due challenges that have entries in the database
        //Create objects
        //Todo: replace following line if possible (= user.getSettings())
        Settings settings = SettingsDataSource.getByUserId(user.getId());
        if (settings==null) {
            settings = getDefaultSettings();
        }
        Date now = new Date();
        Date timebox;

        for (int stage = 1; stage<=6; stage++) {
            List<Completed> completedInStage = CompletedDataSource.getByUserAndStage(user, stage);

            timebox = getTimeboxByStage(settings, stage);

            for (Completed completed : completedInStage) {
                Date lastCompleted = completed.getTimeLastCompleted();

                if (now.getTime() - lastCompleted.getTime() >= timebox.getTime()) {
                    dueActivities.add(completed.getChallengeId());
                }
            }
        }

        //Todo: Create missing entries
        List<Challenge> notCompletedYet = ChallengeDataSource.getNotCompletedByUser(user);

        Date dateChallengesDue = new Date(now.getTime() - settings.getTimeBoxStage1().getTime());

        for (Challenge challenge : notCompletedYet)
        {
            Completed completed = new Completed(null, 1, dateChallengesDue, user.getId(), challenge.getId());
            CompletedDataSource.create(completed);
            dueActivities.add(challenge.getId());
        }

        return dueActivities;
    }

    private static Date getTimeboxByStage(Settings settings, int stage) {
        switch (stage) {
            case 1:
                return settings.getTimeBoxStage1();
            case 2:
                return settings.getTimeBoxStage2();
            case 3:
                return settings.getTimeBoxStage3();
            case 4:
                return settings.getTimeBoxStage4();
            case 5:
                return settings.getTimeBoxStage5();
            case 6:
                return settings.getTimeBoxStage6();
            default:
                return null;
        }
    }

    private static Settings getDefaultSettings() {
        Settings settings = new Settings();

        settings.setTimeBoxStage1(new Date(1000*60*5));         //5 minutes
        settings.setTimeBoxStage2(new Date(1000*60*60));        //1 hour
        settings.setTimeBoxStage3(new Date(1000*60*60*24));     //1 day
        settings.setTimeBoxStage4(new Date(1000*60*60*24*7));   //7 days
        settings.setTimeBoxStage5(new Date(1000*60*60*24*30));  //30 days
        settings.setTimeBoxStage6(new Date(1000*60*60*24*180)); //180 days

        return settings;
    }
}
