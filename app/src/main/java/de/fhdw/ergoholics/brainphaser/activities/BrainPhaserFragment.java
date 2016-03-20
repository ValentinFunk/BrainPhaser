package de.fhdw.ergoholics.brainphaser.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import de.fhdw.ergoholics.brainphaser.BrainPhaserApplication;
import de.fhdw.ergoholics.brainphaser.BrainPhaserComponent;

/**
 * Created by funkv on 06.03.2016.
 * <p/>
 * Base Activity class to be used by all Fragments in the project.
 * Subclasses need to implement injectComponent to use the Depency Injector.
 * See: https://blog.gouline.net/2015/05/04/dagger-2-even-sharper-less-square/
 */
public abstract class BrainPhaserFragment extends Fragment {
    /**
     * OnCreate injects components
     *
     * @param savedInstanceState Ignored
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        injectComponent(((BrainPhaserApplication) getActivity().getApplication()).getComponent());
    }

    /**
     * Called to inject dependencies. Should call component.inject(this) as
     * uniform implementation in all Activities.
     *
     * @param component
     */
    protected abstract void injectComponent(BrainPhaserComponent component);
}
