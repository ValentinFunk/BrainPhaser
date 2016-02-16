package de.fhdw.ergoholics.brainphaser.database;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel Hoogen on 16/02/2016.
 * Class for writing and reading Answer objects to/from the answer table
 * Inherits from DataSource
 * Persistent: Answer-Data
 */
public class AnswerDataSource extends DataSource {

    //Array of the columns of the answer table
    private String[] columns = {
            DatabaseStatics.ANSWER_COL_1,
            DatabaseStatics.ANSWER_COL_2,
            DatabaseStatics.ANSWER_COL_3,
            DatabaseStatics.ANSWER_COL_4
    };

    /**
     * Create an answer in the database
     *
     * @param answer to be created in the database
     * @return true if the insert was successful, false if not
     */
    public boolean createAnswer(Answer answer){
        ContentValues values = new ContentValues();
        values.put(DatabaseStatics.ANSWER_COL_2, answer.getChallengeId());
        values.put(DatabaseStatics.ANSWER_COL_3, answer.getText());
        values.put(DatabaseStatics.ANSWER_COL_4, answer.isAnswerCorrect());
        long rowId = mDatabase.insert(DatabaseStatics.ANSWER_TABLE_NAME, null, values);
        if(rowId!=-1) {
            answer.setId((int) rowId);
            return true;
        }
        return false;
    }

    /**
     * Delete answer from database
     *
     * @param answer Answer to be removed from database
     * @return true if the delete was successful, false if not
     */
    public boolean deleteAnswer(Answer answer){
        long rowId = mDatabase.delete(DatabaseStatics.ANSWER_TABLE_NAME, DatabaseStatics.ANSWER_COL_1 + "=" + answer.getId(), null);
        return rowId != -1;
    }

    /**
     * Reads the answer with the given id from the database
     *
     * @param id ID of the answer
     * @return the answer in the database
     */
    public Answer getAnswer(int id){
        Answer answer;
        Cursor cursor = mDatabase.query(DatabaseStatics.ANSWER_TABLE_NAME,columns,DatabaseStatics.ANSWER_COL_1 + "=" + id,null,null,null,null);
        answer = cursorToAnswer(cursor);
        cursor.close();
        return answer;
    }

    /**
     * Reads an answer with the given question from the database
     *
     * @param text Text of the answer
     * @return the answer in the database
     */
    public Answer getAnswer(String text){
        Answer answer;
        Cursor cursor = mDatabase.query(DatabaseStatics.ANSWER_TABLE_NAME,columns,DatabaseStatics.ANSWER_COL_2 + "=" + text,null,null,null,null);
        answer = cursorToAnswer(cursor);
        cursor.close();
        return answer;
    }

    /**
     * Builds and returns Answer Objects from the rows in the database
     *
     * @return ArrayList of all answers
     */

    public List<Answer> getAnswers(){

        //Create empty list
        List<Answer> allAnswers = new ArrayList<>();

        //Create cursor on the answer table
        Cursor cursor = mDatabase.query(DatabaseStatics.ANSWER_TABLE_NAME,columns,null,null,null,null,null);

        //Reset cursor
        cursor.moveToFirst();
        Answer answer;

        //Build all answer objects and add them to the list
        while(!cursor.isAfterLast()) {
            answer = cursorToAnswer(cursor);
            allAnswers.add(answer);
            cursor.moveToNext();
        }
        cursor.close();
        return allAnswers;
    }

    /**
     * Return the cursor's position as answer
     *
     * @param cursor Cursor that points on a row in the answer table
     * @return Answer from the database on the cursors position
     */
    private Answer cursorToAnswer(Cursor cursor){
        //retrieve the column IDs
        int idId = cursor.getColumnIndex(DatabaseStatics.ANSWER_COL_1);
        int idChallengeId = cursor.getColumnIndex(DatabaseStatics.ANSWER_COL_2);
        int idText = cursor.getColumnIndex(DatabaseStatics.ANSWER_COL_3);
        int idAnswerCorrect = cursor.getColumnIndex(DatabaseStatics.ANSWER_COL_4);

        //retrieve the column values
        int id = cursor.getInt(idId);
        int challengeId = cursor.getInt(idChallengeId);
        String text = cursor.getString(idText);
        boolean answerCorrect = cursor.getString(idAnswerCorrect).equals("true");

        return new Answer(id,challengeId,text,answerCorrect);
    }

    /**
     * Updates the answer with the id of the given Answer object
     *
     * @param answer the answer object to be updated
     * @return true if the update was successful, false if not
     */
    public boolean updateAnswer(Answer answer){
        ContentValues values=new ContentValues();
        values.put(DatabaseStatics.ANSWER_COL_2,answer.getChallengeId());
        values.put(DatabaseStatics.ANSWER_COL_3, answer.getText());
        values.put(DatabaseStatics.ANSWER_COL_4, answer.isAnswerCorrect());
        long rowNo = mDatabase.update(DatabaseStatics.ANSWER_TABLE_NAME,values,DatabaseStatics.ANSWER_COL_1 + "=" + answer.getId(),null);
        return rowNo != 0;
    }
}