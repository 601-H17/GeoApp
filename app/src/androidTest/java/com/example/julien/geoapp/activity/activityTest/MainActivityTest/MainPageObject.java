package com.example.julien.geoapp.activity.activityTest.MainActivityTest;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.example.julien.geoapp.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by eric3 on 2017-02-28.
 */

public class MainPageObject {

    public static void ClickFloor(int floor){
        int ressourceId = 0;
        switch (floor) {
            case 1:  ressourceId = R.id.button;
                break;
            case 2:  ressourceId = R.id.button2;
                break;
            case 3:  ressourceId = R.id.button3;
                break;
        }
        onView(withId(ressourceId))
                .check(matches(isDisplayed()));
        onView(withId(ressourceId))
                .perform(click());
    }

    public static ViewInteraction getFirstMarkerFound() throws InterruptedException {
        ViewInteraction imageView2;
        Thread.sleep(5000);
        imageView2 = onView(
                Matchers.allOf(withId(R.id.image),
                        childAtPosition(
                                Matchers.allOf(withId(R.id.markerViewContainer),
                                        childAtPosition(
                                                withId(R.id.mapview),
                                                1)),
                                0),
                        isDisplayed()));
        return imageView2;
    }

    public static void SearchForLocal(String local){
        onView(withId(R.id.searchMenu)).perform(typeText(local));
    }

    public static void ZoomInTheMap(){
        for(int i = 0; i < 3; i++){
            onView(withId(R.id.mapview)).perform(ViewActions.doubleClick());
        }
    }

    public static void ClearTextOnFirstSearchView() throws InterruptedException {
//        onView(withId(R.id.search_close_btn)).check(matches(isDisplayed()));
//        onView(withId(R.id.search_close_btn)).perform(click())
        onView(withId(R.id.searchMenu)).perform(closeSoftKeyboard());
        //onView(withId(R.id.searchMenu)).perform(replaceText(""));
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
