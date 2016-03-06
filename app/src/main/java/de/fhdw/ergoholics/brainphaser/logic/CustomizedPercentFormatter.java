package de.fhdw.ergoholics.brainphaser.logic;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * Created by Daniel on 06/03/2016.
 */
public class CustomizedPercentFormatter implements ValueFormatter {
    private static final String VALUE_FORMAT = "##0";

    DecimalFormat mFormat;

    public CustomizedPercentFormatter() {
        mFormat = new DecimalFormat(VALUE_FORMAT);
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        String valueText = mFormat.format(value);
        if (valueText.equals("0"))
            return "";
        else
            return valueText + " %";
    }
}
