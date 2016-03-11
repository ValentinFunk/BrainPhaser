package de.fhdw.ergoholics.brainphaser.activities.Challenge;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
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
 * Created by Christian on 03.03.2016.
 */
public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.AnswerViewHolder>  {
        /**
         * User View Holder holds the items in the AnswerList
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
             * Create the item in the View Holder
             *
             * @param answer Username
             */
            public void bindAnswer(String answer) {
                mAnswerText.setText(answer);
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

        @Override
        public AnswerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater myInflater = LayoutInflater.from(parent.getContext());
            //Load the list template
            View customView = myInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            return new AnswerViewHolder(customView);
        }

        //Create a list item
        @Override
        public void onBindViewHolder(AnswerViewHolder holder, int position) {
            //bind the answer
            Answer answer = mAnswers.get(position);
            holder.bindAnswer(answer.getText());
        }

        //Length of View is all answers
        @Override
        public int getItemCount() {
            return mAnswers.size();
        }
}