package de.fhdw.ergoholics.brainphaser.logic;

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
import de.fhdw.ergoholics.brainphaser.model.User;

/**
 * Created by Daniel Hoogen on 05/03/2016.
 */
public class StatisticsLogic {
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

    public void fillDueChart(PieChart chart, User currentUser, long categoryId)
    {
        chart.clear();

        int dueNumber = DueChallengeLogic
                .getDueChallenges(currentUser, categoryId).size();
        int notDueNumber = ChallengeDataSource.getByCategoryId(categoryId).size() - dueNumber;

        if (dueNumber + notDueNumber > 0) {
            ArrayList <Entry> entries = new ArrayList<>();
            ArrayList<String> labels = new ArrayList<>();

            labels.add(CHALLENGE_DUE_TEXT);
            entries.add(new Entry(dueNumber > 0 ? dueNumber : nullValue(notDueNumber), 0));

            labels.add(CHALLENGE_NOT_DUE_TEXT);
            entries.add(new Entry(notDueNumber > 0 ? notDueNumber : nullValue(dueNumber), 1));

            PieDataSet dataset = new PieDataSet(entries, "");
            dataset.setSliceSpace(SLICE_SPACE);
            dataset.setSelectionShift(SELECTION_SHIFT);
            dataset.setColors(COLORSET);
            dataset.setValueFormatter(new CustomizedPercentFormatter());

            PieData pieData = new PieData(labels, dataset);
            pieData.setDrawValues(true);

            chart.setData(pieData);

            chart.setCenterText(DUE_CHART_CENTER_TEXT);
            chart.setCenterTextSize(CENTER_TEXT_SIZE);
            chart.setUsePercentValues(true);
            chart.setDrawSliceText(false);
            chart.setDescription("");
            chart.getLegend().setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        }
        else
        {
            chart.setNoDataText(NO_DATA_TEXT);
            Paint p = chart.getPaint(Chart.PAINT_INFO);
            p.setTextSize(NO_DATA_TEXT_SIZE);
            p.setColor(NO_DATA_TEXT_COLOR);
        }
    }

    public void fillStageChart(PieChart chart, User currentUser, long categoryId) {
        chart.clear();

        int numbers[] = {0,0,0,0,0,0};
        int totalNumber = 0;
        int zeroValueCount = 0;

        for (int i = 0; i <=5; i++) {
            numbers[i] = CompletionDataSource.getByUserAndStageAndCategory(currentUser, i+1, categoryId).size();
            if (numbers[i]==0)
                zeroValueCount++;
            totalNumber += numbers[i];
        }

        if (totalNumber > 0) {
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
                labels.add("Andere");
            }

            PieDataSet dataset = new PieDataSet(entries, "");
            dataset.setSliceSpace(SLICE_SPACE);
            dataset.setSelectionShift(SELECTION_SHIFT);
            dataset.setColors(COLORSET);
            dataset.setValueFormatter(new CustomizedPercentFormatter());

            PieData pieData = new PieData(labels, dataset);
            pieData.setDrawValues(true);

            chart.setData(pieData);

            chart.setCenterText(STAGE_CHART_CENTER_TEXT);
            chart.setCenterTextSize(CENTER_TEXT_SIZE);
            chart.setUsePercentValues(true);
            chart.setDrawSliceText(false);
            chart.setDescription("");
            chart.getLegend().setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        }
        else
        {
            chart.setNoDataText(NO_DATA_TEXT);
            Paint p = chart.getPaint(Chart.PAINT_INFO);
            p.setTextSize(NO_DATA_TEXT_SIZE);
            p.setColor(NO_DATA_TEXT_COLOR);
        }
    }

    private float nullValue(float totalValue) {
        return totalValue/200f;
    }
}
