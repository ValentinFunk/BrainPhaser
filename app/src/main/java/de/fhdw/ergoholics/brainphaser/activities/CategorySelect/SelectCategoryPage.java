package de.fhdw.ergoholics.brainphaser.activities.CategorySelect;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.fhdw.ergoholics.brainphaser.BrainPhaserApplication;
import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.activities.Challenge.ChallengeActivity;
import de.fhdw.ergoholics.brainphaser.database.CategoryDataSource;
import de.fhdw.ergoholics.brainphaser.model.Category;
import de.fhdw.ergoholics.brainphaser.model.User;

import java.util.Arrays;
import java.util.List;

/**
 * Created by funkv on 17.02.2016.
 */
public class SelectCategoryPage extends Fragment implements CategoryAdapter.SelectionListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_select_category, container, false);

        // Set orientation to horizontal
        RecyclerView recyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerView);

        // get 300dpi in px
        float cardWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300.f, getResources().getDisplayMetrics());
        boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        float cardHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300.f - 48.f, getResources().getDisplayMetrics());

        int spans = isLandscape ? (int)Math.floor(getResources().getDisplayMetrics().heightPixels / cardHeight) : (int)Math.floor(getResources().getDisplayMetrics().widthPixels / cardWidth);
        int orientation = isLandscape ? StaggeredGridLayoutManager.HORIZONTAL : StaggeredGridLayoutManager.VERTICAL;

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(spans, orientation);
        recyclerView.setLayoutManager(layoutManager);

        User currentUser = ((BrainPhaserApplication) getActivity().getApplication()).getCurrentUser();
        List<Category> categories = CategoryDataSource.getAll();
        recyclerView.setAdapter(new CategoryAdapter(currentUser,  categories, this));

        return rootView;
    }

    @Override
    public void onCategorySelected(Category category) {
        Intent intent = new Intent(getContext(), ChallengeActivity.class);
        intent.putExtra(ChallengeActivity.EXTRA_CATEGORY_ID, category.getId());
        startActivity(intent);
    }

    @Override
    public void onAllCategoriesSelected() {
        Intent intent = new Intent(getContext(), ChallengeActivity.class);
        intent.putExtra(ChallengeActivity.EXTRA_CATEGORY_ID, CategoryDataSource.CATEGORY_ID_ALL);
        startActivity(intent);
    }
}
