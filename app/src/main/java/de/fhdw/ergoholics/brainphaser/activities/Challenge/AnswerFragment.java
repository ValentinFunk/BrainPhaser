package de.fhdw.ergoholics.brainphaser.activities.Challenge;

import android.os.Bundle;

import de.fhdw.ergoholics.brainphaser.BuildConfig;
import de.fhdw.ergoholics.brainphaser.activities.BrainPhaserFragment;
import de.fhdw.ergoholics.brainphaser.database.ChallengeDataSource;
import de.fhdw.ergoholics.brainphaser.model.Answer;
import de.fhdw.ergoholics.brainphaser.model.Challenge;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Christian on 03.03.2016.
 */
public abstract class AnswerFragment extends BrainPhaserFragment {
    protected List<Answer> mAnswerList;
    protected Challenge mChallenge;

    @Inject ChallengeDataSource mChallengeDataSource;

    public abstract boolean checkAnswers();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Load the current challenge
        Bundle bundle = getArguments();
        long id = bundle.getLong(ChallengeActivity.KEY_CHALLENGE_ID);
        mChallenge = mChallengeDataSource.getById(id);
        if (BuildConfig.DEBUG && mChallenge == null) {
            throw new RuntimeException("Invalid challenge passed to AnswerFragment");
        }

        mAnswerList = mChallenge.getAnswers();
        if (BuildConfig.DEBUG && mAnswerList == null) {
            throw new RuntimeException("Invalid Answers for challenge " + mChallenge.getId() + "(" + mChallenge.getQuestion() + ")");
        }
    }
}
