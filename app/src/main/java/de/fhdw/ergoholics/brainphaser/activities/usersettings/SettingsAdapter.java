package de.fhdw.ergoholics.brainphaser.activities.usersettings;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.Date;

import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.activities.usersettings.slidertimeperiod.TimePeriodSlider;
import de.fhdw.ergoholics.brainphaser.database.SettingsDataSource;
import de.fhdw.ergoholics.brainphaser.logic.SettingsLogic;
import de.fhdw.ergoholics.brainphaser.model.Settings;

/**
 * Created by funkv on 15.03.2016.
 *
 * Adapter for creating a settings panel for each stage
 */
public class SettingsAdapter extends RecyclerView.Adapter<SettingsViewHolder> {
    /**
     * Needs to be implemented to react to changed settings
     */
    public interface OnSettingsChangedListener {
        /**
         * Called when the constructor-passed settings object has been changed.
         * @param adapter adapter that triggered the change
         */
        void onSettingsChanged(SettingsAdapter adapter);
    }

    private OnSettingsChangedListener mListener;

    private Settings mSettings;
    private SettingsLogic mSettingsLogic;

    public SettingsAdapter(Settings settings, OnSettingsChangedListener listener,
                           SettingsLogic logic) {
        mListener = listener;
        mSettings = settings;
        mSettingsLogic = logic;
    }

    /**
     * Called to create the ViewHolder at the given position.
     *
     * @param parent   parent to assign the newly created view to
     * @param viewType ignored
     */
    @Override
    public SettingsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater myInflater = LayoutInflater.from(parent.getContext());

        //Load the list template
        View customView = myInflater.inflate(R.layout.list_item_setting, parent, false);

        LinearLayout layout = (LinearLayout)customView.findViewById(R.id.sliderContainer);

        SparseArray<TimePeriodSlider> sliders = new SparseArray<>();
        for (int dateType : SettingsViewHolder.COMPONENT_SLIDERS_TO_CREATE) {
            TimePeriodSlider periodSlider = new TimePeriodSlider(layout.getContext());
            periodSlider.setDateType(dateType);
            layout.addView(periodSlider);

            sliders.put(dateType, periodSlider);
        }
        return new SettingsViewHolder(customView, sliders, this);
    }

    /**
     * Called to bind the ViewHolder at the given position.
     *
     * @param holder   the ViewHolder object to be bound
     * @param position the position of the ViewHolder
     */
    @Override
    public void onBindViewHolder(SettingsViewHolder holder, int position) {
        int stage = position + 1;
        holder.bindStage(SettingsDataSource.getTimeboxByStage(mSettings, stage), stage);
    }

    /**
     * Returns the count of ViewHolders in the adapter
     *
     * @return the count of ViewHolders
     */
    @Override
    public int getItemCount() {
        return SettingsDataSource.STAGE_COUNT;
    }

    /**
     * Check whether the given duration is valid for a given stage.
     * @param stage stage to check
     * @param durationMsec duration for this stage
     * @return null if this is a valid value, error string id if it is invalid
     */
    public Integer isTimeValidForStage(int stage, long durationMsec) {
        return mSettingsLogic.isTimeValidForStage(mSettings, stage, durationMsec);
    }

    /**
     * Returns the settings attribute
     *
     * @return the setting attribute of the activity
     */
    public Settings getSettings() {
        return mSettings;
    }

    /**
     * Called by ViewHolder to notify a change
     * @param stage stage number that changed
     * @param durationMsec the new duration in milliseconds
     */
    void stageTimeSaved(int stage, long durationMsec) {
        SettingsDataSource.setTimeboxByStage(mSettings, stage, new Date(durationMsec));
        notifyDataSetChanged();
    }
}
