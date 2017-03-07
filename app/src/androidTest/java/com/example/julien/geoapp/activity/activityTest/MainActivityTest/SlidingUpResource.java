package com.example.julien.geoapp.activity.activityTest.MainActivityTest;

import android.support.test.espresso.IdlingResource;

import com.example.julien.geoapp.activity.MainActivity;

/**
 * Created by eric3 on 2017-03-06.
 */

class SlidingUpResource implements IdlingResource {

    private MainActivity activity;
    private ResourceCallback callback;

    public SlidingUpResource(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public String getName() {
        return "MainActivityIdleName";
    }

    @Override
    public boolean isIdleNow() {
        Boolean idle = isIdle();
        if (idle) callback.onTransitionToIdle();
        return idle;
    }

    public boolean isIdle() {
        return activity != null && activity.slidingPanelShowed();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        this.callback = resourceCallback;
    }
}

