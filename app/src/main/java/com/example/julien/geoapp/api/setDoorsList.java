package com.example.julien.geoapp.api;

import com.example.julien.geoapp.activity.MainActivity;

/**
 * Created by Julien on 2017-02-10.
 */

public class setDoorsList extends ApiRequest {
    public setDoorsList(MainActivity context, String route) {
        super(context, route);
    }
    @Override
    protected void onPostExecute(String request) {
        super.onPostExecute(request);
        super.context.setDoorList(request);
    }
}
