package de.fhdw.ergoholics.brainphaser.activities.usersettings.slidertimeperiod;

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
    private DiscreteSeekBar mSeekBar;
    private int mDateType;
    private OnChangeListener mOnChangeListener;
    private DateComponent.ComponentInfo mComponentInfo;
    private TextView mTypeText;

    /**
     * Interface that should be implemented to react to user changes.
     */
    public interface OnChangeListener {
        /**
         * Called when the user changes the value through the slider.
         * @param slider The calling slider
         * @param value the new value
         */
        void onSelectionChanged(TimePeriodSlider slider, int value);
    }

    public TimePeriodSlider(Context context) {
        super(context);
        init();
    }

    public TimePeriodSlider(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        readAttributes(attrs);
    }

    public TimePeriodSlider(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
        readAttributes(attrs);
    }

    private void readAttributes(AttributeSet attrs) {
        // Read the correct DateComponent out of the attribute passed via XML
        TypedArray attrArray = getContext().obtainStyledAttributes(attrs, R.styleable.TimePeriodSlider, 0, 0);
        try {
            mDateType = attrArray.getInteger(R.styleable.TimePeriodSlider_dateComponent, DateComponent.WEEKS);
            setDateType(mDateType);
        } finally {
            attrArray.recycle();
        }
    }

    public void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.control_time_period_slider, this, true);
        this.setOrientation(VERTICAL);

        mSeekBar = (DiscreteSeekBar) findViewById(R.id.seekBar);
        mTypeText = (TextView) findViewById(R.id.typeText);
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
            mOnChangeListener.onSelectionChanged(this, value);
        }

        mCurrentValue.setText(String.format("%d", value));
    }

    @Override
    public void onStartTrackingTouch(DiscreteSeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
    }

    public int getDateType() {
        return mDateType;
    }

    /**
     * Set the Date type/unit to represent.
     *
     * @param dateType one of DateComponent.* (e.g. DateComponent.DAYS)
     */
    public void setDateType(int dateType) {
        mDateType = dateType;
        mComponentInfo = DateComponent.getInfo(mDateType);
        if (mComponentInfo == null) {
            throw new RuntimeException("Invalid date type");
        }

        // Update view:

        // Set the correct text
        mTypeText.setText(getContext().getString(mComponentInfo.getResourceId()));
        mCurrentValue = (TextView) findViewById(R.id.currentValue);
        // Set up the DiscreteSeekBar
        mSeekBar.setMin(0);
        mSeekBar.setMax(mComponentInfo.getRangeMax());
    }
}
