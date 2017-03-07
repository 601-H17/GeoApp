package com.example.julien.geoapp.activity.activityTest.MainActivityTest;


import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

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
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.example.julien.geoapp.activity.activityTest.MainActivityTest.MainPageObject.ClickFloor;
import static com.example.julien.geoapp.activity.activityTest.MainActivityTest.MainPageObject.ClickOnClearQueryButton;
import static com.example.julien.geoapp.activity.activityTest.MainActivityTest.MainPageObject.GetFirstMarkerFound;
import static com.example.julien.geoapp.activity.activityTest.MainActivityTest.MainPageObject.OBJECT_ID_AUTO_COMPLETE_TEXT_VIEW_2;
import static com.example.julien.geoapp.activity.activityTest.MainActivityTest.MainPageObject.SearchForDestination;
import static com.example.julien.geoapp.activity.activityTest.MainActivityTest.MainPageObject.SearchForLocal;
import static com.example.julien.geoapp.activity.activityTest.MainActivityTest.MainPageObject.SelectFirstLocalInAutoCompleteMenu;
import static com.example.julien.geoapp.activity.activityTest.MainActivityTest.MainPageObject.SwipingToADirection;
import static com.example.julien.geoapp.activity.activityTest.MainActivityTest.MainPageObject.ZoomInTheMap;
import static com.example.julien.geoapp.activity.activityTest.MainActivityTest.MainPageObject.ZoomToLocalMarker;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;


/**
 * Created by Julien on 2017-02-13.
 */


@RunWith(AndroidJUnit4.class)
public class MainActivityUiTest {
    private MainActivityIdlingResource idlingResource;
    private SlidingUpResource idlingResourceSlideUp;
    private final String LOCAL = "G-159";
    private final String DESTINATION = "G-170";
    private final int NUMBER_OF_SWIPING = 2;
    final int OBJECT_ID_MARKER_VIEW_CONTAINER = R.id.markerViewContainer;
    final int OBJECT_ID_CURRENT_FLOOR = R.id.currentFloor;
    final int OBJECT_ID_SEARCH_MENU = R.id.searchMenu;
    final int OBJECT_ID_SEARCH_SRC_TEXT = R.id.search_src_text;
    final int OBJECT_ID_DRAG_VIEW = R.id.dragView;
    final int OBJECT_ID_BUTTON_OK = R.id.button4;
    final int OBJECT_ID_MARKER = R.id.image;
    final int OBJECT_ID_LOCAL_NAME =  R.id.local_name;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void registerIntentServiceIdlingResource() {
        MainActivity activity = mActivityTestRule.getActivity();
        idlingResource = new MainActivityIdlingResource(activity);
        idlingResourceSlideUp = new SlidingUpResource(activity);
        setFailureHandler(new CustomFailureHandler(getInstrumentation().getTargetContext()));

    }

    @After
    public void unregisterIntentServiceIdlingResource() {
        Espresso.unregisterIdlingResources(idlingResource);
    }

    @Test
    public void seeAllFloorButtons1() throws InterruptedException {
        //ARRANGE
        int floorNumber = 1;
        String resultLocal = "G-1";
        ViewInteraction viewMarker = null;

        // ACT
        ClickFloor(floorNumber);
        ZoomInTheMap();
        Espresso.registerIdlingResources(idlingResource);
        viewMarker = GetFirstMarkerFound();
        Espresso.unregisterIdlingResources(idlingResource);
        viewMarker.perform(click());

        Espresso.registerIdlingResources(idlingResourceSlideUp);
        onView(withId(OBJECT_ID_LOCAL_NAME)).check(matches(isDisplayed()));
        Espresso.unregisterIdlingResources(idlingResourceSlideUp);

        //ASSERT
        onView(withId(OBJECT_ID_LOCAL_NAME)).check(matches(withText(startsWith(resultLocal))));
    }

