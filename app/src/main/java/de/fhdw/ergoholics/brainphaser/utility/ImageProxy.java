package de.fhdw.ergoholics.brainphaser.utility;

import android.content.Context;
import android.content.res.Resources;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.File;

/**
 * Created by funkv on 17.02.2016.
 *
 * Gives a combined interface for loading both filesystem images and resource drawables
 * via picasso. Filepaths that are drawable should be prefixed with @drawable/, the filepath is
 * then the resource name.
 */
public class ImageProxy {
    public static boolean isDrawableImage(String imagePath) {
        return imagePath.startsWith("@drawable/");
    }

    public static RequestCreator loadImage(String imagePath, Context context) {
        if (isDrawableImage(imagePath)) {
            return Picasso.with(context).load(new File(imagePath));
        } else {
            Resources resources = context.getResources();
            String resourceName = imagePath.substring("@drawable/".length());
            int resourceId =  resources.getIdentifier(resourceName, "drawable", context.getPackageName());
            return Picasso.with(context).load(resourceId);
        }
    }
}
