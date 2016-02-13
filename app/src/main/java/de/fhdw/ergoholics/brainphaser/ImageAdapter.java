package de.fhdw.ergoholics.brainphaser;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.makeramen.roundedimageview.RoundedImageView;

/**
 * Created by funkv on 13.02.2016, adapted from http://developer.android.com/guide/topics/ui/layout/gridview.html
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return mImages[position];
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        RoundedImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new RoundedImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
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

    // references to our images
    private Integer[] mThumbIds = AvatarPickerDialogFragment.getAvatars();

    // references to ImageViews
    private RoundedImageView[] mImages = new RoundedImageView[mThumbIds.length];
}