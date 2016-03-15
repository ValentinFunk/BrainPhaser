package de.fhdw.ergoholics.brainphaser.logic.statistics;

import android.graphics.Paint;
import android.support.v4.content.ContextCompat;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import javax.inject.Inject;

import de.fhdw.ergoholics.brainphaser.BrainPhaserApplication;
import de.fhdw.ergoholics.brainphaser.R;

/**
 * Created by Daniel on 12/03/2016.
 */
public class ChartSettings {
    //Constants
    private static final float SLICE_SPACE = 1f;
    private static final float SELECTION_SHIFT = 5f;

    private static final float CENTER_TEXT_SIZE = 12f;
    private static final float NO_DATA_TEXT_SIZE = 18f;
    private static final float VALUE_TEXT_SIZE = 14f;
    private static final float LEGEND_TEXT_SIZE = 11.f;
    private static final float SCALE_FACTOR = 0.95f;

    //Attributes
    private  int[] mColorset = new int[6];
    private BrainPhaserApplication mApplication;

    //Constructor
    @Inject
    public ChartSettings(BrainPhaserApplication application) {
        mApplication = application;

        mColorset[0] = ContextCompat.getColor(mApplication.getApplicationContext(), R.color.colorStage1);
        mColorset[1] = ContextCompat.getColor(mApplication.getApplicationContext(), R.color.colorStage2);
        mColorset[2] = ContextCompat.getColor(mApplication.getApplicationContext(), R.color.colorStage3);
        mColorset[3] = ContextCompat.getColor(mApplication.getApplicationContext(), R.color.colorStage4);
        mColorset[4] = ContextCompat.getColor(mApplication.getApplicationContext(), R.color.colorStage5);
        mColorset[5] = ContextCompat.getColor(mApplication.getApplicationContext(), R.color.colorStage6);
    }

    /**
     * Applies the specified format to the PieChart Object.
     * @param chart the chart which will be formatted
     */
    public void applyChartSettings(PieChart chart) {
        chart.setScaleX(SCALE_FACTOR);
        chart.setScaleY(SCALE_FACTOR);

        chart.setCenterTextSize(CENTER_TEXT_SIZE);

        chart.setUsePercentValues(false);
        chart.setDrawSliceText(false);

        chart.setDescription("");

        chart.getLegend().setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        chart.getLegend().setTextSize(LEGEND_TEXT_SIZE);
    }

    /**
     * Applies the specified format to the PieDataSet Object.
     * @param dataset the dataset which will be formatted
     */
    public void applyDataSetSettings(PieDataSet dataset) {
        dataset.setSliceSpace(SLICE_SPACE);
        dataset.setValueTextSize(VALUE_TEXT_SIZE);
        dataset.setSelectionShift(SELECTION_SHIFT);
        dataset.setColors(mColorset);
        dataset.setValueFormatter(new CustomizedFormatter());
    }

    /**
     * Applies the specified format to the PieData Object.
     * @param data the data which will be formatted
     */
    public void applyDataSettings(PieData data) {
        data.setDrawValues(true);
    }

    /**
     * Formats the text which will be shown when no challenges exist
     * @param chart the chart whose no data text will be formatted
     */
    public void applyNoDataSettings(Chart chart) {
        chart.setNoDataText(mApplication.getString(R.string.chart_no_data_text));
        Paint p = chart.getPaint(Chart.PAINT_INFO);
        p.setTextSize(NO_DATA_TEXT_SIZE);
        p.setColor(ContextCompat.getColor(mApplication.getApplicationContext(), R.color.colorNoDataText));
    }
}
