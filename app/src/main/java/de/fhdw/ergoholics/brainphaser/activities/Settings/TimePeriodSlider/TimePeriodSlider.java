package de.fhdw.ergoholics.brainphaser.activities.Settings.TimePeriodSlider;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import de.fhdw.ergoholics.brainphaser.R;

/**
 * Created by funkv on 15.03.2016.
 *
 * GUI component to allow selection of a certain timespan component (e.g. months, days, weeks)
 */
public class TimePeriodSlider extends LinearLayout implements DiscreteSeekBar.OnProgressChangeListener {

    private TextView mCurrentValue;

    /**
     * Interface that should be implemented to react to user changes.
     */
    public interface OnChangeListener {
        /**
         * Called when the user changes the value through the slider.
         * @param timeInSecs The value selected represented in seconds (e.g. 1 Minute = 1000 * 60)
         */
        void onSelectionChanged(long timeInSecs);
    }
    private DiscreteSeekBar mSeekBar;

    public TimePeriodSlider(Context context) {
        super(context);
    }

    private int mDateType;
    private OnChangeListener mOnChangeListener;
    private DateComponent.ComponentInfo mComponentInfo;

    public TimePeriodSlider(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.control_time_period_slider, this);

        TypedArray attrArray = context.obtainStyledAttributes(attrs, new int[]{R.attr.dateComponent});
        mDateType = attrArray.getInt(0, DateComponent.MONTHS);
        mComponentInfo = DateComponent.getInfo(mDateType);
        attrArray.recycle();

        TextView typeText = (TextView) findViewById(R.id.typeText);
        typeText.setText(context.getString(DateComponent.getInfo(mDateType).getResourceId()));

        mCurrentValue = (TextView) findViewById(R.id.currentValue);

        mSeekBar = (DiscreteSeekBar) findViewById(R.id.seekBar);
        mSeekBar.setMin(0);
        mSeekBar.setMax(mComponentInfo.getRangeMax());
        mSeekBar.setIndicatorPopupEnabled(true);
        mSeekBar.setOnProgressChangeListener(this);
    }

    public void setValue(int time) {
        mSeekBar.setProgress(time);
    }

    public void setOnChangeListener(OnChangeListener listener) {
        mOnChangeListener = listener;
    }

    @Override
    public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
        if (fromUser && mOnChangeListener != null) {
            mOnChangeListener.onSelectionChanged(value * mComponentInfo.getSecondFactor());
        }
        mCurrentValue.setText(Integer.toString(value));
    }

    @Override
    public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

    }
}
