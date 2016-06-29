package de.fhdw.ergoholics.brainphaser.logic.statistics;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by Daniel Hoogen on 06/03/2016.
 * <p/>
 * This class implements a ValueFormatter for formatting values for pie charts used in the app
 */
public class CustomizedFormatter implements ValueFormatter {
    //Constants
    private static final String VALUE_FORMAT = "#,###,##0.######";

    //Attributes
    DecimalFormat mFormat;

    /**
     * Standard constructor which created the decimal format
     */
    public CustomizedFormatter() {
        //Create a decimal format
        mFormat = (DecimalFormat) NumberFormat.getNumberInstance(Locale.GERMANY);
        mFormat.applyPattern(VALUE_FORMAT);
    }

    /**
     * Formats a value to be shown in a pie chart. Values with decimal places will be hidden,
     * because they are unexpected. This also affects dummy values, which are shown as small slices.
     *
     * @param value the value to be formatted
     * @return String containing the formatted value
     */
    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex,
                                    ViewPortHandler viewPortHandler) {
        String valueText = mFormat.format(value);
        String[] valueParts = valueText.split(",");
        //If the value is 0 the value will be hidden for avoiding visual bugs
        if (valueParts.length != 1) {
            return "";
            //Otherwise the result will be returned
        } else {
            return valueParts[0];
        }
    }
}