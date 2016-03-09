package de.fhdw.ergoholics.brainphaser.activities.ChallengeImport;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import de.fhdw.ergoholics.brainphaser.BrainPhaserApplication;
import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.activities.main.MainActivity;
import de.fhdw.ergoholics.brainphaser.fileimport.FileImport;
import de.fhdw.ergoholics.brainphaser.fileimport.exceptions.FileFormatException;
import de.fhdw.ergoholics.brainphaser.fileimport.exceptions.UnexpectedElementException;

import java.io.File;

/**
 * Created by Daniel Hoogen on 16/02/2016.
 *
 * This is the activity shown when a bpc file is opened with the app
 */
public class ImportChallengeActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_challenges);
    }

    public void onButtonYesClicked(View v) {
        Intent intent = getIntent();
        File file;

        //Get the opened file from the Intent
        //Todo: fix content scheme
        if (intent.getScheme().equals("file")) {
            file = new File(intent.getData().getPath());
        }
        else
        {
            file = null;
        }

        //The file is being imported. String message is filled with a message for the user which
        //informs about a successful import or occurred errors.
        String message = "";
        try {
            FileImport.importBPC(file, (BrainPhaserApplication)getApplication());
            message = "Datei erfolgreich importiert!";
        } catch (FileFormatException e) {
            Log.d("Wrong File Format", e.toString());
            message = "Fehler: Die Datei ist keine XML Datei!";
        } catch (UnexpectedElementException e) {
            Log.d("Unexpected Element", e.toString());
            message = "Fehler: Die Datei enthält ungültige Elemente!";
        }

        //Show the message in a dialogue
        AlertDialog.Builder messageBox = new AlertDialog.Builder(this);
        messageBox.setTitle("Dateiimport");
        messageBox.setMessage(message);
        messageBox.setCancelable(false);
        messageBox.setNeutralButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.dismiss();

                        //Switch to the main activity when the button is clicked
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));

                        setResult(RESULT_OK);
                        finish();
                    }
                });
        messageBox.show();
    }

    public void onButtonNoClicked(View v) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}