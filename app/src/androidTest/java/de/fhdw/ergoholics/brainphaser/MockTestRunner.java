package de.fhdw.ergoholics.brainphaser;

import android.app.Application;
import android.content.Context;
import android.support.test.runner.AndroidJUnitRunner;

import de.fhdw.ergoholics.brainphaser.BrainPhaserApplication;
import de.fhdw.ergoholics.brainphaser.TestBrainPhaserApplication;
import de.fhdw.ergoholics.brainphaser.activities.BrainPhaserActivity;

/**
 * Created by funkv on 07.03.2016.
 */
public class MockTestRunner extends AndroidJUnitRunner {
    @Override
    public Application newApplication(ClassLoader cl, String className, Context context) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return super.newApplication(cl, TestBrainPhaserApplication.class.getName(), context);
    }
}
