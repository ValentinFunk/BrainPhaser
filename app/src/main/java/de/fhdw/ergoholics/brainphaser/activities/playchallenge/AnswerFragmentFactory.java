package de.fhdw.ergoholics.brainphaser.activities.playchallenge;

import java.util.HashMap;

import de.fhdw.ergoholics.brainphaser.BuildConfig;
import de.fhdw.ergoholics.brainphaser.activities.playchallenge.multiplechoice.MultipleChoiceFragment;
import de.fhdw.ergoholics.brainphaser.activities.playchallenge.selfcheck.SelfTestFragment;
import de.fhdw.ergoholics.brainphaser.activities.playchallenge.text.TextFragment;
import de.fhdw.ergoholics.brainphaser.database.ChallengeType;

/**
 * Created by Christian Kost
 * </p>
 * Abstracts and bundles the creation of Fragments for Challenge Types.
 */
public class AnswerFragmentFactory {
    private static HashMap<Integer, AnswerFragmentCreator> challengeTypeFactories;

    static {
        challengeTypeFactories = new HashMap<>();
        challengeTypeFactories.put(ChallengeType.MULTIPLE_CHOICE, new AnswerFragmentCreator() {
            @Override
            public AnswerFragment createFragment() {
                return new MultipleChoiceFragment();
            }
        });
        challengeTypeFactories.put(ChallengeType.TEXT, new AnswerFragmentCreator() {
            @Override
            public AnswerFragment createFragment() {
                return new TextFragment();
            }
        });
        challengeTypeFactories.put(ChallengeType.SELF_TEST, new AnswerFragmentCreator() {
            @Override
            public AnswerFragment createFragment() {
                return new SelfTestFragment();
            }
        });
    }

    /**
     * Creates a fragment for the specified challengeType.
     *
     * @param challengeType one of ChallengeType.*
     * @return newly created fragment.
     */
    public AnswerFragment createFragmentForType(int challengeType) {
        AnswerFragmentCreator creator = challengeTypeFactories.get(challengeType);
        if (BuildConfig.DEBUG && creator == null) {
            throw new RuntimeException("Invalid Challenge Type " + challengeType);
        }

        return creator.createFragment();
    }

    /**
     * Interface to create an AnswerFragment
     */
    private interface AnswerFragmentCreator {
        AnswerFragment createFragment();
    }
}
