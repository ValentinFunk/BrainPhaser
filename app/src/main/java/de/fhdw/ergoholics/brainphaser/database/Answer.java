package de.fhdw.ergoholics.brainphaser.database;

/**
 * Created by Daniel Hoogen on 16/02/2016.
 * This class is the nonpersistent data model of the answers
 */
public class Answer {
    int mId;
    int mChallengeId;
    String mText;
    boolean mAnswerCorrect;

    public Answer(int id, int challengeId, String text, boolean answerCorrect) {
        this.mId = id;
        this.mChallengeId = challengeId;
        this.mText = text;
        this.mAnswerCorrect = answerCorrect;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public int getChallengeId() {
        return mChallengeId;
    }

    public void setChallengeId(int challengeId) {
        this.mChallengeId = challengeId;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        this.mText = text;
    }

    public boolean isAnswerCorrect() {
        return mAnswerCorrect;
    }

    public void setAnswerCorrect(boolean answerCorrect) {
        this.mAnswerCorrect = answerCorrect;
    }
}
