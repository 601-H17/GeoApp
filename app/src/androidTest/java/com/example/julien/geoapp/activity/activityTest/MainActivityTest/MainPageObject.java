package com.example.julien.geoapp.activity.activityTest.MainActivityTest;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.view.KeyEvent;
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
import static android.support.test.espresso.action.ViewActions.pressKey;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

public class MainPageObject {
    static final int OBJECT_ID_MARKER_VIEW_CONTAINER = R.id.markerViewContainer;
    static final int OBJECT_ID_AUTO_COMPLETE_TEXT_VIEW_2 = R.id.autoCompleteTextView2;
    static final int OBJECT_ID_SEARCH_MENU = R.id.searchMenu;
    static final int OBJECT_ID_ACTIVITY_MAIN = R.id.activity_main;
    static final int OBJECT_ID_IMAGE = R.id.image;
    static final int OBJECT_ID_MARKER_VIEW_CONTAINTER = R.id.markerViewContainer;
    static final int OBJECT_ID_MAP_VIEW = R.id.mapview;

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

    public static ViewInteraction GetFirstMarkerFound(){
        ViewInteraction imageView2 = onView(
                Matchers.allOf(withId(OBJECT_ID_IMAGE),
                        childAtPosition(
                                Matchers.allOf(withId(OBJECT_ID_MARKER_VIEW_CONTAINTER),
                                        childAtPosition(
                                                withId(OBJECT_ID_MAP_VIEW ),
                                                1)),
                                1),
                        isDisplayed()));
        return imageView2;
    }

    public static void SwipingToADirection(int numberOfSwiping, String swipingDirection){
        final String RIGHT_DIRECTION = "Right";
        final String LEFT_DIRECTION = "Left";
        final String UP_DIRECTION = "Up";
        final String DOWN_DIRECTION = "Down";

        for(int i = 0; i < numberOfSwiping; i++){
            switch (swipingDirection) {
                case RIGHT_DIRECTION:
                    onView(withId(OBJECT_ID_MARKER_VIEW_CONTAINER))
                            .perform(ViewActions.swipeRight());
                    break;
                case LEFT_DIRECTION:
                    onView(withId(OBJECT_ID_MARKER_VIEW_CONTAINER))
                            .perform(ViewActions.swipeLeft());
                    break;
                case UP_DIRECTION:
                    onView(withId(OBJECT_ID_MARKER_VIEW_CONTAINER))
                            .perform(ViewActions.swipeUp());
                    break;
                case DOWN_DIRECTION:
                    onView(withId(OBJECT_ID_MARKER_VIEW_CONTAINER))
                            .perform(ViewActions.swipeDown());
                    break;
            }
        }
    }

    public static void SearchForLocal(String local){
        onView(withId(OBJECT_ID_SEARCH_MENU)).perform(typeText(local), closeSoftKeyboard());
    }

    public static void SearchForDestination(String destination){
        onView(withId(OBJECT_ID_AUTO_COMPLETE_TEXT_VIEW_2))
                .perform(typeText(destination), closeSoftKeyboard());
    }

    public static void SelectFirstLocalInAutoCompleteMenu(){
        onView(withId(OBJECT_ID_SEARCH_MENU)).perform(pressKey(KeyEvent.KEYCODE_DPAD_DOWN), pressKey(KeyEvent.KEYCODE_ENTER));
    }

    public static void ZoomInTheMap(int zoom){
        for(int i = 0; i < zoom; i++){
            onView(withId(OBJECT_ID_ACTIVITY_MAIN)).perform(ViewActions.doubleClick());
        }
    }

    public static void ClickOnClearQueryButton(){
        String contentDescription = "Clear query";

        ViewInteraction appCompatImageView = onView(
                allOf(withClassName(is("android.support.v7.widget.AppCompatImageView")), withContentDescription(contentDescription),
                        withParent(allOf(withClassName(is("android.widget.LinearLayout")),
                                withParent(withClassName(is("android.widget.LinearLayout"))))),
                        isDisplayed()));
        appCompatImageView.perform(click());
    }

    public static ViewInteraction ZoomToLocalMarker() {
        int floorToClick = 1;
        int numberOfZoom = 3;
        ViewInteraction imageView = null;

        ClickFloor(floorToClick);
        ZoomInTheMap(numberOfZoom);
        while(imageView == null){
            imageView = GetFirstMarkerFound();
        }

        return imageView;
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