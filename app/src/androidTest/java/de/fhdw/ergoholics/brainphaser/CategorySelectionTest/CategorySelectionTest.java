package de.fhdw.ergoholics.brainphaser.CategorySelectionTest;

import android.app.Application;
import android.support.annotation.NonNull;

import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.Espresso.onView;

import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.util.LongSparseArray;
import android.support.v7.widget.RecyclerView;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static org.hamcrest.Matchers.*;

import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

import de.fhdw.ergoholics.brainphaser.BrainPhaserApplication;
import de.fhdw.ergoholics.brainphaser.BrainPhaserComponent;
import de.fhdw.ergoholics.brainphaser.DaggerActivityTestRule;
import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.TestAppModule;
import de.fhdw.ergoholics.brainphaser.TestBrainPhaserApplication;
import de.fhdw.ergoholics.brainphaser.TestUtils;
import de.fhdw.ergoholics.brainphaser.activities.CategorySelect.CategoryAdapter;
import de.fhdw.ergoholics.brainphaser.activities.main.MainActivity;
import de.fhdw.ergoholics.brainphaser.database.CategoryDataSource;
import de.fhdw.ergoholics.brainphaser.database.MockDatabaseModule;
import de.fhdw.ergoholics.brainphaser.logic.DueChallengeLogic;
import de.fhdw.ergoholics.brainphaser.logic.UserLogicFactory;
import de.fhdw.ergoholics.brainphaser.model.Category;
import de.fhdw.ergoholics.brainphaser.model.User;

import android.support.test.espresso.contrib.RecyclerViewActions;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Component;


/**
 * Created by funkv on 05.03.2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class CategorySelectionTest {
    @Singleton
    @Component(modules = {TestAppModule.class, MockDatabaseModule.class})
    interface TestAppComponent extends BrainPhaserComponent {
        void inject(CategorySelectionTest test);
    }

    private TestAppComponent mTestAppComponent;
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
        new DaggerActivityTestRule<>(MainActivity.class, new DaggerActivityTestRule.OnBeforeActivityLaunchedListener<MainActivity>() {
            @Override
            public void beforeActivityLaunched(@NonNull Application application, @NonNull MainActivity activity) {
                mTestAppComponent = DaggerCategorySelectionTest_TestAppComponent.builder()
                    .testAppModule(new TestAppModule((BrainPhaserApplication) application))
                    .mockDatabaseModule(new MockDatabaseModule(application.getApplicationContext(), "TEST"))
                    .build();

                ((TestBrainPhaserApplication) application).setTestComponent(mTestAppComponent);
                setUp();
            }
        });

    private LongSparseArray<Integer> mFakeCounts;
    private List<Category> mFakeCategories;

    @Inject UserLogicFactory mockLogicFactory;
    @Inject CategoryDataSource mockCategoryDataSource;
    DueChallengeLogic mockDueChallengeLogic;

    private SparseArray<Category> mPositions = new SparseArray<>();

    public void setUp() {
        mTestAppComponent.inject(this);

        mFakeCategories = Arrays.asList(
            new Category(1l, "Englisch", "Verbessere deine Englischkenntnisse und dein Wissen über Englischsprachige Länder. Lerne nützliche Phrasen und Umgangsformen.", "@drawable/englisch"),
            new Category(2l, "Architektur", "Verbessere dein Wissen über berühmte Gebäude, Bauarten und Architekturepochen..", "@drawable/architektur"),
            new Category(3l, "Computer", "Lerne neue, coole Fakten über Computer und Informationstechnologie. Du wirst mit Fragen zu Netzwerken, Hardware, Software, Programmiersprachen und Softwareprojekten getestet.", "@drawable/computer"),
            new Category(4l, "Geographie", "Länder, Kulturen und Traditionen.", "@drawable/englisch")
        );
        Mockito.when(mockCategoryDataSource.getAll()).thenReturn(
            mFakeCategories
        );

        mFakeCounts = new LongSparseArray<>();
        mFakeCounts.put(1, 10);
        mFakeCounts.put(2, 0);
        mFakeCounts.put(3, 11);
        mFakeCounts.put(4, 3);
        mockDueChallengeLogic = Mockito.mock(DueChallengeLogic.class);
        Mockito.when(mockDueChallengeLogic.getDueChallengeCounts(Mockito.anyListOf(Category.class))).thenReturn(
            mFakeCounts
        );

        Mockito.when(mockLogicFactory.createDueChallengeLogic(org.mockito.Matchers.any(User.class)))
            .thenReturn(mockDueChallengeLogic);

        // Which category should be at which position (pos -> categoryId)
        mPositions.put(4, mFakeCategories.get(1));
        mPositions.put(3, mFakeCategories.get(3));
        mPositions.put(2, mFakeCategories.get(0));
        mPositions.put(1, mFakeCategories.get(2));
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
                .check(matches(TestUtils.atPosition(position, hasDescendant(withText(categoryName)))));
        }
    }
}
