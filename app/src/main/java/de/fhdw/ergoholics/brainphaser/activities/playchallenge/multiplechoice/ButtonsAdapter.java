package de.fhdw.ergoholics.brainphaser.activities.playchallenge.multiplechoice;

import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ToggleButton;

import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.model.Answer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by funkv on 18.03.2016.
 */
public class ButtonsAdapter extends RecyclerView.Adapter<ButtonsAdapter.ButtonViewHolder> {
    public class ButtonViewHolder extends RecyclerView.ViewHolder {
        ToggleButton mToggleButton;
        ImageView mSelectionCorrectMarker;
        Answer mAnswer;

        public ButtonViewHolder(View itemView) {
            super(itemView);
            mToggleButton = (ToggleButton) itemView.findViewById(R.id.toggleButton);
            mSelectionCorrectMarker = (ImageView) itemView.findViewById(R.id.selectionCorrectIndicator);
        }

        /**
         * Updates the view to indicate whether or not the answer was checked correctly.
         * @return true, if this answer is in the correct pressed state, else false
         */
        public boolean performAnswerCheck( ) {
            // Correct answers get a green background
            if (mAnswer.getAnswerCorrect()) {
                StateListDrawable drawable = (StateListDrawable)mToggleButton.getBackground();
                ((GradientDrawable)drawable.getCurrent()).setColor(ContextCompat.getColor(itemView.getContext(), R.color.buttonBackgroundRight));
            } else {
                StateListDrawable drawable = (StateListDrawable)mToggleButton.getBackground();
                ((GradientDrawable)drawable.getCurrent()).setColor(ContextCompat.getColor(itemView.getContext(), R.color.buttonBackground));
            }

            // Correct selections receive a cross or check
            boolean answerCorrect = mAnswer.getAnswerCorrect() == mToggleButton.isChecked();
            if (mToggleButton.isChecked()) {
                mSelectionCorrectMarker.setVisibility(View.VISIBLE);
                if (answerCorrect) {
                    mSelectionCorrectMarker.setImageResource(R.drawable.ic_check);
                } else {
                    mSelectionCorrectMarker.setImageResource(R.drawable.ic_cross);
                }
            } else {
                mSelectionCorrectMarker.setVisibility(View.GONE);
            }
            mSelectionCorrectMarker.bringToFront();
            mToggleButton.setEnabled(false);

            return answerCorrect;
        }

        public void bind(Answer answer) {
            mAnswer = answer;
            mToggleButton.setText(answer.getText());
            mToggleButton.setTextOn(answer.getText());
            mToggleButton.setTextOff(answer.getText());
            mSelectionCorrectMarker.bringToFront();
            mSelectionCorrectMarker.setVisibility(View.GONE);
        }
    }

    private List<Answer> mAnswers;
    private List<ButtonViewHolder> mButtons = new ArrayList<>();
    public ButtonsAdapter(List<Answer> answers) {
        mAnswers = answers;
        mButtons = new ArrayList<>(mAnswers.size());
    }

    @Override
    public ButtonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.multiplechoice_button, parent, false);

        // Clone drawable to allow modifications
        ToggleButton button = (ToggleButton) v.findViewById(R.id.toggleButton);
        StateListDrawable drawable = (StateListDrawable)button.getBackground();
        button.setBackgroundDrawable(drawable.getConstantState().newDrawable());

        return new ButtonViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ButtonViewHolder holder, int position) {
        mButtons.add(position, holder);
        holder.bind(mAnswers.get(position));
    }

    @Override
    public int getItemCount() {
        return mAnswers.size();
    }

    /**
     * Updates the view to display the correct answer and validates user selection.
     * @return true, if the user answered correctly, else false
     */
    public boolean performAnswerCheck() {
        boolean answerCorrect = true;
        for (ButtonViewHolder vh : mButtons) {
            if (!vh.performAnswerCheck()) {
                answerCorrect = false;
            }
        }
        return answerCorrect;
    }
}