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
        createUserEntity(schema);
        Entity categoryEntity = createCategoryEntity(schema);
        Entity challengeEntity = createChallengeEntity(schema);
        Entity answerEntity = createAnswerEntity(schema);

        // category HAS MANY challenge
        Property categoryId = challengeEntity.addLongProperty("categoryId").notNull().getProperty();
        ToMany categoryToChallenge = categoryEntity.addToMany(challengeEntity, categoryId);
        categoryToChallenge.setName("challenges");

        // Todo: challenge HAS MANY answer?

        new DaoGenerator().generateAll(schema, "../app/src/main/java/");
    }

    public static void createUserEntity(Schema schema) {
        Entity user = schema.addEntity("User");
        user.addIdProperty();
        user.addStringProperty("name").notNull();
        user.addStringProperty("avatar").notNull();
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
        answer.addIntProperty("challenge_id");
        answer.addStringProperty("text").notNull();
        answer.addBooleanProperty("answer_correct");

        return answer;
    }
}
