package de.fhdw.ergoholics.brainphaser.activities.CategorySelect;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.model.Category;
import de.fhdw.ergoholics.brainphaser.utility.ImageProxy;

import java.util.List;

/**
 * Created by funkv on 17.02.2016.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    List<Category> mCategories;
    SelectionListener mListener;

    public CategoryAdapter(List<Category> categories, SelectionListener listener) {
        mListener = listener;
        mCategories = categories;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category, parent, false);

        int sizeDip = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
            300.f,
            parent.getContext().getResources().getDisplayMetrics());
        v.setLayoutParams(new LinearLayoutCompat.LayoutParams(sizeDip, LinearLayoutCompat.LayoutParams.WRAP_CONTENT));

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Categories
        if (position == 0) {
            holder.getTitle().setText(holder.itemView.getResources().getString(R.string.all_categories));
            holder.getDescription().setText(holder.itemView.getResources().getString(R.string.all_categories_desc));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onAllCategoriesSelected();
                }
            });
        } else {
            final Category category = mCategories.get(position - 1);
            holder.getTitle().setText(category.getTitle());
            holder.getDescription().setText(category.getDescription());
            if (ImageProxy.isDrawableImage(category.getImage())) {
                holder.getImage().setImageResource(ImageProxy.getResourceId(category.getImage(), holder.getContext()));
            } else {
                ImageProxy.loadImage(category.getImage(), holder.getContext()).into(holder.getImage());
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onCategorySelected(category);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mCategories.size() + 1;
    }

    /**
     * Interface to pass selection events.
     */
    public interface SelectionListener {
        void onCategorySelected(Category category);

        void onAllCategoriesSelected();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitle;
        private TextView mDescription;
        private ImageView mImage;

        public ViewHolder(View itemView) {
            super(itemView);

            mTitle = (TextView) itemView.findViewById(R.id.categoryTitle);
            mDescription = (TextView) itemView.findViewById(R.id.categoryDescription);
            mImage = (ImageView) itemView.findViewById(R.id.categoryImage);
        }

        public TextView getTitle() {
            return mTitle;
        }

        public TextView getDescription() {
            return mDescription;
        }

        public ImageView getImage() {
            return mImage;
        }

        public Context getContext() {
            return itemView.getContext();
        }
    }
}
