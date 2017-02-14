package com.example.julien.geoapp;

import android.app.Activity;
import com.robotium.recorder.executor.Executor;

@SuppressWarnings("rawtypes")
public class MainActivityExecutor extends Executor {

	@SuppressWarnings("unchecked")
	public MainActivityExecutor() throws Exception {
		super((Class<? extends Activity>) Class.forName("com.example.julien.geoapp.activity.MainActivity"),  "com.example.julien.geoapp.R.id.", new android.R.id(), false, false, "1487036442797");
	}

	public void setUp() throws Exception { 
		super.setUp();
	}
}
