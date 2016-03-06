package de.fhdw.ergoholics.brainphaser.logic.statistics;

import android.graphics.Color;
import android.graphics.Paint;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import java.util.ArrayList;

import de.fhdw.ergoholics.brainphaser.database.ChallengeDataSource;
import de.fhdw.ergoholics.brainphaser.database.CompletionDataSource;
import de.fhdw.ergoholics.brainphaser.logic.due.DueChallengeLogic;
import de.fhdw.ergoholics.brainphaser.model.User;

/**
 * Created by Daniel Hoogen on 05/03/2016.
 *
 * This class contains the logic for creating statistics about due challenges and challenge stages
 */
public class StatisticsLogic {
    //Constants
    private static final int[] COLORSET = {
        0xFF303F9F, 0xFFC5CAE9, 0xFFEEEEEE, 0xFFFF4081, 0xFF727272, 0xFFB6B6B6
    };
    private static int NO_DATA_TEXT_COLOR = Color.BLACK;

    private static final float SLICE_SPACE = 1f;
    private static final float SELECTION_SHIFT = 0f;

    private static final float CENTER_TEXT_SIZE = 8f;
    private static final float NO_DATA_TEXT_SIZE = 30f;

    private static final String DUE_CHART_CENTER_TEXT = "Fälligkeit";
    private static final String STAGE_CHART_CENTER_TEXT = "Klassen";
    private static final String NO_DATA_TEXT = "Keine Challenges vorhanden!";

    private static final String CHALLENGE_DUE_TEXT = "Ja";
    private static final String CHALLENGE_NOT_DUE_TEXT = "Nein";
    private static final String STAGE_OTHERS_TEXT = "Andere";

    /**
     * Fills the given pie chart with data for visualizing the shares of due and not due challenges.
     * The data for the given user and the given category is shown.
     * @param chart the chart component to be filled with data and to be formatted
     * @param currentUser the user whose due challenges will be visualized
     * @param categoryId the id of the category whose due challenges will be visualized
     */
    public void fillDueChart(PieChart chart, User currentUser, long categoryId)
    {
        //Clear the chart for reloading
        chart.clear();

        //Calculate numbers for the data to be visualized
        int dueNumber = DueChallengeLogic.getDueChallenges(currentUser, categoryId).size();
        int notDueNumber = ChallengeDataSource.getByCategoryId(categoryId).size() - dueNumber;

        if (dueNumber + notDueNumber > 0) {
            //Create dataset
            ArrayList <Entry> entries = new ArrayList<>();
            ArrayList<String> labels = new ArrayList<>();

            labels.add(CHALLENGE_DUE_TEXT);
            entries.add(new Entry(dueNumber > 0 ? dueNumber : nullValue(notDueNumber), 0));

            labels.add(CHALLENGE_NOT_DUE_TEXT);
            entries.add(new Entry(notDueNumber > 0 ? notDueNumber : nullValue(dueNumber), 1));

            PieDataSet dataset = new PieDataSet(entries, "");
            applyDataSetSettings(dataset);

            //Create data
            PieData data = new PieData(labels, dataset);
            applyDataSettings(data);

            //Add data to chart
            chart.setData(data);

            //Format the chart
            chart.setCenterText(DUE_CHART_CENTER_TEXT);
            applyChartSettings(chart);
        }
        else
            applyNoDataText(chart);
    }

    /**
     * Fills the given pie chart with data for visualizing the shares of the different stages. The
     * data for the given user and the given category is shown.
     * @param chart the chart component to be filled with data and to be formatted
     * @param currentUser the user whose stage shares will be visualized
     * @param categoryId the id of the category whose stage shares will be visualized
     */
    public void fillStageChart(PieChart chart, User currentUser, long categoryId) {
        //Clear the chart for reloading
        chart.clear();

        //Calculate numbers for the data to be visualized
        int numbers[] = {0,0,0,0,0,0};
        int totalNumber = 0;
        int zeroValueCount = 0;

        for (int i = 0; i <=5; i++) {
            numbers[i] = CompletionDataSource.getByUserAndStageAndCategory(currentUser, i + 1, categoryId).size();
            if (numbers[i]==0)
                zeroValueCount++;
            totalNumber += numbers[i];
        }

        if (totalNumber > 0) {
            //Create dataset
            ArrayList <Entry> entries = new ArrayList<>();
            ArrayList<String> labels = new ArrayList<>();

            for (int i = 0; i <=5; i++) {
                if (numbers[i] > 0) {
                    entries.add(new Entry(numbers[i], i));
                    labels.add("" + (i+1));
                }
            }
            if (zeroValueCount==5) {
                entries.add(new Entry(nullValue(totalNumber), 6));
                labels.add(STAGE_OTHERS_TEXT);
            }

            PieDataSet dataset = new PieDataSet(entries, "");
            applyDataSetSettings(dataset);

            //Create data
            PieData data = new PieData(labels, dataset);
            applyDataSettings(data);

            //Add data to chart
            chart.setData(data);

            //Format the chart
            chart.setCenterText(STAGE_CHART_CENTER_TEXT);
            applyChartSettings(chart);
        }
        else
            applyNoDataText(chart);
    }

    /**
     * Applies the specified format to the PieChart Object.
     * @param chart the chart which will be formatted
     */
    private void applyChartSettings(PieChart chart) {
        chart.setCenterTextSize(CENTER_TEXT_SIZE);

        chart.setUsePercentValues(true);
        chart.setDrawSliceText(false);

        chart.setDescription("");

        chart.getLegend().setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
    }

    /**
     * Applies the specified format to the PieDataSet Object.
     * @param dataset the dataset which will be formatted
     */
    private void applyDataSetSettings(PieDataSet dataset) {
        dataset.setSliceSpace(SLICE_SPACE);
        dataset.setSelectionShift(SELECTION_SHIFT);
        dataset.setColors(COLORSET);
        dataset.setValueFormatter(new CustomizedPercentFormatter());
    }

    /**
     * Applies the specified format to the PieData Object.
     * @param data the data which will be formatted
     */
    private void applyDataSettings(PieData data) {
        data.setDrawValues(true);
    }

    /**
     * Formats the text which will be shown when no challenges exist
     * @param chart the chart whose no data text will be formatted
     */
    private void applyNoDataText(Chart chart) {
        chart.setNoDataText(NO_DATA_TEXT);
        Paint p = chart.getPaint(Chart.PAINT_INFO);
        p.setTextSize(NO_DATA_TEXT_SIZE);
        p.setColor(NO_DATA_TEXT_COLOR);
    }

    /**
     * Creates a value between 0% and 1% which will be shown as 0% if rounded. This method is
     * necessary due to a bug in the statistics library, which disallows the user to create a pie
     * chart with 0% values or with only 1 date to be shown.
     * @param totalValue the total value of items in the pie chart
     * @return the value which is bigger than 0% but will be shown as 0%
     */
    private float nullValue(float totalValue) {
        return totalValue/200f;
    }
}
