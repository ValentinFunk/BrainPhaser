package de.fhdw.ergoholics.brainphaser.activities.Settings;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.activities.Settings.TimePeriodSlider.DateComponent;
import de.fhdw.ergoholics.brainphaser.activities.Settings.TimePeriodSlider.TimePeriodSlider;
import de.fhdw.ergoholics.brainphaser.database.SettingsDataSource;
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
         * Called when the timespan for a specific stage changes.
         * @param stage number of the stage that was updated (1-6)
         * @param seconds timespan represented in seconds
         */
        void onStageChanged(int stage, int seconds);
    }

    private OnSettingsChangedListener mListener;
    private Settings mSettings;

    public SettingsAdapter(Settings settings, OnSettingsChangedListener listener) {
        mListener = listener;
        mSettings = settings;
    }

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

    @Override
    public void onBindViewHolder(SettingsViewHolder holder, int position) {
        int stage = position + 1;
        holder.bindStage(SettingsDataSource.getTimeboxByStage(mSettings, stage), stage);
    }

    @Override
    public int getItemCount() {
        return SettingsDataSource.STAGE_COUNT;
    }
}
