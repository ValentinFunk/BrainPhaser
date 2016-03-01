package de.fhdw.ergoholics.brainphaser.activities.ChallengeImport;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import java.io.File;

import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.activities.MainActivity;
import de.fhdw.ergoholics.brainphaser.database.AnswerDataSource;
import de.fhdw.ergoholics.brainphaser.database.CategoryDataSource;
import de.fhdw.ergoholics.brainphaser.database.ChallengeDataSource;
import de.fhdw.ergoholics.brainphaser.fileimport.exceptions.FileFormatException;
import de.fhdw.ergoholics.brainphaser.fileimport.FileImport;
import de.fhdw.ergoholics.brainphaser.fileimport.exceptions.UnexpectedElementException;
import de.fhdw.ergoholics.brainphaser.model.Answer;
import de.fhdw.ergoholics.brainphaser.model.Category;
import de.fhdw.ergoholics.brainphaser.model.Challenge;

/**
 * Created by Daniel Hoogen on 16/02/2016.
 */
public class ImportChallengeActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_challenges);
    }

    public void onButtonYesClicked(View v) {
        Intent intent = getIntent();
        File file;

        //Todo: fix content scheme
        if (intent.getScheme().equals("content")) {
            file = new File("/sdcard/Download/test.bpc");
        }
        else if (intent.getScheme().equals("file")) {
            file = new File(intent.getData().getPath());
        }
        else
        {
            file = null;
        }

        try {
            FileImport.importBPC(file);
        } catch (FileFormatException e) {
            Log.d("Wrong File Format", e.toString());
        } catch (UnexpectedElementException e) {
            Log.d("Unexpected Element", e.toString());
        }

        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    public void onButtonNoClicked(View v) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}