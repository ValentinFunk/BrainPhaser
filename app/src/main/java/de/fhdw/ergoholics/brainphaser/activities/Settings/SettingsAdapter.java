package de.fhdw.ergoholics.brainphaser.activities.Settings;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.database.SettingsDataSource;
import de.fhdw.ergoholics.brainphaser.model.Settings;

/**
 * Created by funkv on 15.03.2016.
 *
 * Adapter for creating a settings panel for each stage
 */
public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.SettingsViewHolder> {
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

    class SettingsViewHolder extends RecyclerView.ViewHolder {
        private SettingsAdapter mAdapter;
        private TextView mTitle;
        private TextView mTime;

        public SettingsViewHolder(View itemView, SettingsAdapter adapter) {
            super(itemView);
            mAdapter = adapter;

            mTitle = (TextView)itemView.findViewById(R.id.stageTitle);
            mTime = (TextView) itemView.findViewById(R.id.stageTime);
        }

        public void bindStage(Date selectedDate, int stage) {
            mTitle.setText(itemView.getResources().getString(R.string.setting_stage, stage));
            mTime.setText(SimpleDateFormat.getTimeInstance().format(selectedDate));
        }
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
        return new SettingsViewHolder(customView, this);
    }

    @Override
    public void onBindViewHolder(SettingsViewHolder holder, int position) {
        int stage = position + 1;
        holder.bindStage(SettingsDataSource.getTimeboxByStage(mSettings, stage), stage);
    }

    @Override
    public int getItemCount() {
        return 6;
    }
}
