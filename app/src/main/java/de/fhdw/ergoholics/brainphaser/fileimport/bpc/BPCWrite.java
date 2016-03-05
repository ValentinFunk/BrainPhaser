package de.fhdw.ergoholics.brainphaser.fileimport.bpc;

import java.util.List;

import de.fhdw.ergoholics.brainphaser.database.AnswerDataSource;
import de.fhdw.ergoholics.brainphaser.database.CategoryDataSource;
import de.fhdw.ergoholics.brainphaser.database.ChallengeDataSource;
import de.fhdw.ergoholics.brainphaser.model.Answer;
import de.fhdw.ergoholics.brainphaser.model.Category;
import de.fhdw.ergoholics.brainphaser.model.Challenge;

/**
 * Created by Daniel Hoogen on 25/02/2016.
 */
public class BPCWrite {
    public static void writeAll(List<Category> categoryList, List<Challenge> challengeList, List<Answer> answerList) {
        for (Category category : categoryList) {
            writeCategory(category, challengeList, answerList);
        }
    }

    private static void writeCategory(Category category, List<Challenge> challengeList, List<Answer> answerList) {
        long oldCategoryId = category.getId();
        category.setId(null);
        long categoryId = CategoryDataSource.getInstance().create(category);

        for (Challenge challenge : challengeList) {
            if (challenge.getCategoryId() == oldCategoryId) {
                challenge.setCategoryId(categoryId);
                writeChallenge(challenge, answerList);
                challenge.setCategoryId(-1);
            }
        }
    }

    private static void writeChallenge(Challenge challenge, List<Answer> answerList) {
        long oldChallengeId = challenge.getId();
        challenge.setId(null);
        long challengeId = ChallengeDataSource.getInstance().create(challenge);

        for (Answer answer : answerList) {
            if (answer.getChallengeId() == oldChallengeId) {
                answer.setChallengeId(challengeId);
                writeAnswer(answer);
                answer.setChallengeId(-1);
            }
        }
    }

    private static void writeAnswer(Answer answer) {
        answer.setId(null);
        AnswerDataSource.getInstance().create(answer);
    }
}
