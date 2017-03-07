package com.example.julien.geoapp.api;

import com.example.julien.geoapp.activity.MainActivity;

/**
 * Created by Julien on 2017-03-07.
 */

public class setSpecificDoor extends ApiRequest {
    public setSpecificDoor(MainActivity context, String route) {
        super(context, route);
    }
    @Override
    protected void onPostExecute(String request) {
        super.onPostExecute(request);
        super.context.setSpecificDoorsListCallback(request);
    }
}
