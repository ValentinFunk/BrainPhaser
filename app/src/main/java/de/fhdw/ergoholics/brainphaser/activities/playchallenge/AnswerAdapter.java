package de.fhdw.ergoholics.brainphaser.activities.playchallenge;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.model.Answer;

/**
 * Created by Christian Kost
 * <p/>
 * Adapter to load the given answers into a simple list
 */
public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.AnswerViewHolder> {
    private List<Answer> mAnswers;
    private String mGivenAnswer;

    /**
     * Constructor for the answer adapter which loads the different answers of a challenge
     *
     * @param answers     List of answers of the challenge
     * @param givenAnswer The given answer by the user
     */
    public AnswerAdapter(List<Answer> answers, String givenAnswer) {
        mAnswers = answers;
        mGivenAnswer = givenAnswer;
    }

    /**
     * Inflate the view
     *
     * @param parent   Ignored
     * @param viewType Ignored
     * @return The ViewHolder with the inflated view
     */
    @Override
    public AnswerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater myInflater = LayoutInflater.from(parent.getContext());
        //Load the list template
        View customView = myInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        TextView textView = (TextView) customView.findViewById(android.R.id.text1);
        textView.setGravity(Gravity.CENTER);
        return new AnswerViewHolder(customView);
    }

    /**
     * Create a list item
     *
     * @param holder   ViewHolder which binds the answer text
     * @param position The position of the answer item
     */
    @Override
    public void onBindViewHolder(AnswerViewHolder holder, int position) {
        //bind the answer
        Answer answer = mAnswers.get(position);
        holder.bindAnswer(answer.getText());
    }

    /**
     * Get the size of the view
     *
     * @return size of the answer list
     */
    @Override
    public int getItemCount() {
        return mAnswers.size();
    }

    /**
     * Answer View Holder holds the items in the AnswerList
     */
    public class AnswerViewHolder extends RecyclerView.ViewHolder {
        private TextView mAnswerText;
        private int colorRight;

        /**
         * Constructor fo the ViewHolder. Sets up the answer text and the highlight color
         *
         * @param itemView View to find the TextView for the answer text
         */
        public AnswerViewHolder(View itemView) {
            super(itemView);
            mAnswerText = (TextView) itemView.findViewById(android.R.id.text1);
            colorRight = ContextCompat.getColor(itemView.getContext(), R.color.colorRight);
        }

        /**
         * Set the answer text in the View Holder and highlight the given answer
         *
         * @param answer Answer text
         */
        public void bindAnswer(String answer) {
            mAnswerText.setText(answer);
            // Ff the answer equals to the given answer, mark the text
            if (mGivenAnswer != null && mGivenAnswer.equals(answer)) {
                mAnswerText.setTextColor(colorRight);
            }
        }
    }
}