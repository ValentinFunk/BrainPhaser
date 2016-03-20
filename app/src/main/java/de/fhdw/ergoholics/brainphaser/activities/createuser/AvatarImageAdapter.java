package de.fhdw.ergoholics.brainphaser.activities.createuser;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.makeramen.roundedimageview.RoundedImageView;

/**
 * Created by funkv on 13.02.2016, adapted from http://developer.android.com/guide/topics/ui/layout/gridview.html
 * <p/>
 * Creates RoundedImages for each Avatar from the AvatarPickerDialogFragment.
 */
public class AvatarImageAdapter extends BaseAdapter {
    private Context mContext;
    // references to our images
    private Integer[] mThumbIds = Avatars.getAvatarResources();
    // references to ImageViews
    private RoundedImageView[] mImages = new RoundedImageView[mThumbIds.length];

    /**
     * Constructor defines the context
     *
     * @param c Context
     */
    public AvatarImageAdapter(Context c) {
        mContext = c;
    }

    /**
     * Get the length of the adapter
     *
     * @return Thumb-Id length
     */
    public int getCount() {
        return mThumbIds.length;
    }

    /**
     * Get an object at the position
     *
     * @param position Position of the item
     * @return Object at the position
     */
    public Object getItem(int position) {
        return mImages[position];
    }

    /**
     * Get the id of an item
     *
     * @param position Position of the item
     * @return 0
     */
    public long getItemId(int position) {
        return 0;
    }

    /**
     * Create a new ImageView for each item referenced by the Adapter
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        RoundedImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new RoundedImageView(mContext);

            int sizeDip = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    85.f,
                    mContext.getResources().getDisplayMetrics());

            imageView.setLayoutParams(new GridView.LayoutParams(sizeDip, sizeDip));

            //imageView.setScaleType(ImageView.ScaleType.);
            imageView.setOval(true);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (RoundedImageView) convertView;
        }

        mImages[position] = imageView;
        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }
}