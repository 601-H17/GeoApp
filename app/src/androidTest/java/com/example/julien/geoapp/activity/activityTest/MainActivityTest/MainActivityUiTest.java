package com.example.julien.geoapp.activity.activityTest.MainActivityTest;


import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.julien.geoapp.R;
import com.example.julien.geoapp.activity.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


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
                .check(ViewAssertions.matches(isDisplayed()));
    }

    @Test
    public void seeAllFloorButtons2() {
        onView(withId(R.id.button2))
                .check(ViewAssertions.matches(isDisplayed()));
    }

    @Test
    public void seeAllFloorButtons3() {
        onView(withId(R.id.button3))
                .check(ViewAssertions.matches(isDisplayed()));
    }

    @Test
    public void seeSearchMenu() {
        onView(withId(R.id.searchMenu))
                .check(ViewAssertions.matches(isDisplayed()));
    }



}