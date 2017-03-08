package com.example.julien.geoapp.activity.activityTest.MainActivityTest.services;

import android.support.test.espresso.NoMatchingViewException;

/**
 * Created by eric3 on 2017-03-03.
 */

class MySpecialException extends Throwable {
    public MySpecialException(NoMatchingViewException e) {
    }
}
