package de.fhdw.ergoholics.brainphaser.database;

import de.fhdw.ergoholics.brainphaser.R;

/**
 * Created by Christian Kost on 03.03.2016.
 * <p/>
 * Defines valid challenge types.
 */
public class ChallengeType {
    public final static int MULTIPLE_CHOICE = 1;
    public final static int TEXT = 2;
    public final static int SELF_TEST = 3;

    private static int[] challengeNameResources = new int[]{
        -1,
        R.string.multiple_choice,
        R.string.text,
        R.string.self_check
    };

    private static int[] challengeColorResources = new int[]{
            -1,
            R.color.colorMultipleChoice,
            R.color.colorText,
            R.color.colorSelfCheck
    };

    /**
     * Returns the resource id of the name string for this challenge type
     * @param challengeType challenge type to get the name for
     * @return resource id corresponding to the challenge type name
     */
    public static int getNameResource(int challengeType) {
        return challengeNameResources[challengeType];
    }

    /**
     * Returns the resource id of the color for this challenge type
     * @param challengeType challenge type to get the color for
     * @return resource id corresponding to the challenge type's associated color
     */
    public static int getColorResource(int challengeType) {
        return challengeColorResources[challengeType];
    }
}