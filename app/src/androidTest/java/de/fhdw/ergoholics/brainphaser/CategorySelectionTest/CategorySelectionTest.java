package de.fhdw.ergoholics.brainphaser.CategorySelectionTest;

import android.support.annotation.IdRes;
import android.support.test.espresso.Espresso;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;

import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.util.LongSparseArray;
import android.support.v7.widget.RecyclerView;
import android.test.ActivityInstrumentationTestCase2;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static org.hamcrest.Matchers.*;

import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.TestUtils;
import de.fhdw.ergoholics.brainphaser.activities.CategorySelect.CategoryAdapter;
import de.fhdw.ergoholics.brainphaser.activities.main.MainActivity;
import de.fhdw.ergoholics.brainphaser.database.CategoryDataSource;
import de.fhdw.ergoholics.brainphaser.logic.DueChallengeLogic;
import de.fhdw.ergoholics.brainphaser.model.Category;
import android.support.test.espresso.contrib.RecyclerViewActions;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;


/**
 * Created by funkv on 05.03.2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class CategorySelectionTest {

    private LongSparseArray<Integer> mFakeCounts;
    private List<Category> mFakeCategories;

    public static class ViewMatchers
    {
        @SuppressWarnings("unchecked")
        public static Matcher<View> withRecyclerView(@IdRes int viewId)
        {
            return allOf(isAssignableFrom(RecyclerView.class), withId(viewId));
        }

        @SuppressWarnings("unchecked")
        public static ViewInteraction onRecyclerItemView(@IdRes int identifyingView, Matcher<View> identifyingMatcher, Matcher<View> childMatcher)
        {
            Matcher<View> itemView = allOf(withParent(withRecyclerView(R.id.recyclerView)),
                withChild(allOf(withId(identifyingView), identifyingMatcher)));
            return Espresso.onView(allOf(isDescendantOfA(itemView), childMatcher));
        }
    }

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    private CategoryDataSource tCategoryDataSource = Mockito.mock(CategoryDataSource.class);
    private DueChallengeLogic tDueChallengeLogic = Mockito.mock(DueChallengeLogic.class);
    private SparseArray<Category> mPositions = new SparseArray<>();

    @Before public void setUp() {
        mFakeCategories = Arrays.asList(
            new Category(1l, "Englisch", "Verbessere deine Englischkenntnisse und dein Wissen über Englischsprachige Länder. Lerne nützliche Phrasen und Umgangsformen.", "@drawable/englisch"),
            new Category(2l, "Architektur", "Verbessere dein Wissen über berühmte Gebäude, Bauarten und Architekturepochen..", "@drawable/architektur"),
            new Category(3l, "Computer", "Lerne neue, coole Fakten über Computer und Informationstechnologie. Du wirst mit Fragen zu Netzwerken, Hardware, Software, Programmiersprachen und Softwareprojekten getestet.", "@drawable/computer"),
            new Category(4l, "Geographie", "Länder, Kulturen und Traditionen.", "@drawable/englisch")
        );
        Mockito.when(tCategoryDataSource.getAll()).thenReturn(
            mFakeCategories
        );

        mFakeCounts = new LongSparseArray<Integer>();
        mFakeCounts.put(1, 10);
        mFakeCounts.put(2, 0);
        mFakeCounts.put(3, 11);
        mFakeCounts.put(4, 3);
        Mockito.when(tDueChallengeLogic.getDueChallengeCounts(mFakeCategories)).thenReturn(
            mFakeCounts
        );

        // Which category should be at which position (pos -> categoryId)
        mPositions.put(4, mFakeCategories.get(1));
        mPositions.put(3, mFakeCategories.get(3));
        mPositions.put(2, mFakeCategories.get(0));
        mPositions.put(1, mFakeCategories.get(2));

        Log.d("Test", "Mocking");
    }

    @Test
    public void checkFirstElementAllCategories() {
        onView(withId(R.id.recyclerView))
            .check(matches(TestUtils.atPosition(0, notNullValue(View.class))))
            .check(matches(TestUtils.atPosition(0, hasDescendant(withText(R.string.all_categories)))));
    }

    public static Matcher<RecyclerView.ViewHolder> categoryVH(final String catName) {
        return new BoundedMatcher<RecyclerView.ViewHolder, CategoryAdapter.ViewHolder>(CategoryAdapter.ViewHolder.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("category title is " + catName);
            }

            @Override
            protected boolean matchesSafely(CategoryAdapter.ViewHolder item) {
                return item.getTitle().getText().equals(catName);
            }
        };
    }

    @Test
    public void checkAllCategoriesShown( ) {
        int i = 0;
        for (Category c : mFakeCategories) {
            Matcher<View> hasDescendantTitle = hasDescendant(withText(c.getTitle()));
            onView(withId(R.id.recyclerView))
                .perform(RecyclerViewActions.scrollToHolder(categoryVH(c.getTitle())))
                .check(matches(hasDescendantTitle));
        }
    }

    @Test
    public void checkListOrdering() {
        for (int i = 0; i < mPositions.size(); i++) {
            int position = mPositions.keyAt(i);
            String categoryName = mPositions.get(position).getTitle();
            onView(withId(R.id.recyclerView))
                .check(matches(TestUtils.atPosition(0, hasDescendant(withText(categoryName)))));
        }
    }
}
