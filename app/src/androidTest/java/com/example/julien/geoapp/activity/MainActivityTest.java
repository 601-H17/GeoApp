package com.example.julien.geoapp.activity;


import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;

import com.example.julien.geoapp.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void seeAllFloorButtons1() {
        ViewInteraction button = onView(withId(R.id.button)).check(ViewAssertions.matches(isDisplayed()));
    }
    @Test
    public void seeAllFloorButtons2() {
        ViewInteraction button2 = onView(withId(R.id.button2)).check(ViewAssertions.matches(isDisplayed()));
    }
    @Test
    public void seeAllFloorButtons3() {
        ViewInteraction button3 = onView(withId(R.id.button3)).check(ViewAssertions.matches(isDisplayed()));
    }
    @Test
    public void seeMapsOnCreate() {
        ViewInteraction maps = onView(withId(R.id.mapview)).check(ViewAssertions.matches(isDisplayed()));
    }
    @Test
    public void ChooseFloorAndMapsChange() {
        ViewInteraction button1 = onView(withId(R.id.button)).perform(click());
    }
}
