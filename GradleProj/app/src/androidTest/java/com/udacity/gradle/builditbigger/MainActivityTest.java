package com.udacity.gradle.builditbigger;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mMainActivityRule =
            new ActivityTestRule<>(MainActivity.class, false, true);
    @Test
    public void asyncJokeTest() {
        Espresso.onView(ViewMatchers.withId(R.id.instructions_button))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.androidjokeslib_tv_joke))
                .check(ViewAssertions.matches(not(withText(""))));
    }
}
