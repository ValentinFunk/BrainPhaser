package de.fhdw.ergoholics.brainphaser.database;

/**
 * Created by Daniel Hoogen on 16/02/2016.
 * This class is the nonpersistent data model of the challenges
 */
public class Challenge
{
    public static final int MULTIPLECHOICE = 1;
    public static final int ONE_CORRECT_ANSWER = 2;
    public static final int MULTIPLE_CORRECT_ANSWERS = 3;
    public static final int USER_DECISION = 4;

    int mId;
    int mCategoryId;
    int mChallengeType;
    String mQuestion;

    public Challenge(int id, int categoryId, int challengeType, String question) {
        this.mId = id;
        this.mCategoryId = categoryId;
        this.mChallengeType = challengeType;
        this.mQuestion = question;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public int getCategoryId() {
        return mCategoryId;
    }

    public void setCategoryId(int categoryId) {
        this.mCategoryId = categoryId;
    }

    public int getChallengeType() {
        return mChallengeType;
    }

    public void setChallengeType(int challengeType) {
        this.mChallengeType = challengeType;
    }

    public String getQuestion() {
        return mQuestion;
    }

    public void setQuestion(String question) {
        this.mQuestion = question;
    }
}
