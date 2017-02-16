package com.example.julien.geoapp.api;

import com.example.julien.geoapp.activity.MainActivity;

/**
 * Created by Julien on 2017-02-14.
 */

public class setPathGeoJson extends ApiRequest {
    public setPathGeoJson(MainActivity context, String route) {
        super(context, route);
    }

    @Override
    protected void onPostExecute(String request) {
        super.onPostExecute(request);
       // super.context.setPathGeoJson(request);
    }
}
