package de.fhdw.ergoholics.brainphaser.logic;

import org.mockito.Mockito;

import de.fhdw.ergoholics.brainphaser.model.User;

/**
 * Created by funkv on 07.03.2016.
 */
public class MockTestUserLogicFactory extends UserLogicFactory {
    @Override
    public DueChallengeLogic createDueChallengeLogic(User user) {
        return Mockito.mock(DueChallengeLogic.class);
    }
}
