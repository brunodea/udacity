package udabake.brunodea.com.udabake;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import udabake.brunodea.com.udabake.model.RecipeModel;
import udabake.brunodea.com.udabake.model.RecipeStepModel;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class RecipeStepsAndroidTest {
    private RecipeModel mRecipeModelTest;
    private static final int DEFAULT_NUM_STEPS = 5;
    private static final String DEFAULT_DESCRIPTION = "Fake step description for step ";

    @Rule
    public ActivityTestRule<RecipeDetailsActivity> mRecipeDetailsActivityRule =
            new ActivityTestRule<>(RecipeDetailsActivity.class, false, false);

    @Before
    public void configureTestRecipe() {
        ArrayList<RecipeStepModel> steps = new ArrayList<>();
        for (int i = 0; i < DEFAULT_NUM_STEPS; i++) {
            steps.add(new RecipeStepModel(i, "",
                    DEFAULT_DESCRIPTION + i,
                    "Fake short step description for step " + i));
        }
        mRecipeModelTest = new RecipeModel(0, "Fake Recipe", "", 3);
        mRecipeModelTest.setSteps(steps);
    }

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("udabake.brunodea.com.udabake", appContext.getPackageName());
    }

    @Test
    public void recipeSteps_DisplayAllSteps() {
        Intent intent = new Intent();
        intent.putExtra(RecipeDetailsActivity.RECIPE_MODEL_EXTRA, mRecipeModelTest);
        mRecipeDetailsActivityRule.launchActivity(intent);

        onView(withId(R.id.rv_recipe_steps))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        for (int i = 0; i < DEFAULT_NUM_STEPS; i++) {
            onView(withId(R.id.tv_step_description))
                    .check(matches(withText(DEFAULT_DESCRIPTION + i)));
            onView(withId(R.id.bt_next_step))
                    .perform(click());
        }
    }
}
