package com.theandroiddeveloper.bakersworld;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.anything;

import com.theandroiddeveloper.bakersworld.activity.HomeActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class HomeActivityScreenTest {

    @Rule
    public ActivityTestRule<HomeActivity> homeActivityActivityTestRule =
            new ActivityTestRule<>(HomeActivity.class);

    @Before
    public void registerIdlingResources() {
        IdlingRegistry.getInstance()
                .register(homeActivityActivityTestRule.getActivity().getIdlingResource());
    }

    @Test
    public void clickFirstRecipe_OpensStepsList() {
        onView(withId(R.id.recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        //check if listView on steps list fragment is displayed.
        onView(withId(R.id.listView)).check(matches(isDisplayed()));
    }

    @After
    public void unregisterIdlingResources() {
        IdlingRegistry.getInstance()
                .unregister(homeActivityActivityTestRule.getActivity().getIdlingResource());
    }

}
