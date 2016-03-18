package de.fhdw.ergoholics.brainphaser.activities.playchallenge.multiplechoice;

import android.os.Parcel;
import android.os.Parcelable;

import de.fhdw.ergoholics.brainphaser.BrainPhaserApplication;
import de.fhdw.ergoholics.brainphaser.activities.BrainPhaserActivity;
import de.fhdw.ergoholics.brainphaser.database.AnswerDataSource;
import de.fhdw.ergoholics.brainphaser.model.Answer;

import javax.inject.Inject;

/**
 * Created by funkv on 18.03.2016.
 *
 * Represents an answer button's state including answer and toggle state.
 * Implement Parcelable to allow saving/restoring using bundles
 */
public class ButtonViewState implements Parcelable {
    private boolean mToggleState = false;
    private Answer mAnswer;
    @Inject
    AnswerDataSource mAnswerDataSource;

    public ButtonViewState(Answer answer) {
        mAnswer = answer;
    }

    private ButtonViewState(Parcel in) {
        BrainPhaserApplication.component.inject(this);
        mAnswer = mAnswerDataSource.getById(in.readLong());
        mToggleState = in.readInt() == 1;
    }

    public boolean getToggleState() {
        return mToggleState;
    }

    public void setToggleState(boolean toggleState) {
        mToggleState = toggleState;
    }

    public Answer getAnswer() {
        return mAnswer;
    }

    public void setAnswer(Answer answer) {
        mAnswer = answer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mAnswer.getId());
        dest.writeInt(mToggleState ? 1 : 0);
    }

    public static final Parcelable.Creator<ButtonViewState> CREATOR
        = new Parcelable.Creator<ButtonViewState>() {
        public ButtonViewState createFromParcel(Parcel in) {
            return new ButtonViewState(in);
        }

        public ButtonViewState[] newArray(int size) {
            return new ButtonViewState[size];
        }
    };
}