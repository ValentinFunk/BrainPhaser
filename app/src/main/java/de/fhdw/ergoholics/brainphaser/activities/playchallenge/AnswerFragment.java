package de.fhdw.ergoholics.brainphaser.activities.playchallenge;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import de.fhdw.ergoholics.brainphaser.BuildConfig;
import de.fhdw.ergoholics.brainphaser.activities.BrainPhaserFragment;
import de.fhdw.ergoholics.brainphaser.database.ChallengeDataSource;
import de.fhdw.ergoholics.brainphaser.model.Answer;
import de.fhdw.ergoholics.brainphaser.model.Challenge;

import java.util.List;

import javax.inject.Inject;

/**
 * The abstract fragment contains all necessary  methods for the different challenge-type fragments
 */
public abstract class AnswerFragment extends BrainPhaserFragment {
    /**
     * Interface
     */
    public interface AnswerListener{
        void onAnswerChecked(boolean answer);
    }

    // Use this interface to deliver action events
    AnswerListener mListener;

    protected List<Answer> mAnswerList;
    protected Challenge mChallenge;
    protected View mView;

    @Inject ChallengeDataSource mChallengeDataSource;

    /**
     * Called to check the given answer(s)
     */
    public abstract void checkAnswers();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        loadAnswerListener();
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


    /**
     * Loads all answers of the current challenge into a simple list
     * @param listViewId RecyclerView, which will contain the answers
     */
    protected void loadAnswers(int listViewId, String givenAnswer){
        //loading of the components
        RecyclerView answerList = (RecyclerView) mView.findViewById(listViewId);
        answerList.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        answerList.setLayoutManager(layoutManager);
        //Create the View
        //Adapter which sets all answers into the list
        AnswerAdapter listAdapter = new AnswerAdapter(mAnswerList, givenAnswer);
        answerList.setAdapter(listAdapter);
    }
    /**
     * Loads the AnswerListener of the opening activity
     */
    private void loadAnswerListener(){
        // The activity that opens these fragments must implement AnswerListener.
        // This method stores the listener when the activity is attached.
        // Verify that the host activity implements the callback interface
        try {
            // Cast to SelfTestDialogListener so we can send events to the host
            mListener = (AnswerListener) getActivity();
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().toString()
                    + " must implement NoticeDialogListener");
        }
    }
}
