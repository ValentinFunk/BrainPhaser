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

    public Challenge(int mId, int mCategoryId, int mChallengeType, String mQuestion) {
        this.mId = mId;
        this.mCategoryId = mCategoryId;
        this.mChallengeType = mChallengeType;
        this.mQuestion = mQuestion;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public int getCategoryId() {
        return mCategoryId;
    }

    public void setCategoryId(int mCategoryId) {
        this.mCategoryId = mCategoryId;
    }

    public int getChallengeType() {
        return mChallengeType;
    }

    public void setChallengeType(int mChallengeType) {
        this.mChallengeType = mChallengeType;
    }

    public String getQuestion() {
        return mQuestion;
    }

    public void setQuestion(String mQuestion) {
        this.mQuestion = mQuestion;
    }
}
