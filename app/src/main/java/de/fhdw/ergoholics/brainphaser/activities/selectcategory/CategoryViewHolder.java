package de.fhdw.ergoholics.brainphaser.activities.selectcategory;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.model.Category;
import de.fhdw.ergoholics.brainphaser.utility.ImageProxy;

/**
 * Created by funkv on 08.03.2016.
 *
 * The view holder is responsible for the view interaction with each Category within a
 * RecyclerView.
 */
public class CategoryViewHolder extends RecyclerView.ViewHolder {
    private TextView mTitle;
    private TextView mDescription;
    private ImageView mImage;
    private TextView mDueCountText;
    private Button mLearnButton;
    private Button mStatisticsButton;

    private CategoryAdapter.SelectionListener mSelectionListener;

    /**
     * This constructor saves the given parameters as member attributes and retrieves all necessary
     * views from the given itemView.
     *
     * @param itemView the item view of the view holder
     * @param selectionListener the selection listener for the view holder
     */
    public CategoryViewHolder(View itemView, CategoryAdapter.SelectionListener selectionListener) {
        super(itemView);
        mSelectionListener = selectionListener;

        mTitle = (TextView) itemView.findViewById(R.id.categoryTitle);
        mDescription = (TextView) itemView.findViewById(R.id.categoryDescription);
        mImage = (ImageView) itemView.findViewById(R.id.categoryImage);
        mDueCountText = (TextView) itemView.findViewById(R.id.challenges_due);
        mLearnButton = (Button) itemView.findViewById(R.id.learnButton);
        mStatisticsButton = (Button) itemView.findViewById(R.id.statisticsButton);
    }

    /**
     * Bind a specific category to this view holder, updating the associated view.
     *
     * @param category category to bind
     */
    public void bindCard(final Category category, int dueChallenges) {
        mTitle.setText(category.getTitle());
        mDescription.setText(category.getDescription());
        if (ImageProxy.isDrawableImage(category.getImage())) {
            mImage.setImageResource(ImageProxy.getResourceId(category.getImage(), itemView.getContext()));
        } else {
            ImageProxy.loadImage(category.getImage(), itemView.getContext()).into(mImage);
        }

        View.OnClickListener categorySelected = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectionListener.onCategorySelected(category);
            }
        };
        itemView.setOnClickListener(categorySelected);
        mLearnButton.setOnClickListener(categorySelected);

        mStatisticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectionListener.onCategoryStatisticsSelected(category);
            }
        });

        updateDueCounter(dueChallenges);
    }

    /**
     * Bind the "All Categories" element
     */
    public void bindAllCategoriesCard(int dueChallenges) {
        mTitle.setText(itemView.getResources().getString(R.string.all_categories));
        mDescription.setText(itemView.getResources().getString(R.string.all_categories_desc));

        View.OnClickListener allCategoriesSelected = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectionListener.onAllCategoriesSelected();
            }
        };
        itemView.setOnClickListener(allCategoriesSelected);
        mLearnButton.setOnClickListener(allCategoriesSelected);
        mStatisticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectionListener.onAllCategoriesStatisticsSelected();
            }
        });
        mImage.setImageResource(R.drawable.all);

        updateDueCounter(dueChallenges);
    }

    /**
     * Updates the due counter's color depending on the count
     */
    private void updateDueCounter(int amountDue) {
        if (amountDue > 0) {
            mDueCountText.setTextColor(ContextCompat.getColor(itemView.getContext(), android.R.color.primary_text_light));
            mDueCountText.setText(itemView.getResources().getQuantityString(R.plurals.challenges_due, amountDue, amountDue));
        } else {
            mDueCountText.setTextColor(ContextCompat.getColor(itemView.getContext(), android.R.color.tertiary_text_dark));
            mDueCountText.setText(R.string.no_challenges_due);
        }
    }

    /**
     * Returns the title view
     *
     * @return the title view of the view holder
     */
    public TextView getTitle() {
        return mTitle;
    }
}
