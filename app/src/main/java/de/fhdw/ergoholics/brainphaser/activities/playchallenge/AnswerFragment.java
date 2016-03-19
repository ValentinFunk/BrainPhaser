package de.fhdw.ergoholics.brainphaser.activities.playchallenge;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import javax.inject.Inject;

import de.fhdw.ergoholics.brainphaser.BuildConfig;
import de.fhdw.ergoholics.brainphaser.activities.BrainPhaserFragment;
import de.fhdw.ergoholics.brainphaser.database.ChallengeDataSource;
import de.fhdw.ergoholics.brainphaser.model.Answer;
import de.fhdw.ergoholics.brainphaser.model.Challenge;

/**
 * Created by Christian Kost
 * <p/>
 * The abstract fragment contains all necessary  methods for the different challenge-type fragments
 */
public abstract class AnswerFragment extends BrainPhaserFragment {
    protected List<Answer> mAnswerList;
    protected Challenge mChallenge;

    // Interface to pass events to the activity
    protected AnswerListener mListener;

    @Inject
    ChallengeDataSource mChallengeDataSource;

    /**
     * Called by the Activity when the floating action button has been pressed.
     * Should be used to check answers or query information from the user.
     * <p/>
     * After this call the fragment is responsible for calling onAnswerChecked to continue to the
     * next challenge.
     *
     * @return Whether or not to disable the challenge FloatingActionButton for custom view: if true, the floating action button is hidden for the state.
     */
    public abstract ContinueMode goToNextState();

    /**
     * Saves the current Challenge-Id
     *
     * @param outState Bundle that contains the Challenge-Id
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putLong(ChallengeActivity.KEY_CHALLENGE_ID, mChallenge.getId());
    }

    /**
     * Loads the Listener, the current challenge and its answers
     *
     * @param savedInstanceState Reloads the old state of the fragment
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadAnswerListener();

        // Load the current challenge from saved state or arguments
        Bundle bundle = savedInstanceState != null ? savedInstanceState : getArguments();
        long id = bundle.getLong(ChallengeActivity.KEY_CHALLENGE_ID);
        mChallenge = mChallengeDataSource.getById(id);
        if (BuildConfig.DEBUG && mChallenge == null) {
            throw new RuntimeException("Invalid challenge passed to AnswerFragment");
        }

        loadAnswers();
    }

    /**
     * Loads the answers of the challenge
     */
    protected void loadAnswers() {
        mAnswerList = mChallenge.getAnswers();
        if (BuildConfig.DEBUG && mAnswerList == null) {
            throw new RuntimeException("Invalid Answers for challenge " + mChallenge.getId() + "(" + mChallenge.getQuestion() + ")");
        }
    }

    /**
     * Loads the AnswerListener of the opening activity
     */
    private void loadAnswerListener() {
        // The activity that opens these fragments must implement AnswerListener.
        // This method stores the listener when the activity is attached.
        // Verify that the host activity implements the callback interface
        try {
            // Cast to SelfTestDialogListener so we can send events to the host
            mListener = (AnswerListener) getActivity();
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().toString()
                    + " must implement AnswerListener");
        }
    }

    /**
     * Utility function that loads all of the correct answers of the current challenge into a
     * recycler view.
     *
     * @param listViewId id of the recycler view, which will contain the answers
     */
    protected void populateRecyclerViewWithCorrectAnswers(int listViewId, String givenAnswer) {
        //loading of the components
        RecyclerView answerList = (RecyclerView) getView().findViewById(listViewId);
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
     * Possible modes for the Fragment to react to a request to goToNextState().
     */
    public enum ContinueMode {
        /**
         * Abort the action because some kind of validation failed. State was not changed.
         */
        CONTINUE_ABORT,

        /**
         * Successful state change, instruct activity to hide Floating Action Button.
         * The Challenge will instruct the activity to load the next challenge through
         * {@link de.fhdw.ergoholics.brainphaser.activities.playchallenge.AnswerFragment.AnswerListener#onAnswerChecked(boolean, boolean)}
         */
        CONTINUE_HIDE_FAB,

        /**
         * Successful state change, instruct activity to show the floating action button and
         * allow progression to next challenge by clicking it.
         */
        CONTINUE_SHOW_FAB
    }

    /**
     * Interface to pass answer checks on to the activity.
     */
    public interface AnswerListener {
        /**
         * Called by the Fragment when the correctness of an answer has been determined.
         *
         * @param answerCorrect whether or not the user answered correctly
         * @param skipConfirm   when true the activity switches directly to the next challenge without
         *                      waiting for the user to click on the FAB
         */
        void onAnswerChecked(boolean answerCorrect, boolean skipConfirm);
    }
}