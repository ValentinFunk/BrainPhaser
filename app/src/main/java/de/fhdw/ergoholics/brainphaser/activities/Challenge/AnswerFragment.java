package de.fhdw.ergoholics.brainphaser.activities.Challenge;

import android.support.v4.app.Fragment;
import android.app.AlertDialog;
import android.nfc.FormatException;
import android.os.Bundle;
import java.util.List;

import de.fhdw.ergoholics.brainphaser.activities.BrainPhaserFragment;
import de.fhdw.ergoholics.brainphaser.database.ChallengeDataSource;
import de.fhdw.ergoholics.brainphaser.model.Answer;
import de.fhdw.ergoholics.brainphaser.model.Challenge;

import javax.inject.Inject;

/**
 * Created by Christian on 03.03.2016.
 */
public abstract class AnswerFragment extends BrainPhaserFragment {
    protected List<Answer> mAnswerList;
    protected Challenge mChallenge;

    @Inject ChallengeDataSource mChallengeDataSource;

    public abstract boolean checkAnswers();

    public void loadChallengeAndAnswers(){
        try {
            //Load the current challenge

            Bundle bundle = getArguments();
            long id = bundle.getLong(ChallengeActivity.KEY_CHALLENGE_ID);
            mChallenge = mChallengeDataSource.getById(id);

            //Check if challenge is okay
            if (mChallenge == null) {
                throw new NullPointerException("Whops. Leider konnte die Challenge " + id + " nicht geladen werden. :(");
            }
            //Load its Answers
            mAnswerList = mChallenge.getAnswers();
            //Check if answers are okay
            if (mAnswerList == null) {
                throw new NullPointerException("Whops. Leider konnten die Antworten der Challenge " + id + " nciht geladen werden. :(");
            }
            if (mChallenge.getChallengeType() == 1 && mAnswerList.size() != 4) {
                throw new FormatException("Whops. Leider sind die Antworten der Challenge " + id + " nicht korrekt. :(");
            }
        }
        catch (FormatException e) {
            AlertDialog.Builder messageBox = new AlertDialog.Builder(getActivity());
            messageBox.setTitle("Fehler");
            messageBox.setMessage(e.getMessage());
            messageBox.setCancelable(false);
            messageBox.setNeutralButton("OK", null);
            messageBox.show();
        }
    }
}
