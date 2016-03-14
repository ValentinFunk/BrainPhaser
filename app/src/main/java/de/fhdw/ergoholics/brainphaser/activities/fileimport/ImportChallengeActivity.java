package de.fhdw.ergoholics.brainphaser.activities.fileimport;

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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by Daniel Hoogen on 16/02/2016.
 *
 * This is the activity shown when a bpc file is opened with the app
 */
public class ImportChallengeActivity extends Activity {
    /**
     * This method is called when the activity is created
     * @param savedInstanceState ignored
     */
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_challenges);
    }

    /**
     * This method is called the yes button in the activity is clicked. It imports the bpc file
     * opened with the app after showing a confirmation dialogue. In the end the main activity is
     * called.
     * @param view ignored
     */
    public void onButtonYesClicked(View view) {
        Intent intent = getIntent();
        InputStream is;

        String scheme = intent.getScheme();

        //Get the input stream from opened file or content
        if (scheme.equals("file")) {
            Log.d("File Intent", intent.toString());
            File file = new File(intent.getData().getPath());

            try {
                is = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                is = null;
                Log.d("File scheme error", e.getMessage());
            }
        }
        else if (scheme.equals("content")) {
            Log.d("Content Intent", intent.toString());
            try {
                is = getContentResolver().openInputStream(intent.getData());
            } catch (FileNotFoundException e) {
                is = null;
                Log.d("Content scheme error", e.getMessage());
            }
        }
        else {
            is = null;
        }

        //The file is being imported. String message is filled with a message for the user which
        //informs about a successful import or occurred errors.
        String message = "";
        try {
            if (is!=null) {
                FileImport.importBPC(is, (BrainPhaserApplication) getApplication());
                message = "Datei erfolgreich importiert!";
            }
            else {
                message = "Dateiimport fehlgeschlagen!";
            }
        } catch (FileFormatException e) {
            Log.d("Wrong File Format", e.toString());
            message = "Fehler: Die Datei ist nicht im XML-Format!";
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

    /**
     * This method is called the no button in the activity is clicked. It calls the main activity.
     * @param view ignored
     */
    public void onButtonNoClicked(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        // Recreate the activity to respect new categories
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
    }
}