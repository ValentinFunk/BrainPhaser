package de.fhdw.ergoholics.brainphaser.activities.CategorySelect;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;

import de.fhdw.ergoholics.brainphaser.R;

/**
 * Created by Daniel on 03/03/2016.
 */
public class StatisticsFragment extends Fragment {
        ViewGroup mParentGroup;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            mParentGroup = container;

            View view = inflater.inflate(R.layout.fragment_category_statistics, container, false);

            BarChart barChart = (BarChart) mParentGroup.findViewById(R.id.barChart);
            PieChart pieChart = (PieChart) mParentGroup.findViewById(R.id.pieChart);

            setVisible(false);

            return view;
        }

    public void setVisible(boolean visible)
    {
        if (visible) {
            mParentGroup.findViewById(R.id.statisticsView).setVisibility(View.VISIBLE);
        }
        else {
            mParentGroup.findViewById(R.id.statisticsView).setVisibility(View.GONE);
        }
    }
}
