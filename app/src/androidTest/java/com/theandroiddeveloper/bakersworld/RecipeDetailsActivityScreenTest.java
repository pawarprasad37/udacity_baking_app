package com.theandroiddeveloper.bakersworld;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.theandroiddeveloper.bakersworld.activity.HomeActivity;
import com.theandroiddeveloper.bakersworld.activity.RecipeDetailsActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withResourceName;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.startsWith;

@RunWith(AndroidJUnit4.class)
public class RecipeDetailsActivityScreenTest {
    private HomeActivity mActivity;
    private boolean mIsScreenSw600dp;

    @Rule
    public ActivityTestRule<HomeActivity> homeActivityActivityTestRule =
            new ActivityTestRule<>(HomeActivity.class);

    @Before
    public void registerIdlingResources() {
        IdlingRegistry.getInstance()
                .register(homeActivityActivityTestRule.getActivity().getIdlingResource());
        this.mActivity = homeActivityActivityTestRule.getActivity();
        this.mIsScreenSw600dp = isScreenSw600dp();
    }

    @Test
    public void clickThirdStep_OpensStepDetailsScreen() {
        onView(withId(R.id.recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onData(anything())
                .inAdapterView(withId(R.id.listView))
                .atPosition(2)
                .perform(click());

        //check if description starts with '2.'
        onView(withId(R.id.tvDescription))
                .perform(ViewActions.scrollTo())
                .check(matches(withText(startsWith("2."))));

        if (!mIsScreenSw600dp) {
            //testing a phone; will have nav buttons.

            onView(withId(R.id.btNext))
                    .perform(click());

            //check if description starts with '3.'
            onView(withId(R.id.tvDescription))
                    .perform(ViewActions.scrollTo())
                    .check(matches(withText(startsWith("3."))));

            onView(withId(R.id.btPrevious))
                    .perform(click());

            onView(withId(R.id.btPrevious))
                    .perform(click());

            //check if description starts with '1.'
            onView(withId(R.id.tvDescription))
                    .perform(ViewActions.scrollTo())
                    .check(matches(withText(startsWith("1."))));
        } else {
            //testing on tablet device.
            onData(anything())
                    .inAdapterView(withId(R.id.listView))
                    .atPosition(3)
                    .perform(click());

            //check if description starts with '3.'
            onView(withId(R.id.tvDescription))
                    .perform(ViewActions.scrollTo())
                    .check(matches(withText(startsWith("3."))));

            onData(anything())
                    .inAdapterView(withId(R.id.listView))
                    .atPosition(1)
                    .perform(click());

            //check if description starts with '1.'
            onView(withId(R.id.tvDescription))
                    .perform(ViewActions.scrollTo())
                    .check(matches(withText(startsWith("1."))));

        }

    }

    @After
    public void unregisterIdlingResources() {
        IdlingRegistry.getInstance()
                .unregister(homeActivityActivityTestRule.getActivity().getIdlingResource());
    }

    private boolean isScreenSw600dp() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float widthDp = displayMetrics.widthPixels / displayMetrics.density;
        float heightDp = displayMetrics.heightPixels / displayMetrics.density;
        float screenSw = Math.min(widthDp, heightDp);
        return screenSw >= 600;
    }

}
