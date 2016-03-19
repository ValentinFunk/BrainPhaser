package de.fhdw.ergoholics.brainphaser;

import android.app.Application;

import net.danlew.android.joda.JodaTimeAndroid;

import javax.inject.Singleton;

import dagger.Component;
import de.fhdw.ergoholics.brainphaser.database.DatabaseModule;

/**
 * Created by funkv on 17.02.2016.
 *
 * Custom Application class for hooking into App creation
 */
public class BrainPhaserApplication extends Application {
    public static String PACKAGE_NAME;
    public static BrainPhaserComponent component;

    /**
     * Creates the Production app Component
     */
    protected BrainPhaserComponent createComponent( ) {
        return DaggerBrainPhaserApplication_ApplicationComponent.builder()
            .appModule(new AppModule(this))
            .databaseModule(new DatabaseModule(getApplicationContext(), "prodDb"))
            .build();
    }

    /**
     * Returns the Component for use with Dependency Injection for this
     * Application.
     * @return compoenent to use for DI
     */
    public BrainPhaserComponent getComponent( ) {
        return component;
    }

    /**
     * initializes the DaoManager with a writeable database
     */
    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
        component = createComponent();
        PACKAGE_NAME = getApplicationContext().getPackageName();
    }

    /**
     * Defines the Component to use in the Production Application.
     * The component is a bridge between Modules and Injects.
     * It creates instances of all the types defined.
     */
    @Singleton
    @Component(modules = {AppModule.class, DatabaseModule.class})
    public interface ApplicationComponent extends BrainPhaserComponent {
    }
}