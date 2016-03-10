package de.fhdw.ergoholics.brainphaser;

/**
 * Created by funkv on 07.03.2016.
 */
public class TestBrainPhaserApplication extends BrainPhaserApplication {
    BrainPhaserComponent mTestComponent;
    public void setTestComponent(BrainPhaserComponent component) {
        mTestComponent = component;
    }

    @Override
    public BrainPhaserComponent getComponent() {
        return mTestComponent;
    }
}
