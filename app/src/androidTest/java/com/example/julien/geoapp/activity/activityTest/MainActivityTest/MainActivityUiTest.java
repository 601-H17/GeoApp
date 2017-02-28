package com.example.julien.geoapp.activity.activityTest.MainActivityTest;


import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.example.julien.geoapp.R;
import com.example.julien.geoapp.activity.MainActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.example.julien.geoapp.activity.activityTest.MainActivityTest.MainPageObject.*;
import static org.hamcrest.Matchers.allOf;


/**
 * Created by Julien on 2017-02-13.
 */


@RunWith(AndroidJUnit4.class)
public class MainActivityUiTest {
    private MainActivityIdlingResource idlingResource;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void registerIntentServiceIdlingResource() {
        MainActivity activity = mActivityTestRule.getActivity();
        idlingResource = new MainActivityIdlingResource(activity);
        Espresso.registerIdlingResources(idlingResource);
    }

    @After
    public void unregisterIntentServiceIdlingResource() {
        Espresso.unregisterIdlingResources(idlingResource);
    }

    @Test
    public void seeAllFloorButtons1() {
        GoToFloorOne();
    }

    @Test
    public void seeAllFloorButtons2() {
        GoToFloorTwo();
    }

    @Test
    public void seeAllFloorButtons3() {
        GoToFloorTwo();
    }

    @Test
    public void blockingOnLeftSideWhenSwipingLeftALot() throws InterruptedException {
        for(int i = 0; i < 2; i++){
            onView(withId(R.id.markerViewContainer))
                    .perform(ViewActions.swipeLeft());
        }
        Thread.sleep(2000);
    }

    @Test
    public void blockingOnRightSideWhenSwipingRightALot() throws InterruptedException {
        for(int i = 0; i < 2; i++){
            onView(withId(R.id.markerViewContainer))
                    .perform(ViewActions.swipeRight());
        }
        Thread.sleep(2000);
    }




    @Test
    public void seeSearchMenu() {
        onView(withId(R.id.searchMenu))
                .check(matches(isDisplayed()));
    }

    @Test
    public void seeDoorWithText() throws InterruptedException {
        onView(withId(R.id.button))
                .check(matches(isDisplayed()));
        onView(withId(R.id.button))
                .perform(click());
        onView(withId(R.id.markerViewContainer))
                .perform(ViewActions.doubleClick());
        onView(withId(R.id.markerViewContainer))
                .perform(ViewActions.doubleClick());
        onView(withId(R.id.markerViewContainer))
                .perform(ViewActions.doubleClick());
        Thread.sleep(3000);
        ViewInteraction imageView2 = onView(
                allOf(withId(R.id.image),
                        childAtPosition(
                                allOf(withId(R.id.markerViewContainer),
                                        childAtPosition(
                                                withId(R.id.mapview),
                                                1)),
                                0),
                        isDisplayed()));
        imageView2.check(matches(isDisplayed()));
        imageView2.perform(ViewActions.click());

        Thread.sleep(1000);
        onView(withId(R.id.infowindow_title)).check(matches(withText("G-116")));
    }



    private static Matcher<View> childAtPosition(final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

}