package com.example.julien.geoapp.activity.activityTest.MainActivityTest;

import com.example.julien.geoapp.R;

import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by eric3 on 2017-02-28.
 */

public class MainPageObject {

    private static void clickOverflowItem(int resId) {

    }

    // overflow menu
    public static void GoToFloorOne() {
        onView(withId(R.id.button))
                .check(matches(isDisplayed()));
        onView(withId(R.id.button))
                .perform(click());
    }

    // overflow menu
    public static void GoToFloorTwo() {
        onView(withId(R.id.button2))
                .check(matches(isDisplayed()));
        onView(withId(R.id.button2))
                .perform(click());
    }

    // overflow menu
    public static void GoToFloorThree() {
        onView(withId(R.id.button3))
                .check(matches(isDisplayed()));
        onView(withId(R.id.button3))
                .perform(click());
    }

    @Test
    public void seeSearchMenu() {
        onView(withId(R.id.searchMenu))
                .check(matches(isDisplayed()));
    }

    // settings overflow item
    public static void navigateToSettings() {
        //clickOverflowItem(R.string.action_settings);
    }

    // post overflow item
    public static void navigateToPost() {
        //clickOverflowItem(R.string.action_post);
    }
}
