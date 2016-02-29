package de.fhdw.ergoholics.brainphaser.activities.Challenge;

import android.app.AlertDialog;
import android.app.Fragment;
import android.nfc.FormatException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import java.util.Collections;
import java.util.List;
import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.database.ChallengeDataSource;
import de.fhdw.ergoholics.brainphaser.model.Answer;
import de.fhdw.ergoholics.brainphaser.model.Challenge;

/**
 * Created by Chris on 2/25/2016.
 */
public class MultipleChoiceFragment extends Fragment{


    private List<Answer> mAnswerList;
    private Challenge mChallenge;

    private CheckBox[] mCheckBoxArray;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_challenge_multiple_choice, container,false);
        try {
            CheckBox checkBox1 = (CheckBox) view.findViewById(R.id.checkbox1);
            CheckBox checkBox2 = (CheckBox) view.findViewById(R.id.checkbox2);
            CheckBox checkBox3 = (CheckBox) view.findViewById(R.id.checkbox3);
            CheckBox checkBox4 = (CheckBox) view.findViewById(R.id.checkbox4);

            //Load the current challenge
            Bundle bundle = getArguments();
            long id = bundle.getLong(ChallengeActivity.KEY_CHALLENGE_ID);
            mChallenge = ChallengeDataSource.getById(id);

            if (mChallenge == null) {
                throw new NullPointerException("Whops. No challenge with id: " + id);
            }
            //Load its Answers
            mAnswerList = mChallenge.getAnswers();
            if (mAnswerList == null) {
                throw new NullPointerException("Whops. No Answers to challenge " + id);
            }
            if(mAnswerList.size()!=4){
                throw new FormatException("Whops. " + mAnswerList.size() + "instead of 4 answers for challenge " +id);
            }
            //Fill Checkboxes
            mCheckBoxArray = new CheckBox[mAnswerList.size()];
            mCheckBoxArray[0] = checkBox1;
            mCheckBoxArray[1] = checkBox2;
            mCheckBoxArray[2] = checkBox3;
            mCheckBoxArray[3] = checkBox4;
            changeAnswers(mAnswerList);
        } catch (Exception e){
            AlertDialog.Builder messageBox = new AlertDialog.Builder(getActivity());
            messageBox.setTitle("Error");
            messageBox.setMessage(e.getMessage());
            messageBox.setCancelable(false);
            messageBox.setNeutralButton("OK", null);
            messageBox.show();

        }finally {
            return view;
        }
    }

    /**
     * Loads the answers of the challenge into the checkboxes
     * @param answers The answers of the Challenge (Multiple_Choice)
     */
    public void changeAnswers(List<Answer> answers){
        Collections.shuffle(answers);
        for(int i=0; i< mAnswerList.size(); i++) {
            mCheckBoxArray[i].setText(answers.get(i).getText());
        }
    }

    public boolean getCheckedAnswersRight(){

        Boolean[] booleanArray= new Boolean[mAnswerList.size()];
        Answer answer;

        for(int i=0; i< mAnswerList.size(); i++){
            answer = mAnswerList.get(i);
            if(mCheckBoxArray[i].isChecked()==false && answer.getAnswerCorrect() ==false ){
                booleanArray[i]=true;
            }else if (mCheckBoxArray[i].isChecked() && answer.getAnswerCorrect()){
                mCheckBoxArray[i].setBackgroundColor(getResources().getColor(R.color.colorRight));
                booleanArray[i]=true;
            }else if (mCheckBoxArray[i].isChecked() != answer.getAnswerCorrect() && answer.getAnswerCorrect()){
                mCheckBoxArray[i].setBackgroundColor(getResources().getColor(R.color.colorRight));
                booleanArray[i]=false;
            }else{
                mCheckBoxArray[i].setBackgroundColor(getResources().getColor(R.color.colorWrong));
                booleanArray[i]=false;
            }
        }
        return booleanArray[0] && booleanArray[1] && booleanArray[2] && booleanArray[3];
    }
}
