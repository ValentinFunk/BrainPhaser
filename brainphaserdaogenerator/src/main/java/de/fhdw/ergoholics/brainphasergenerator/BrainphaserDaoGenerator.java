package de.fhdw.ergoholics.brainphasergenerator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class BrainphaserDaoGenerator {
    public static int DATABASE_VERSION = 1;
    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(1, "de.fhdw.ergoholics.brainphaser.model");

        // Create entities
        Entity userEntity = createUserEntity(schema);
        Entity categoryEntity = createCategoryEntity(schema);
        Entity challengeEntity = createChallengeEntity(schema);
        Entity answerEntity = createAnswerEntity(schema);
        Entity completedEntity = createCompletedEntity(schema);

        // category HAS MANY challenge
        Property categoryId = challengeEntity.addLongProperty("categoryId").notNull().getProperty();
        ToMany categoryToChallenge = categoryEntity.addToMany(challengeEntity, categoryId);
        categoryToChallenge.setName("challenges");

        // challenge HAS MANY answer
        Property challengeIdAnswer = answerEntity.addLongProperty("challengeId").notNull().getProperty();
        ToMany challengeToAnswer = challengeEntity.addToMany(answerEntity, challengeIdAnswer);
        categoryToChallenge.setName("answers");

        // Todo: check if correct
        // completed HAS MANY challenge
        Property challengeIdCompleted = completedEntity.addLongProperty("challengeId").notNull().getProperty();
        ToMany completedToChallenge = challengeEntity.addToMany(challengeEntity, challengeIdCompleted);
        categoryToChallenge.setName("userCompletions");

        // Todo: check if correct
        // completed HAS MANY user
        Property userId = completedEntity.addLongProperty("userId").notNull().getProperty();
        ToMany completedToUser = userEntity.addToMany(userEntity, userId);
        categoryToChallenge.setName("userCompletions");

        new DaoGenerator().generateAll(schema, "../app/src/main/java/");
    }

    public static Entity createUserEntity(Schema schema) {
        Entity user = schema.addEntity("User");
        user.addIdProperty();
        user.addStringProperty("name").notNull();
        user.addStringProperty("avatar").notNull();

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
        Entity completed = schema.addEntity("Completed");
        completed.addIdProperty();
        completed.addIntProperty("class");
        completed.addDateProperty("timeLastCompleted");

        return completed;
    }
}
