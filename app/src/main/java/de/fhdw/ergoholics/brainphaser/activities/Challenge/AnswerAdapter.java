package de.fhdw.ergoholics.brainphaser.activities.Challenge;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import de.fhdw.ergoholics.brainphaser.model.Answer;

/**
 * Created by Christian on 03.03.2016.
 */
public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.UserViewHolder>  {
        /**
         * User View Holder holds the items in the AnswerList
         */
        public class UserViewHolder extends RecyclerView.ViewHolder {
            private TextView mAnswerText;

            //Constructor
            public UserViewHolder(View itemView) {
                super(itemView);
                mAnswerText = (TextView) itemView.findViewById(android.R.id.text1);
            }

            /**
             * Create the item in the View Holder
             *
             * @param answer Username
             */
            public void bindAnswer(String answer) {
                mAnswerText.setText(answer);
            }
        }

        private List<Answer> mAnswers;

        public AnswerAdapter(List<Answer> answers) {
            mAnswers=answers;
        }

        @Override
        public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater myInflater = LayoutInflater.from(parent.getContext());
            //Load the list template
            View customView = myInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            return new UserViewHolder(customView);
        }

        //Create a list item
        @Override
        public void onBindViewHolder(UserViewHolder holder, int position) {
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