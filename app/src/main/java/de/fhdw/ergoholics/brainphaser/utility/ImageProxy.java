package de.fhdw.ergoholics.brainphaser.utility;

import android.content.Context;
import android.content.res.Resources;
import android.util.SparseArray;

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
    private static SparseArray<RequestCreator> requestCache = new SparseArray<>();

    /**
     * Returns whether the path represents a drawable resource.
     * @param imagePath the path to analyze
     * @return true if the path represents a drawable resource
     */
    public static boolean isDrawableImage(String imagePath) {
        return imagePath.startsWith("@drawable/");
    }

    /**
     * Gets the resource id of an abstract path.
     * @param imagePath imagePath the path to analyze
     * @param context context to use
     * @return resource id of the corresponding drawable
     */
    public static int getResourceId(String imagePath, Context context) {
        if (!isDrawableImage(imagePath)) {
            throw new IllegalArgumentException("Drawable is not a resource drawable.");
        }

        Resources resources = context.getResources();
        String resourceName = imagePath.substring("@drawable/".length());
        return resources.getIdentifier(resourceName, "drawable", context.getPackageName());
    }

    /**
     * Loads an image using Picasso while caching it.
     * @param imagePath path as described above
     * @param context context to use
     * @return cached or newly created RequestCreator
     */
    public static RequestCreator loadImage(String imagePath, Context context) {
        if (!isDrawableImage(imagePath)) {
            return Picasso.with(context).load(new File(imagePath));
        } else {
            int resourceId = getResourceId(imagePath, context);
            RequestCreator requestCreator = requestCache.get(resourceId);
            if (requestCreator == null) {
                requestCreator = Picasso.with(context).load(resourceId);
                requestCache.put(resourceId, requestCreator);
            }

            return requestCreator;
        }
    }
}
