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

import de.fhdw.ergoholics.brainphaser.BrainPhaserComponent;
import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.activities.playchallenge.AnswerFragment;

import java.util.Arrays;
import java.util.Collections;

/**
 * Fragment for a multiple-choice challenge
 */
public class MultipleChoiceFragment extends AnswerFragment {
    private static final String KEY_BUTTONS_STATE = "KEY_BUTTONS_STATE";
    private static final String KEY_ANSWERS_CHECKED = "KEY_ANSWERS_CHECKED";

    private RecyclerView mRecyclerView;
    private ButtonsAdapter mAdapter;
    private GridLayoutManager mLayoutManager;

    // State
    private boolean mAnswersChecked = false;
    private ButtonViewState[] mStateHolder;

    private static Bundle mBundleRecyclerViewState;

    @Override
    protected void injectComponent(BrainPhaserComponent component) {
        component.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate the view
        View view = inflater.inflate(R.layout.fragment_challenge_multiple_choice, container, false);

        final int SPANS = 2;
        mRecyclerView = (RecyclerView) view.findViewById(R.id.buttonsRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getContext(), SPANS);
        mRecyclerView.setLayoutManager(mLayoutManager);

        if (savedInstanceState != null) {
            mStateHolder = (ButtonViewState[]) savedInstanceState.getParcelableArray(KEY_BUTTONS_STATE);
            mAnswersChecked = savedInstanceState.getBoolean(KEY_ANSWERS_CHECKED);
        } else {
            mStateHolder = new ButtonViewState[mAnswerList.size()];
            Collections.shuffle(mAnswerList);
            for (int i = 0; i < mAnswerList.size(); i++) {
                mStateHolder[i] = new ButtonViewState(mAnswerList.get(i));
            }
        }

        mAdapter = new ButtonsAdapter(Arrays.asList(mStateHolder));
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
                    outRect.right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8.f, getResources().getDisplayMetrics());
                }
            }
        });


        if (mAnswersChecked) {
            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    mAdapter.performAnswerCheck();
                }
            });
        }

        return view;
    }

    /**
     * Checks if the given answer is correct, displays it and passes the event on to the activity
     */
    @Override
    public AnswerFragment.ContinueMode goToNextState() {
        mListener.onAnswerChecked(mAdapter.performAnswerCheck(), false);
        mAnswersChecked = true;
        return ContinueMode.CONTINUE_SHOW_FAB;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArray(KEY_BUTTONS_STATE, mStateHolder);
        outState.putBoolean(KEY_ANSWERS_CHECKED, mAnswersChecked);
    }
}
