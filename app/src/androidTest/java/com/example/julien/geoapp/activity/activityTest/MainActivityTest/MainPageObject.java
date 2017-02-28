package com.example.julien.geoapp.activity.activityTest.MainActivityTest;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.example.julien.geoapp.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Created by eric3 on 2017-02-28.
 */

public class MainPageObject {

    private static void clickOverflowItem(int resId) {

    }

    // overflow menu
    public static void GoToFloorOne() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.button))
                .check(matches(isDisplayed()));
        onView(withId(R.id.button))
                .perform(click());
    }

    // overflow menu
    public static void GoToFloorTwo() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.button2))
                .check(matches(isDisplayed()));
        onView(withId(R.id.button2))
                .perform(click());
    }

    // overflow menu
    public static void GoToFloorThree() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.button3))
                .check(matches(isDisplayed()));
        onView(withId(R.id.button3))
                .perform(click());
    }

    public static void ClickOnFirstMarkerFound(){
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
    }

    // settings overflow item
    public static void navigateToSettings() {
        //clickOverflowItem(R.string.action_settings);
    }

    // post overflow item
    public static void navigateToPost() {
        //clickOverflowItem(R.string.action_post);
    }

    public static Matcher<View> childAtPosition(final Matcher<View> parentMatcher, final int position) {

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
