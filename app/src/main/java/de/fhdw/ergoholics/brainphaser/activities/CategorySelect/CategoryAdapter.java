package de.fhdw.ergoholics.brainphaser.activities.CategorySelect;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
        private ImageView mExpandButton;
        private boolean mExpanded;
        private CardView mStatisticsView;
        private StatisticsFragment mStatisticsFragment;

        private static Drawable BTN_LESS;
        private static Drawable BTN_MORE;

        public ViewHolder(final View itemView) {
            super(itemView);

            BTN_LESS = getContext().getResources().getDrawable(R.drawable.ic_expand_less_black_48dp);
            BTN_MORE = getContext().getResources().getDrawable(R.drawable.ic_expand_more_black_48dp);

            mTitle = (TextView) itemView.findViewById(R.id.categoryTitle);
            mDescription = (TextView) itemView.findViewById(R.id.categoryDescription);
            mImage = (ImageView) itemView.findViewById(R.id.categoryImage);

            mExpandButton = (ImageView) itemView.findViewById(R.id.expandButton);
            mExpandButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onToggle(itemView);
                }
            });
            mExpanded = false;

            mStatisticsView = (CardView) itemView.findViewById(R.id.statisticsView);

            mStatisticsFragment = new StatisticsFragment();

            //Show StatisticsFragment in CardView
            FragmentManager fragmentManager = ((Activity) getContext()).getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.statisticsView, mStatisticsFragment);
            fragmentTransaction.commit();
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

        public void onToggle(View itemView) {
            if (mExpanded) {
                mExpandButton.setImageDrawable(BTN_MORE);

                mStatisticsFragment.setVisible(false);
            }
            else {
                mExpandButton.setImageDrawable(BTN_LESS);

                mStatisticsFragment.setVisible(true);

            }
            mExpanded = !mExpanded;
        }
    }
}
