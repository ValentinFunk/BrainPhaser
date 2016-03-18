package de.fhdw.ergoholics.brainphaser;

import de.fhdw.ergoholics.brainphaser.activities.aboutscreen.AboutActivity;
import de.fhdw.ergoholics.brainphaser.activities.createuser.CreateUserActivity;
import de.fhdw.ergoholics.brainphaser.activities.main.MainActivity;
import de.fhdw.ergoholics.brainphaser.activities.main.ProxyActivity;
import de.fhdw.ergoholics.brainphaser.activities.playchallenge.AnswerFragment;
import de.fhdw.ergoholics.brainphaser.activities.playchallenge.ChallengeActivity;
import de.fhdw.ergoholics.brainphaser.activities.playchallenge.multiplechoice.ButtonViewState;
import de.fhdw.ergoholics.brainphaser.activities.playchallenge.multiplechoice.MultipleChoiceFragment;
import de.fhdw.ergoholics.brainphaser.activities.playchallenge.selfcheck.SelfTestFragment;
import de.fhdw.ergoholics.brainphaser.activities.playchallenge.text.TextFragment;
import de.fhdw.ergoholics.brainphaser.activities.selectcategory.SelectCategoryPage;
import de.fhdw.ergoholics.brainphaser.activities.selectuser.UserAdapter;
import de.fhdw.ergoholics.brainphaser.activities.selectuser.UserSelectionActivity;
import de.fhdw.ergoholics.brainphaser.activities.statistics.StatisticsActivity;
import de.fhdw.ergoholics.brainphaser.activities.usersettings.SettingsActivity;
import de.fhdw.ergoholics.brainphaser.logic.UserLogicFactory;
import de.fhdw.ergoholics.brainphaser.logic.fileimport.bpc.BPCWrite;

/**
 * Created by funkv on 06.03.2016.
 *
 * App Component that defines injection targets for DI.
 */
public interface BrainPhaserComponent {
    void inject(MainActivity mainActivity);
    void inject(ProxyActivity activity);
    void inject(ChallengeActivity challengeActivity);
    void inject(MultipleChoiceFragment questionFragment);
    void inject(TextFragment textFragment);
    void inject(SelfTestFragment selfTestFragment);
    void inject(CreateUserActivity createUserActivity);
    void inject(UserAdapter userAdapter);
    void inject(UserSelectionActivity activity);
    void inject(StatisticsActivity activity);
    void inject(SettingsActivity activity);

    void inject(AboutActivity activity);

    void inject(SelectCategoryPage selectCategoryPage);
    void inject(AnswerFragment answerFragment);

    void inject(BPCWrite bpcWrite);
    void inject(ButtonViewState state);

    void inject(UserLogicFactory f);
}
