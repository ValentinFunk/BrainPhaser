package de.fhdw.ergoholics.brainphasergenerator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;
import de.greenrobot.daogenerator.ToOne;

public class BrainphaserDaoGenerator {
    public static int DATABASE_VERSION = 7;

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(1, "de.fhdw.ergoholics.brainphaser.model");

        // Create entities
        Entity userEntity = createUserEntity(schema);
        Entity categoryEntity = createCategoryEntity(schema);
        Entity challengeEntity = createChallengeEntity(schema);
        Entity answerEntity = createAnswerEntity(schema);
        Entity completedEntity = createCompletedEntity(schema);
        Entity settingsEntity = createSettingsEntity(schema);
        Entity statisticsEntity = createStatisticsEntity(schema);

        // category HAS MANY challenge
        Property categoryId = challengeEntity.addLongProperty("categoryId").notNull().getProperty();
        ToMany categoryToChallenge = categoryEntity.addToMany(challengeEntity, categoryId);
        categoryToChallenge.setName("challenges");

        // challenge HAS MANY answer
        Property challengeIdAnswer = answerEntity.addLongProperty("challengeId").notNull().getProperty();
        ToMany challengeToAnswer = challengeEntity.addToMany(answerEntity, challengeIdAnswer);
        challengeToAnswer.setName("answers");

        // user HAS MANY completion
        Property userIdCompleted = completedEntity.addLongProperty("userId").notNull().getProperty();
        ToMany userToCompleted = userEntity.addToMany(completedEntity, userIdCompleted);
        userToCompleted.setName("completions");

        // completion TO ONE challenge
        Property challengeIdCompleted = completedEntity.addLongProperty("challengeId").notNull().getProperty();
        ToOne completedToChallenge = completedEntity.addToOne(challengeEntity, challengeIdCompleted);
        completedToChallenge.setName("challenge");

        // user HAS MANY statistics
        Property userIdStatistics = statisticsEntity.addLongProperty("userId").notNull().getProperty();
        ToMany userToStatistics = userEntity.addToMany(statisticsEntity, userIdStatistics);
        userToStatistics.setName("statistics");

        // statistics TO ONE challenge
        Property challengeIdStatistics = statisticsEntity.addLongProperty("challengeId").notNull().getProperty();
        ToOne completedToStatistics = statisticsEntity.addToOne(challengeEntity, challengeIdStatistics);
        completedToStatistics.setName("statistic");

        // user HAS ONE settings
        Property userIdSettings = userEntity.addLongProperty("settingsId").notNull().getProperty();
        ToOne userToSettings = userEntity.addToOne(settingsEntity, userIdSettings);
        userToSettings.setName("settings");

        new DaoGenerator().generateAll(schema, "../app/src/main/java/");
    }

    public static Entity createUserEntity(Schema schema) {
        Entity user = schema.addEntity("User");
        user.addIdProperty();
        user.addStringProperty("name").notNull();
        user.addStringProperty("avatar").notNull();
        user.setHasKeepSections(true);

        return user;
    }

    public static Entity createCategoryEntity(Schema schema) {
        Entity category = schema.addEntity("Category");
        category.addIdProperty();
        category.addStringProperty("title").notNull();
        category.addStringProperty("description").notNull();
        category.addStringProperty("image").notNull();
        return category;
    }

    public static Entity createChallengeEntity(Schema schema) {
        Entity challenge = schema.addEntity("Challenge");
        challenge.addIdProperty();
        challenge.addIntProperty("challengeType").notNull();
        challenge.addStringProperty("question").notNull();

        return challenge;
    }

    public static Entity createAnswerEntity(Schema schema) {
        Entity answer = schema.addEntity("Answer");
        answer.addIdProperty();
        answer.addStringProperty("text").notNull();
        answer.addBooleanProperty("answerCorrect");

        return answer;
    }

    public static Entity createCompletedEntity(Schema schema) {
        Entity completed = schema.addEntity("Completion");
        completed.addIdProperty();
        completed.addIntProperty("stage");
        completed.addDateProperty("lastCompleted");

        return completed;
    }

    public static Entity createStatisticsEntity(Schema schema) {
        Entity completed = schema.addEntity("Statistics");
        completed.addIdProperty();
        completed.addBooleanProperty("succeeded");
        completed.addDateProperty("time");

        return completed;
    }

    public static Entity createSettingsEntity(Schema schema) {
        Entity settings = schema.addEntity("Settings");

        settings.addIdProperty();
        settings.addDateProperty("timeBoxStage1");
        settings.addDateProperty("timeBoxStage2");
        settings.addDateProperty("timeBoxStage3");
        settings.addDateProperty("timeBoxStage4");
        settings.addDateProperty("timeBoxStage5");
        settings.addDateProperty("timeBoxStage6");

        return settings;
    }
}
