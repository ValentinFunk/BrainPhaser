package de.fhdw.ergoholics.brainphaser.activities.playchallenge;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.model.Answer;

/**
 * Adapter to load the given answers into a simple list
 */
public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.AnswerViewHolder>  {
        /**
         * Answer View Holder holds the items in the AnswerList
         */
        public class AnswerViewHolder extends RecyclerView.ViewHolder {
            private TextView mAnswerText;
            private int colorRight ;
            //Constructor
            public AnswerViewHolder(View itemView) {
                super(itemView);
                mAnswerText = (TextView) itemView.findViewById(android.R.id.text1);
                colorRight = ContextCompat.getColor(itemView.getContext(),R.color.colorRight);
            }

            /**
             * set the answer text in the View Holder
             * @param answer answer
             */
            public void bindAnswer(String answer) {
                mAnswerText.setText(answer);
                // if the answer equals to the given answer, mark the text
                if(mGivenAnswer != null && mGivenAnswer.equals(answer)){
                    mAnswerText.setTextColor(colorRight);
                }
            }
        }

        private List<Answer> mAnswers;
        private String mGivenAnswer;
        public AnswerAdapter(List<Answer> answers, String givenAnswer) {
            mAnswers=answers;
            mGivenAnswer=givenAnswer;
        }

    /**
     * Inflate the view
     * @param parent
     * @param viewType
     * @return
     */
        @Override
        public AnswerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater myInflater = LayoutInflater.from(parent.getContext());
            //Load the list template
            View customView = myInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            return new AnswerViewHolder(customView);
        }

    /**
     * Create a list item
     * @param holder
     * @param position
     */
        @Override
        public void onBindViewHolder(AnswerViewHolder holder, int position) {
            //bind the answer
            Answer answer = mAnswers.get(position);
            holder.bindAnswer(answer.getText());
        }

    /**
     * Get the size of the view
     * @return size of the answer list
     */
        @Override
        public int getItemCount() {
            return mAnswers.size();
        }
}