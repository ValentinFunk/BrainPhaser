package de.fhdw.ergoholics.brainphaser.activities.about;

import android.support.v7.app.ActionBar;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

import de.fhdw.ergoholics.brainphaser.BrainPhaserComponent;
import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.activities.BrainPhaserActivity;
import de.fhdw.ergoholics.brainphaser.utility.FileUtils;


/**
 * Created by thomasstuckel on 07/03/2016.
 */
public class AboutActivity extends BrainPhaserActivity {
    private TextView mAboutText;

    @Override
    protected void injectComponent(BrainPhaserComponent component) {
        component.inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);

        mAboutText = (TextView) findViewById(R.id.action_about);

        try {
            String contents = getStringFromRawFile();
            mAboutText.setText(contents);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        // Set as Actionbar
        setSupportActionBar(toolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
    }

    String getStringFromRawFile() throws IOException {
        Resources r = getResources();
        InputStream is = r.openRawResource(R.raw.credits);
        String myText = FileUtils.convertStreamToString(is);
        is.close();
        return myText;
    }

}
