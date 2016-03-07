package de.fhdw.ergoholics.brainphaser.activities.CategorySelect;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.media.Image;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.LongSparseArray;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.logic.DueChallengeLogic;
import de.fhdw.ergoholics.brainphaser.model.Category;
import de.fhdw.ergoholics.brainphaser.utility.ImageProxy;

import java.util.List;

/**
 * Created by funkv on 17.02.2016.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitle;
        private TextView mDescription;
        private ImageView mImage;

        private TextView mDueCountText;

        public ViewHolder(View itemView) {
            super(itemView);

            mTitle = (TextView) itemView.findViewById(R.id.categoryTitle);
            mDescription = (TextView) itemView.findViewById(R.id.categoryDescription);
            mImage = (ImageView) itemView.findViewById(R.id.categoryImage);
            mDueCountText = (TextView) itemView.findViewById(R.id.challenges_due);
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

        public TextView getDueCountText() {
            return mDueCountText;
        }
    }

    List<Category> mCategories;
    SelectionListener mListener;
    LongSparseArray<Integer> mDueChallengeCounts = new LongSparseArray<>();

    /**
     * Adapter for Listing Categories in a recycler view
     * @param categories list of categories to show
     * @param dueChallengeCounts an SparseArray that maps category ids to their due challenge count.
     * @param listener listener that is notified when a category is clicked.
     */
    public CategoryAdapter(List<Category> categories, LongSparseArray<Integer> dueChallengeCounts, SelectionListener listener) {
        mListener = listener;
        mCategories = categories;
        mDueChallengeCounts = dueChallengeCounts;

        setHasStableIds(true);
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

    // Bind data to the view
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Categories
        int amountDue = 0;
        if (position == 0) {
            holder.getTitle().setText(holder.itemView.getResources().getString(R.string.all_categories));
            holder.getDescription().setText(holder.itemView.getResources().getString(R.string.all_categories_desc));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onAllCategoriesSelected();
                }
            });

            for (Category category : mCategories) {
                amountDue += mDueChallengeCounts.get(category.getId());
            }
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

            amountDue = mDueChallengeCounts.get(category.getId());
        }

        if (amountDue > 0) {
            holder.getDueCountText( ).setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorAccent));

            holder.getDueCountText( ).setText(holder.itemView.getResources().getQuantityString(R.plurals.challenges_due, amountDue, amountDue));
        } else {
            int[] attrs = {android.R.attr.textColorSecondary};
            TypedArray ta = holder.itemView.getContext().obtainStyledAttributes(android.R.style.TextAppearance_Medium, attrs);
            holder.getDueCountText( ).setTextColor(ta.getColor(0, Color.BLACK));
            ta.recycle();

            holder.getDueCountText().setText(R.string.no_challenges_due);
        }
    }

    @Override
    public int getItemCount() {
        return mCategories.size() + 1;
    }

    /**
     * Returns the stable ID for the item at <code>position</code>.
     *
     * @param position Adapter position to query
     * @return the stable ID of the item at position
     */
    @Override
    public long getItemId(int position) {
        if (position == 0) {
            return -1; // All Categories
        }

        Category category = mCategories.get(position - 1);
        return category.getId();
    }

    /**
     * Interface to pass selection events.
     */
    public interface SelectionListener {
        void onCategorySelected(Category category);

        void onAllCategoriesSelected();
    }
}
