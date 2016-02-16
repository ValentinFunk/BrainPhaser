package de.fhdw.ergoholics.brainphaser.database;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel Hoogen on 16/02/2016.
 * Class for writing and reading Challenge objects to/from the challenge table
 * Inherits from DataSource
 * Persistent: Challenge-Data
 */
public class ChallengeDataSource extends DataSource {

    //Array of the columns of the challenge table
    private String[] columns = {
            DatabaseStatics.CHALLENGE_COL_1,
            DatabaseStatics.CHALLENGE_COL_2,
            DatabaseStatics.CHALLENGE_COL_3,
            DatabaseStatics.CHALLENGE_COL_4
    };

    /**
     * Create a challenge in the database
     *
     * @param challenge Challenge to be created in the database
     * @return true if the insert was successful, false if not
     */
    public boolean createChallenge(Challenge challenge){
        ContentValues values = new ContentValues();
        values.put(DatabaseStatics.CHALLENGE_COL_2, challenge.getCategoryId());
        values.put(DatabaseStatics.CHALLENGE_COL_3, challenge.getChallengeType());
        values.put(DatabaseStatics.CHALLENGE_COL_4, challenge.getQuestion());
        long rowId = mDatabase.insert(DatabaseStatics.CHALLENGE_TABLE_NAME, null, values);
        if(rowId!=-1) {
            challenge.setId((int) rowId);
            return true;
        }
        return false;
    }

    /**
     * Delete challenge from database
     *
     * @param challenge Challenge to be removed from database
     * @return true if the delete was successful, false if not
     */
    public boolean deleteChallenge(Challenge challenge){
        long rowId = mDatabase.delete(DatabaseStatics.CHALLENGE_TABLE_NAME, DatabaseStatics.CHALLENGE_COL_1 + "=" + challenge.getId(), null);
        return rowId != -1;
    }

    /**
     * Reads the challenge with the given id from the database
     *
     * @param id ID of the challenge
     * @return the challenge in the database
     */
    public Challenge getChallenge(int id){
        Challenge challenge;
        Cursor cursor=mDatabase.query(DatabaseStatics.CHALLENGE_TABLE_NAME,columns,DatabaseStatics.CHALLENGE_COL_1 + "=" + id,null,null,null,null);
        challenge = cursorToChallenge(cursor);
        cursor.close();
        return challenge;
    }

    /**
     * Reads a challenge with the given question from the database
     *
     * @param question Question of the challenge
     * @return the challenge in the database
     */
    public Challenge getChallenge(String question){
        Challenge challenge;
        Cursor cursor = mDatabase.query(DatabaseStatics.CHALLENGE_TABLE_NAME,columns,DatabaseStatics.CHALLENGE_COL_4 + "=" + question,null,null,null,null);
        challenge = cursorToChallenge(cursor);
        cursor.close();
        return challenge;
    }

    /**
     * Builds and returns Challenge Objects from the rows in the database
     *
     * @return ArrayList of all challenges
     */

    public List<Challenge> getChallenges(){

        //Create empty list
        List<Challenge> allChallenges = new ArrayList<>();

        //Create cursor on the challenge table
        Cursor cursor = mDatabase.query(DatabaseStatics.CHALLENGE_TABLE_NAME,columns,null,null,null,null,null);

        //Reset cursor
        cursor.moveToFirst();
        Challenge challenge;

        //Build all challenge objects and add them to the list
        while(!cursor.isAfterLast()) {
            challenge = cursorToChallenge(cursor);
            allChallenges.add(challenge);
            cursor.moveToNext();
        }
        cursor.close();
        return allChallenges;
    }

    /**
     * Return the cursor's position as challenge
     *
     * @param cursor Cursor that points on a row in the challenge table
     * @return Challenge from the database on the cursors position
     */
    private Challenge cursorToChallenge(Cursor cursor){
        //retrieve the column IDs
        int idId = cursor.getColumnIndex(DatabaseStatics.CHALLENGE_COL_1);
        int idChallengeSetId = cursor.getColumnIndex(DatabaseStatics.CHALLENGE_COL_2);
        int idType = cursor.getColumnIndex(DatabaseStatics.CHALLENGE_COL_3);
        int idQuestion = cursor.getColumnIndex(DatabaseStatics.CHALLENGE_COL_4);

        //retrieve the column values
        int id = cursor.getInt(idId);
        int challengeSet = cursor.getInt(idChallengeSetId);
        int type = cursor.getInt(idType);
        String question = cursor.getString(idQuestion);

        return new Challenge(id,challengeSet,type,question);
    }

    /**
     * Updates the challenge with the id of the given Challenge object
     *
     * @param challenge the challenge object to be updated
     * @return true if the update was successful, false if not
     */
    public boolean updateChallenge(Challenge challenge){
        ContentValues values=new ContentValues();
        values.put(DatabaseStatics.CHALLENGE_COL_2,challenge.getCategoryId());
        values.put(DatabaseStatics.CHALLENGE_COL_3, challenge.getChallengeType());
        values.put(DatabaseStatics.CHALLENGE_COL_4, challenge.getQuestion());
        long rowNo = mDatabase.update(DatabaseStatics.CHALLENGE_TABLE_NAME,values,DatabaseStatics.CHALLENGE_COL_1 + "=" + challenge.getId(),null);
        return rowNo != 0;
    }
}
