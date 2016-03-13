package de.fhdw.ergoholics.brainphaser.activities.statistics;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

/**
 * Created by Daniel Hoogen on 13/03/2016.
 */
public class ChartValueSelectedListener implements OnChartValueSelectedListener {
    private StatisticViewHolder mHolder;

    public ChartValueSelectedListener(StatisticViewHolder holder) {
        mHolder = holder;
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        mHolder.onValueSelected(e);
    }

    @Override
    public void onNothingSelected() {
        mHolder.onNothingSelected();
    }
}
