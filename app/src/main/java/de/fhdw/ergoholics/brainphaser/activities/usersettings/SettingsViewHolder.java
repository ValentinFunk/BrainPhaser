package de.fhdw.ergoholics.brainphaser.activities.usersettings;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.joda.time.Duration;
import org.joda.time.DurationFieldType;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.util.Date;

import de.fhdw.ergoholics.brainphaser.BrainPhaserApplication;
import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.activities.usersettings.slidertimeperiod.DateComponent;
import de.fhdw.ergoholics.brainphaser.activities.usersettings.slidertimeperiod.TimePeriodSlider;

/**
 * Created by funkv on 15.03.2016.
 *
 * View Holder for the individual stage settings
 */
class SettingsViewHolder extends RecyclerView.ViewHolder implements TimePeriodSlider.OnChangeListener {
    public final static int[] COMPONENT_SLIDERS_TO_CREATE = new int[]{
        DateComponent.WEEKS,
        DateComponent.DAYS,
        DateComponent.HOURS,
        DateComponent.MINUTES
    };

    private SettingsAdapter mAdapter;
    private TextView mTitle;
    private TextView mTime;
    private Button mButton;
    private CollapsingLinearLayout mErrorLayout;
    private TextView mErrorText;
    private SparseArray<TimePeriodSlider> mTimePeriodSliders; // SparseArray mapping dateTypes from DateComponent to their respective slider
    private CollapsingLinearLayout mConfigArea;
    private ImageButton mExpandButton;

    private final PeriodFormatter mFormatter;
    private Period mPeriod;
    private Duration mDuration;
    private Date mInitialDate;

    public int getStage() {
        return mStage;
    }

    private int mStage;
    private final SparseArray<DurationFieldType> mConversion; // Map between representations or DateComponent and DurationFieldType

