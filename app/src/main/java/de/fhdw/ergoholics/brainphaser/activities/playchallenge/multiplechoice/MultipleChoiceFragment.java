package de.fhdw.ergoholics.brainphaser.activities.playchallenge.multiplechoice;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import de.fhdw.ergoholics.brainphaser.BrainPhaserComponent;
import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.activities.playchallenge.AnswerFragment;

import java.util.Collections;

/**
 * Fragment for a multiple-choice challenge
 */
public class MultipleChoiceFragment extends AnswerFragment {
    private RecyclerView mRecyclerView;
    private ButtonsAdapter mAdapter;

    @Override
    protected void injectComponent(BrainPhaserComponent component) {
        component.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate the view
        mView = inflater.inflate(R.layout.fragment_challenge_multiple_choice, container, false);

        final int SPANS = 2;
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.buttonsRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), SPANS));

        mAdapter = new ButtonsAdapter(mAnswerList);
        mRecyclerView.setAdapter(mAdapter);

        // Add decorators for margins
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                // Apply vertical margin to all but bottom children
                if (parent.getChildAdapterPosition(view) < parent.getAdapter().getItemCount() - SPANS) {
                    outRect.bottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8.f, getResources().getDisplayMetrics());
                }
            }
        });

        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                // Apply horizontal margin to all but rightmost children
                if (parent.getChildAdapterPosition(view) % SPANS < SPANS - 1) {
                    outRect.right = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8.f, getResources().getDisplayMetrics());
                }
            }
        });

        return mView;
    }

    @Override
    protected void loadAnswers() {
        super.loadAnswers();
        Collections.shuffle(mAnswerList);
    }

    /**
     * Checks if the given answer is correct, displays it and executes the AnswerListener
     */
    @Override
    public void checkAnswers() {
        mListener.onAnswerChecked(mAdapter.performAnswerCheck());
    }
}
