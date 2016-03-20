package de.fhdw.ergoholics.brainphaser.activities.playchallenge.multiplechoice;

import android.os.Parcel;
import android.os.Parcelable;

import javax.inject.Inject;

import de.fhdw.ergoholics.brainphaser.BrainPhaserApplication;
import de.fhdw.ergoholics.brainphaser.database.AnswerDataSource;
import de.fhdw.ergoholics.brainphaser.model.Answer;

/**
 * Created by Christian Kost
 * </p>
 * Represents an answer button's state including answer and toggle state.
 * Implement Parcelable to allow saving/restoring using bundles
 */
public class ButtonViewState implements Parcelable {
    public static final Parcelable.Creator<ButtonViewState> CREATOR
            = new Parcelable.Creator<ButtonViewState>() {
        public ButtonViewState createFromParcel(Parcel in) {
            return new ButtonViewState(in);
        }

        public ButtonViewState[] newArray(int size) {
            return new ButtonViewState[size];
        }
    };
    @Inject
    AnswerDataSource mAnswerDataSource;
    private boolean mToggleState = false;
    private Answer mAnswer;

    /**
     * Constructor instantiates the answer
     *
     * @param answer Answer text
     */
    public ButtonViewState(Answer answer) {
        mAnswer = answer;
    }

    /**
     * @param in Parcel
     */
    private ButtonViewState(Parcel in) {
        BrainPhaserApplication.component.inject(this);
        mAnswer = mAnswerDataSource.getById(in.readLong());
        mToggleState = in.readInt() == 1;
    }

    /**
     * Returns the current ToggleState
     *
     * @return ToggleState
     */
    public boolean getToggleState() {
        return mToggleState;
    }

    /**
     * Sets the ToggleState
     *
     * @param toggleState ToggleState
     */
    public void setToggleState(boolean toggleState) {
        mToggleState = toggleState;
    }

    /**
     * Return the current Answer
     *
     * @return Answer
     */
    public Answer getAnswer() {
        return mAnswer;
    }

    /**
     * Set the Answer
     *
     * @param answer Answer
     */
    public void setAnswer(Answer answer) {
        mAnswer = answer;
    }

    /**
     * DescribeContents return 0
     *
     * @return 0
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Writes the answer id and the toggle state to the given parcel
     *
     * @param dest the parel to write to
     * @param flags ignored
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mAnswer.getId());
        dest.writeInt(mToggleState ? 1 : 0);
    }
}