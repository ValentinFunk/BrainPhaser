package de.fhdw.ergoholics.brainphaser.activities.statistics;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

/**
 * Created by Daniel Hoogen on 13/03/2016.
 * <p>
 * This class implements a listener for selecting and deselecting values in a chart
 */
public class ChartValueSelectedListener implements OnChartValueSelectedListener {
    //Attributes
    private StatisticViewHolder mHolder;

    /**
     * This constructor saves the given parameters as member attributes
     *
     * @param holder the view holder to be saved as a member attribute
     */
    public ChartValueSelectedListener(StatisticViewHolder holder) {
        mHolder = holder;
    }

    /**
     * This method is called when a value in the chart, this listener is assigned to, is selected.
     * It runs the corresponding method with the ViewHolder object containing the chart.
     *
     * @param e            the selected entry
     * @param dataSetIndex ignored
     * @param h            ignored
     */
    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        mHolder.onValueSelected(e);
    }

    /**
     * This method is called when a value in the chart is unselected. It runs the corresponding
     * method with the ViewHolder object containing the chart.
     */
    @Override
    public void onNothingSelected() {
        mHolder.onNothingSelected();
    }
}
