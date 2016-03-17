package de.fhdw.ergoholics.brainphaser.activities.createuser;

import android.content.Context;
import android.content.res.Resources;

import de.fhdw.ergoholics.brainphaser.R;

/**
 * Created by funkv on 15.02.2016.
 * <p/>
 * Class for bundling shared avatar functionality.
 */
public class Avatars {
    // List of all available avatars
    private static final Integer[] AVATARS = {
            R.drawable.anonymous, R.drawable.anonymous2_girl,
            R.drawable.astronaut, R.drawable.basketball_man,
            R.drawable.bomberman, R.drawable.bomberman2,
            R.drawable.boxer_hispanic, R.drawable.bride_hispanic_material,
            R.drawable.budist, R.drawable.call_center_operator_man,
            R.drawable.cashier_woman, R.drawable.cook2_man
    };

    /**
     * Get a list of all avatar resource ids.
     *
     * @return Array containing all avatar resource ids.
     */
    public static Integer[] getAvatarResources() {
        return AVATARS;
    }

    /**
     * Get the resource name for a given avatar resource id.
     *
     * @param context
     * @param avatarResourceId resource id of the avatar
     * @return resource name of the avatar to be used for persistent storage.
     */
    public static String getAvatarResourceName(Context context, Integer avatarResourceId) {
        Resources resources = context.getResources();
        return resources.getResourceEntryName(avatarResourceId);
    }

    /**
     * Get the resource id for a given avatar name
     *
     * @param context
     * @param avatarResourceName name of the avatar
     * @return resource id
     */
    public static Integer getAvatarResourceId(Context context, String avatarResourceName) {
        Resources resources = context.getResources();
        return resources.getIdentifier(avatarResourceName, "drawable", context.getPackageName());
    }

    /**
     * @return resource name of the default avatar
     */
    public static String getDefaultAvatarResourceName() {
        return "anonymous";
    }

    /**
     * @return resource id of the default avatar
     */
    public static Integer getDefaultAvatarResourceId() {
        return R.drawable.anonymous;
    }
}