    @Test
    public void seeAllFloorButtons2() {
        //ARRANGE
        int floorNumber = 2;
        String resultLocal = "G-2";
        ViewInteraction viewMarker = null;

        // ACT
        ClickFloor(floorNumber);
        ZoomInTheMap();
        Espresso.registerIdlingResources(idlingResource);
        viewMarker = GetFirstMarkerFound();
        Espresso.unregisterIdlingResources(idlingResource);
        viewMarker.perform(click());

        Espresso.registerIdlingResources(idlingResourceSlideUp);
        onView(withId(OBJECT_ID_LOCAL_NAME)).check(matches(isDisplayed()));
        Espresso.unregisterIdlingResources(idlingResourceSlideUp);

        //ASSERT
        onView(withId(OBJECT_ID_LOCAL_NAME)).check(matches(withText(startsWith(resultLocal))));
    }

    @Test
    public void seeAllFloorButtons3() {
        //ARRANGE
        int floorNumber = 3;

        // ACT
        ClickFloor(floorNumber);
        //Nothing to test - TO DO when the map is finished
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
    public void clearTextInSearchMenu(){
        //ARRANGE
        String resultLocal = "";

        //ACT
        SearchForLocal(LOCAL);
        ClickOnClearQueryButton();

        //ASSERT
        onView(withId(OBJECT_ID_SEARCH_SRC_TEXT)).check(matches(withText(resultLocal)));
    }

    @Test
    public void insertTextOnDestinationSearchMenu(){
        //ARRANGE
        String resultLocal = LOCAL;
        String resultDestination = DESTINATION;

        //ACT
        SearchForLocal(LOCAL);
        onView(withId(MainPageObject.OBJECT_ID_AUTO_COMPLETE_TEXT_VIEW_2)).perform(click());
        SearchForDestination(DESTINATION);

        //ASSERT
        onView(withId(OBJECT_ID_SEARCH_SRC_TEXT)).check(matches(withText(resultLocal)));
        onView(withId(MainPageObject.OBJECT_ID_AUTO_COMPLETE_TEXT_VIEW_2))
                .check(matches(withText(resultDestination)));
    }

    @Test
    public void selectLocalInAutoCompleteSearchMenu(){
        //ARRANGE
        String resultLocal = LOCAL;

        //ACT
        SearchForLocal("G-");
        SelectFirstLocalInAutoCompleteMenu();

        //ASSERT
        onView(withId(OBJECT_ID_SEARCH_SRC_TEXT)).check(matches(withText(resultLocal)));
    }

    @Test
    public void seeLocalMarkerWithText() throws InterruptedException {
        //ACT
        SearchForLocal(LOCAL);
        SelectFirstLocalInAutoCompleteMenu();
        onView(withId(OBJECT_ID_BUTTON_OK)).perform(click());
        ViewInteraction imageView = GetFirstMarkerFound();
        imageView.perform(click());
        //ASSERT
        Espresso.registerIdlingResources(idlingResourceSlideUp);
        onView(withId(OBJECT_ID_DRAG_VIEW)).check(matches(isDisplayed()));
        Espresso.unregisterIdlingResources(idlingResourceSlideUp);
    }

    @Test
    public void dragViewMarkerIsNotDisplayedWhenApplicationLaunch(){
        //ASSERT
        onView(withId(OBJECT_ID_DRAG_VIEW)).check(matches(not(isDisplayed())));
    }

    @Test
    public void markerIsNotDisplayedOnTheMapWhenApplicationLaunch(){
        //ASSERT
        onView(withId(OBJECT_ID_MARKER)).check(matches(not(isDisplayed())));
    }

    @Test
    public void secondSearchViewIsDisplayedWhenTheFirstSearchviewContainsMoreThanOneCharacter() {
        //ACT
        SearchForLocal("G-153");

        //ASSERT
        onView(withId(OBJECT_ID_AUTO_COMPLETE_TEXT_VIEW_2)).check(matches(isDisplayed()));
    }


    /*
    @Test
    public void seeTheSpinnerWhenWritingOnTheFirstSearchView(){
        SearchForLocal("G-1");
        //onView(withId(R.id.searchMenu)).check(matches(withSpinnerText("G-159")));
    }*/

}