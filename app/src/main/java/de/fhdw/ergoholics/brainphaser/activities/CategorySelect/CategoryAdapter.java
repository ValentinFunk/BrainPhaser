package de.fhdw.ergoholics.brainphaser.activities.CategorySelect;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.model.Category;
import de.fhdw.ergoholics.brainphaser.utility.ImageProxy;

/**
 * Created by funkv on 17.02.2016.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
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

    List<Category> mCategories;

    public CategoryAdapter(List<Category> categories) {
        mCategories = categories;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category, parent, false);

        int sizeDip = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                300.f,
                parent.getContext().getResources().getDisplayMetrics());
        v.setLayoutParams(new LinearLayoutCompat.LayoutParams(sizeDip, LinearLayoutCompat.LayoutParams.MATCH_PARENT));

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Category category = mCategories.get(position);

        holder.getTitle().setText(category.getTitle());
        holder.getDescription().setText(category.getDescription());
        ImageProxy.loadImage(category.getImage(), holder.getContext()).into(holder.getImage());
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }
}
