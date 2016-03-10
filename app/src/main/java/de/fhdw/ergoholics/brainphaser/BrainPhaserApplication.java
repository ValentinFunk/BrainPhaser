package de.fhdw.ergoholics.brainphaser;

import android.app.Application;

import de.fhdw.ergoholics.brainphaser.database.DatabaseModule;

import javax.inject.Singleton;
import dagger.Component;

/**
 * Created by funkv on 17.02.2016.
 *
 * Custom Application class for hooking into App creation
 */
public class BrainPhaserApplication extends Application {
    /**
     * Defines the Component to use in the Production Application.
     * The component is a bridge between Modules and Injects.
     * It creates instances of all the types defined.
     */
    @Singleton
    @Component(modules = {AppModule.class, DatabaseModule.class})
    public interface ApplicationComponent extends BrainPhaserComponent {
    }

    /**
     * Creates the Production app Component
     */
    private BrainPhaserComponent mComponent;
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
        return mComponent;
    }

    /**
     * Create the component used for Dependency Injection when the App is initialized
     */
    @Override
    public void onCreate() {
        super.onCreate();
        mComponent = createComponent();
    }
}
