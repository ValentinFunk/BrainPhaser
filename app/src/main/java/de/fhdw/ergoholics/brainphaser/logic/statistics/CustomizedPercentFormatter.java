package de.fhdw.ergoholics.brainphaser.logic.statistics;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * Created by Daniel on 06/03/2016.
 *
 * This class implements a ValueFormatter for formatting values for pie charts used in the app
 */
public class CustomizedPercentFormatter implements ValueFormatter {
    //Constants
    private static final String VALUE_FORMAT = "##0";

    //Attributes
    DecimalFormat mFormat;

    //Constructors
    public CustomizedPercentFormatter() {
        //Creates a decimal format without decimal places
        mFormat = new DecimalFormat(VALUE_FORMAT);
    }

    /**
     * Formats a percent value to be shown in a pie chart. 0% values will result in an empty String
     * to be returned, for hiding these values in the charts.
     * @param value the value to be formatted
     * @return String containing the formatted value
     */
    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        String valueText = mFormat.format(value);
        //If the value is 0% the value will be hidden for avoiding visual bugs
        if (valueText.equals("0"))
            return "";
        //Otherwise a % character will be added and the result will be returned
        else
            return valueText + " %";
    }
}
