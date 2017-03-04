package com.example.julien.geoapp.activity.activityTest.MainActivityTest;


import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.julien.geoapp.Externalization.Message;
import com.example.julien.geoapp.R;
import com.example.julien.geoapp.activity.MainActivity;
import com.example.julien.geoapp.activity.activityTest.MainActivityTest.services.CustomFailureHandler;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.setFailureHandler;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.example.julien.geoapp.activity.activityTest.MainActivityTest.MainPageObject.ClickFloor;
import static com.example.julien.geoapp.activity.activityTest.MainActivityTest.MainPageObject.SearchForLocal;
import static com.example.julien.geoapp.activity.activityTest.MainActivityTest.MainPageObject.ZoomInTheMap;
import static com.example.julien.geoapp.activity.activityTest.MainActivityTest.MainPageObject.getFirstMarkerFound;
import static com.example.julien.geoapp.activity.activityTest.MainActivityTest.MainPageObject.ZoomToLocalMarker;
import static com.example.julien.geoapp.activity.activityTest.MainActivityTest.MainPageObject.ClickOnClearQueryButton;
import static com.example.julien.geoapp.activity.activityTest.MainActivityTest.MainPageObject.SwipingToADirection;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;


/**
 * Created by Julien on 2017-02-13.
 */


@RunWith(AndroidJUnit4.class)
public class MainActivityUiTest {
    private MainActivityIdlingResource idlingResource;
    private final String LOCAL = "G-159";
    private final int NUMBER_OF_SWIPING = 2;
    final int OBJECT_ID_MARKER_VIEW_CONTAINER = R.id.markerViewContainer;
    final int OBJECT_ID_CURRENT_FLOOR = R.id.currentFloor;
    final int OBJECT_ID_SEARCH_MENU = R.id.searchMenu;
    final int OBJECT_ID_SEARCH_SRC_TEXT = R.id.search_src_text;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void registerIntentServiceIdlingResource() {
        MainActivity activity = mActivityTestRule.getActivity();
        idlingResource = new MainActivityIdlingResource(activity);
        setFailureHandler(new CustomFailureHandler(getInstrumentation().getTargetContext()));
        Espresso.registerIdlingResources(idlingResource);
    }

    @After
    public void unregisterIntentServiceIdlingResource() {
        Espresso.unregisterIdlingResources(idlingResource);
    }

    @Test
    public void seeAllFloorButtons1() {
        //ARRANGE
        int floorNumber = 1;

        // ACT
        ClickFloor(floorNumber);
        onView(withId(OBJECT_ID_CURRENT_FLOOR)).check(matches(withText(Message.FIRST_FLOOR_TEXT)));
    }

    @Test
    public void seeAllFloorButtons2() {
        //ARRANGE
        int floorNumber = 2;

        // ACT
        ClickFloor(floorNumber);
        onView(withId(OBJECT_ID_CURRENT_FLOOR)).check(matches(withText(Message.SECOND_FLOOR_TEXT)));
    }

    @Test
    public void seeAllFloorButtons3() {
        //ARRANGE
        int floorNumber = 3;

        // ACT
        ClickFloor(floorNumber);
        onView(withId(OBJECT_ID_CURRENT_FLOOR)).check(matches(withText(Message.THIRD_FLOOR_TEXT)));
    }

    @Test
    public void blockingOnLeftSideWhenSwipingLeftALot() throws InterruptedException {
        //ARRANGE
        String swipingDirection = "Left";

        // ACT
        SwipingToADirection(NUMBER_OF_SWIPING, swipingDirection);
    }

    @Test
    public void blockingOnRightSideWhenSwipingRightALot() throws InterruptedException {
        //ARRANGE
        String swipingDirection = "Right";

        // ACT
        SwipingToADirection(NUMBER_OF_SWIPING, swipingDirection);
    }

    @Test
    public void blockingOnTopSideWhenSwipingUpALot() throws InterruptedException {
        //ARRANGE
        String swipingDirection = "Up";

        // ACT
        SwipingToADirection(NUMBER_OF_SWIPING, swipingDirection);
    }

    @Test
    public void blockingOnBottomSideWhenSwipingDownALot() throws InterruptedException {
        //ARRANGE
        String swipingDirection = "Down";

        // ACT
        SwipingToADirection(NUMBER_OF_SWIPING, swipingDirection);
    }

    @Test
    public void seeLocalMarkerWhenZoom(){
        // ACT
        ViewInteraction imageView = ZoomToLocalMarker();

        //ASSERT
        imageView.check(matches(isDisplayed()));
    }

    @Test
    public void seeSearchMenu(){
        //ASSERT
        onView(withId(OBJECT_ID_SEARCH_MENU)).check(matches(isDisplayed()));
    }
    
    @Test
    public void insertTextInSearchMenu(){
        //ARRANGE
        String resultLocal = LOCAL;

        //ACT
        SearchForLocal(LOCAL);

        //ASSERT
        onView(withId(OBJECT_ID_SEARCH_SRC_TEXT)).check(matches(withText(resultLocal)));
    }

    @Test
    public void ClearTextInSearchMenu(){
        //ARRANGE
        String resultLocal = "";

        //ACT
        SearchForLocal(LOCAL);
        ClickOnClearQueryButton();

        //ASSERT
        onView(withId(OBJECT_ID_SEARCH_SRC_TEXT)).check(matches(withText(resultLocal)));
    }

    @Test
    public void seeLocalMarkerWithText() throws InterruptedException {
        //ACT
        ViewInteraction imageView = ZoomToLocalMarker();
        imageView.perform(ViewActions.click());

        //ASSERT
        //onView(withId(R.id.infowindow_title)).check(matches(withText(startsWith("G-1"))));
    }

    @Test
    public void seeTheSecondSearchViewWhenTheFirstSearchviewContainsMoreThanFourCharacter() throws InterruptedException {
        //ACT
        SearchForLocal("G");
        onView(withId(R.id.autoCompleteTextView2)).check(matches(not(isDisplayed())));
        //ClearTextOnFirstSearchView();
        SearchForLocal("-153");
        onView(withId(R.id.autoCompleteTextView2)).check(matches(isDisplayed()));
    }

    /*
    @Test
    public void seeTheSpinnerWhenWritingOnTheFirstSearchView(){
        SearchForLocal("G-1");
        //onView(withId(R.id.searchMenu)).check(matches(withSpinnerText("G-159")));
    }*/

}