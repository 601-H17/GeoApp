package com.example.julien.geoapp.activity.activityTest.MainActivityTest;


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
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;


/**
 * Created by Julien on 2017-02-13.
 */


@RunWith(AndroidJUnit4.class)
public class MainActivityUiTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);



    @Test
    public void seeAllFloorButtons1() {
        onView(withId(R.id.button))
                .check(matches(isDisplayed()));
    }

    @Test
    public void seeAllFloorButtons2() {
        onView(withId(R.id.button2))
                .check(matches(isDisplayed()));
    }

    @Test
    public void seeAllFloorButtons3() {
        onView(withId(R.id.button3))
                .check(matches(isDisplayed()));
    }

    @Test
    public void seeSearchMenu() {
        onView(withId(R.id.searchMenu))
                .check(matches(isDisplayed()));
    }

    @Test
    public void seeDoor() {
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

        Matcher<View> relativeLayout = childAtPosition(withId(R.id.mapview),2);
        Matcher<View> linearLayout = childAtPosition(relativeLayout,0);
        childAtPosition(linearLayout,0).matches(isDisplayed());
        childAtPosition(linearLayout,0).matches(withText("G-153"));
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