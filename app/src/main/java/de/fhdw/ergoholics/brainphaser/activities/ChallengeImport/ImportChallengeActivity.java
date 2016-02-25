package de.fhdw.ergoholics.brainphaser.activities.ChallengeImport;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.File;

import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.activities.MainActivity;
import de.fhdw.ergoholics.brainphaser.fileimport.exceptions.FileFormatException;
import de.fhdw.ergoholics.brainphaser.fileimport.FileImport;
import de.fhdw.ergoholics.brainphaser.fileimport.exceptions.UnexpectedElementException;

/**
 * Created by Daniel Hoogen on 16/02/2016.
 */
public class ImportChallengeActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_challenges);
    }

    public void onButtonYesClicked(View v) {
        File file = new File(getIntent().getData().getPath());

        try {
            FileImport.importBPC(file);
        } catch (FileFormatException e) {
            Log.d("Wrong File Format", e.toString());
        } catch (UnexpectedElementException e) {
            Log.d("Unexpected Element", e.toString());
        }

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void onButtonNoClicked(View v) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}