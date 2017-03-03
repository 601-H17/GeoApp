package com.example.julien.geoapp.activity.activityTest.MainActivityTest.services;

import android.content.Context;
import android.support.test.espresso.FailureHandler;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.base.DefaultFailureHandler;
import android.view.View;

import org.hamcrest.Matcher;

/**
 * Created by eric3 on 2017-03-03.
 */

public class CustomFailureHandler implements FailureHandler {
    private final FailureHandler delegate;

    public CustomFailureHandler(Context targetContext) {
        delegate = new DefaultFailureHandler(targetContext);
    }

    @Override
    public void handle(Throwable error, Matcher<View> viewMatcher) {
        try {
            delegate.handle(error, viewMatcher);
        } catch (NoMatchingViewException e) {
            try {
                throw new MySpecialException(e);
            } catch (MySpecialException e1) {
                e1.printStackTrace();
            }
        }
    }
}
