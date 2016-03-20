package de.fhdw.ergoholics.brainphaser.activities.selectcategory;

import android.support.v4.util.LongSparseArray;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.database.CategoryDataSource;
import de.fhdw.ergoholics.brainphaser.model.Category;

/**
 * Created by funkv on 17.02.2016.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {
    private List<Category> mCategories;
    private SelectionListener mListener;
    private LongSparseArray<Integer> mDueChallengeCounts = new LongSparseArray<>();

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

    /**
     * Notify the adapter that new due challenge counts are to be displayed
     * @param dueChallengeCounts due challenge counts to apply
     */
    public void notifyDueChallengeCountsChanged(LongSparseArray<Integer> dueChallengeCounts) {
        mDueChallengeCounts = dueChallengeCounts;
        notifyDataSetChanged();
    }

    /**
     * Called to create the ViewHolder at the given position.
     *
     * @param parent   parent to assign the newly created view to
     * @param viewType ignored
     */
    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_category, parent, false);
        v.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT));

        return new CategoryViewHolder(v, mListener);
    }

    /**
     * Called to bind the ViewHolder at the given position.
     *
     * @param holder   the ViewHolder object to be bound
     * @param position the position of the ViewHolder
     */
    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        // Categories
        int amountDue = 0;
        if (position == 0) {
            for (Category category : mCategories) {
                amountDue += mDueChallengeCounts.get(category.getId());
            }
            holder.bindAllCategoriesCard(amountDue);
        } else {
            final Category category = mCategories.get(position - 1);
            holder.bindCard(category, mDueChallengeCounts.get(category.getId()));
        }
    }

    /**
     * Returns the count of ViewHolders in the adapter
     *
     * @return the count of ViewHolders
     */
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
            return CategoryDataSource.CATEGORY_ID_ALL;
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

        void onCategoryStatisticsSelected(Category category);

        void onAllCategoriesStatisticsSelected();
    }
}