    /**
     * Create a ViewHolder for a stage object
     *
     * @param itemView inflated list_item_setting
     * @param sliders  SparseArray mapping dateTypes from DateComponent to their respective
     *                 slider
     * @param adapter  adapter to pass callbacks on to
     */
    public SettingsViewHolder(View itemView,
                              SparseArray<TimePeriodSlider> sliders,
                              SettingsAdapter adapter) {
        super(itemView);
        mAdapter = adapter;

        // Map between representations
        mConversion = new SparseArray<>();
        mConversion.put(DateComponent.WEEKS, DurationFieldType.weeks());
        mConversion.put(DateComponent.DAYS, DurationFieldType.days());
        mConversion.put(DateComponent.HOURS, DurationFieldType.hours());
        mConversion.put(DateComponent.MINUTES, DurationFieldType.minutes());

        mTitle = (TextView) itemView.findViewById(R.id.stageTitle);
        mTime = (TextView) itemView.findViewById(R.id.stageTime);
        mButton = (Button) itemView.findViewById(R.id.saveButton);
        mErrorLayout = (CollapsingLinearLayout) itemView.findViewById(R.id.errorLayout);
        mErrorText = (TextView) itemView.findViewById(R.id.errorText);
        mConfigArea = (CollapsingLinearLayout) itemView.findViewById(R.id.config_area);
        mExpandButton = (ImageButton) itemView.findViewById(R.id.expand_settings_button);

        // Intialize the formater
        Resources res = itemView.getResources();
        mFormatter = new PeriodFormatterBuilder()
            .appendWeeks().appendSuffix(" ").appendSuffix(res.getString(R.string.formatter_week), res.getString(R.string.formatter_weeks))
            .appendSeparator(", ")
            .appendDays().appendSuffix(" ").appendSuffix(res.getString(R.string.formatter_day), res.getString(R.string.formatter_days))
            .appendSeparator(", ")
            .appendHours().appendSuffix(" ").appendSuffix(res.getString(R.string.formatter_hour), res.getString(R.string.formatter_hours))
            .appendSeparator(", ")
            .appendMinutes().appendSuffix(" ").appendSuffix(res.getString(R.string.formatter_minute), res.getString(R.string.formatter_minutes))
            .printZeroNever()
            .toFormatter();

        mTimePeriodSliders = sliders;
        for (int i = 0; i < sliders.size(); i++) {
            sliders.valueAt(i).setOnChangeListener(this);
        }

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mButton.isEnabled()) {
                    mAdapter.stageTimeSaved(mStage, mDuration.getMillis());
                    mConfigArea.collapse();
                }
            }
        });

        final int expand = itemView.getResources().getIdentifier("ic_expand_more_white_24dp", "drawable", BrainPhaserApplication.PACKAGE_NAME);
        final int collapse = itemView.getResources().getIdentifier( "ic_expand_less_white_24dp","drawable", BrainPhaserApplication.PACKAGE_NAME);

        mConfigArea.setVisibility(View.GONE);
        mConfigArea.setOnChangeListener(new CollapsingLinearLayout.OnChangeListener() {
            @Override
            public void onExpand() {
                mExpandButton.setImageResource(collapse);
            }

            @Override
            public void onCollapse() {
                mExpandButton.setImageResource(expand);
            }
        });

        View.OnClickListener expander = new View.OnClickListener() {
            public void onClick(View v) {
                if (mConfigArea.isCollapsing()) {
                    mConfigArea.expand();

                } else {
                    mConfigArea.collapse();
                }
            }
        };

        LinearLayout header = (LinearLayout) itemView.findViewById(R.id.header);
        header.setOnClickListener(expander);
        mExpandButton.setOnClickListener(expander);

        // Reset and collapse on abort
        final SettingsViewHolder self = this;
        Button cancelButton = (Button) itemView.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                self.resetAndCollapse();
            }
        });
    }

    /**
     * Resets and collapses this view holder
     */
    public void resetAndCollapse() {
        mConfigArea.collapse();
        bindStage(mInitialDate, mStage); //Reset
    }

    /**
     * Formats the period into a language string (e.g. "1 hour, 2 minutes, 30 seconds")
     *
     * @return formatted string
     */
    private String getFormattedPeriod() {
        return mFormatter.print(mPeriod);
    }

    /**
     * Creates a Joda duration/period object
     *
     * @param date object that represents the duration of the period through milisseconds
     */
    private void initializeDuration(Date date) {
        // Get duration from Date
        mDuration = new Duration(date.getTime());

        // Get period
        final DurationFieldType[] durationFields = new DurationFieldType[]{DurationFieldType.weeks(), DurationFieldType.days(), DurationFieldType.hours(), DurationFieldType.minutes()};
        mPeriod = mDuration.toPeriod(PeriodType.forFields(durationFields)).normalizedStandard();
        updatePeriod();
    }

    /**
     * Updates the view to reflect changes in the period object
     */
    private void updatePeriod() {
        // Apply values from period to the sliders
        for (int i = 0; i < mConversion.size(); i++) {
            int dateType = mConversion.keyAt(i);
            TimePeriodSlider slider = mTimePeriodSliders.get(dateType);
            slider.setValue(mPeriod.get(mConversion.valueAt(i)));
        }
        mTime.setText(getFormattedPeriod());
        mDuration = mPeriod.toStandardDuration();

        doValidation();
    }

    /**
     * Validate Period and update view with error
     */
    private void doValidation() {
        Integer error = mAdapter.isTimeValidForStage(mStage, mDuration.getMillis());
        if (error == null) {
            mButton.setEnabled(true);
            mErrorLayout.collapse();

        } else {
            mButton.setEnabled(false);
            mErrorText.setText(itemView.getResources().getString(error));
            mErrorLayout.expand();
        }
    }

    /**
     * Bind a stage to this ViewHolder
     * @param currentDate date that represents the current period for this stage
     * @param stage stage number to modify
     */
    public void bindStage(Date currentDate, int stage) {
        mStage = stage;
        mTitle.setText(itemView.getResources().getString(R.string.setting_stage, stage));
        mInitialDate = currentDate;
        initializeDuration(currentDate);
        mButton.setEnabled(false);
        mErrorLayout.setVisibility(View.GONE);
        mErrorLayout.collapse();
    }

    /**
     * This method is called when the selection in the view holder has changed
     *
     * @param slider The calling slider
     * @param value the new value
     */
    @Override
    public void onSelectionChanged(TimePeriodSlider slider, int value) {
        DurationFieldType fieldType = mConversion.get(slider.getDateType());
        mPeriod = mPeriod.withField(fieldType, value);
        updatePeriod();
    }
}
