package com.example.julien.geoapp.activityTest.MainActivityTest;

import android.test.mock.MockContentProvider;

import com.example.julien.geoapp.activity.MainActivity;
import com.google.common.util.concurrent.ExecutionError;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Julien on 2017-02-08.
 */

public class MainActivityUnitTest {

    private MainActivity mainActivity;
    private String map;
    @Before
    public void setUp() throws Exception{
        mainActivity = new MainActivity();
        map = "nice map";
    }

    @Test
    public void mainActivityiIinstanced() throws Exception {
        Assert.assertTrue(mainActivity != null);
    }
}
