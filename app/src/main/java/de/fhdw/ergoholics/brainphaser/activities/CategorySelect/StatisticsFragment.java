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
        View mView;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            mView = inflater.inflate(R.layout.fragment_category_statistics, container, false);

            BarChart barChart = (BarChart) mView.findViewById(R.id.barChart);
            PieChart pieChart = (PieChart) mView.findViewById(R.id.pieChart);

            setVisible(false);

            return mView;
        }

    public void setVisible(boolean visible)
    {
        if (visible) {
            mView.setVisibility(View.VISIBLE);
        }
        else {
            mView.setVisibility(View.GONE);
        }
    }
}
