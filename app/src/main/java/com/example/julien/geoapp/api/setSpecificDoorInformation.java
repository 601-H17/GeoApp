package com.example.julien.geoapp.api;

import com.example.julien.geoapp.activity.MainActivity;

/**
 * Created by eric3 on 2017-03-03.
 */

public class setSpecificDoorInformation extends ApiRequest {
    public setSpecificDoorInformation(MainActivity context, String route) {
        super(context, route);
    }

    @Override
    protected void onPostExecute(String request) {
        super.onPostExecute(request);
        super.context.setSpecificDoorInformationCallback(request);
    }
}
