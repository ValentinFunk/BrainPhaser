package de.fhdw.ergoholics.brainphaser.database;

import de.fhdw.ergoholics.brainphaser.R;

/**
 * Created by Christian Kost
 *
 * Defines common functions for stages
 */
public class ChallengeStage {
    private static int[] stageColorResources = new int[] {
        -1,
        R.color.colorStage1,
        R.color.colorStage2,
        R.color.colorStage3,
        R.color.colorStage4,
        R.color.colorStage5,
        R.color.colorStage6
    };

    /**
     * Returns the resource id of the color for this stage
     * @param stage challenge type to get the color for
     * @return resource id corresponding to the stage's associated color
     */
    public static int getColorResource(int stage) {
        return stageColorResources[stage];
    }
}
