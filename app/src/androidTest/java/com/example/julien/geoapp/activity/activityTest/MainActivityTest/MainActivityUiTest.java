package com.example.julien.geoapp.activity.activityTest.MainActivityTest;


import android.support.test.espresso.Espresso;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.julien.geoapp.Externalisation.Message;
import com.example.julien.geoapp.R;
import com.example.julien.geoapp.activity.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.example.julien.geoapp.activity.activityTest.MainActivityTest.MainPageObject.ClickFloor;
import static com.example.julien.geoapp.activity.activityTest.MainActivityTest.MainPageObject.SearchForLocal;
import static com.example.julien.geoapp.activity.activityTest.MainActivityTest.MainPageObject.ZoomInTheMap;
import static com.example.julien.geoapp.activity.activityTest.MainActivityTest.MainPageObject.getFirstMarkerFound;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;


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
        ClickFloor(1);
        onView(withId(R.id.currentFloor)).check(matches(withText(Message.FIRST_FLOOR_TEXT)));
    }

    @Test
    public void seeAllFloorButtons2() {
        ClickFloor(2);
        onView(withId(R.id.currentFloor)).check(matches(withText(Message.SECOND_FLOOR_TEXT)));
    }

    @Test
    public void seeAllFloorButtons3() {
        ClickFloor(3);
        onView(withId(R.id.currentFloor)).check(matches(withText(Message.THIRD_FLOOR_TEXT)));
    }

    @Test
    public void blockingOnLeftSideWhenSwipingLeftALot() throws InterruptedException {
        for(int i = 0; i < 2; i++){
            onView(withId(R.id.markerViewContainer))
                    .perform(ViewActions.swipeLeft());
        }
    }

    @Test
    public void blockingOnRightSideWhenSwipingRightALot() throws InterruptedException {
        for(int i = 0; i < 2; i++){
            onView(withId(R.id.markerViewContainer))
                    .perform(ViewActions.swipeRight());
        }
    }

    @Test
    public void seeDoorWhenZoom(){
        // ACT
        ClickFloor(1);
        ZoomInTheMap();
        //PO
        ViewInteraction imageView2 = null;
        while(imageView2 == null){
            try{
                imageView2 = getFirstMarkerFound();
            }
            catch (NoMatchingViewException e){
                System.out.print(e.toString());
            }
        }
        //ASSERT
        ViewInteraction check = imageView2.check(matches(isDisplayed()));
        //imageView2.
        
    }


    @Test
    public void seeSearchMenu(){
        //ASSERT
        onView(withId(R.id.searchMenu))
                .check(matches(isDisplayed()));
    }

    @Test
    public void seeDoorWithText() throws InterruptedException {
        //ACT
        seeDoorWhenZoom();
        ViewInteraction imageView2 = getFirstMarkerFound();
        imageView2.perform(ViewActions.click());
        //ASSERT
        onView(withId(R.id.infowindow_title)).check(matches(withText(startsWith("G-1"))));
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

    @Test
    public void seeTheSpinnerWhenWritingOnTheFirstSearchView(){
        SearchForLocal("G-1");
        //onView(withId(R.id.searchMenu)).check(matches(withSpinnerText("G-159")));
    }

    @Test
    public void seeBlaBlaBla(){

    }

}