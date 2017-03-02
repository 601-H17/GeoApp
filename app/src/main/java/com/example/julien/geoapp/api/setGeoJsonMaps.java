package com.example.julien.geoapp.api;

import android.os.AsyncTask;
import android.util.Log;

import com.example.julien.geoapp.R;
import com.example.julien.geoapp.activity.MainActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Julien on 2017-02-06.
 */

public class setGeoJsonMaps extends ApiRequest {
    public setGeoJsonMaps(MainActivity context, String route) {
        super(context, route);
    }

    @Override
    protected void onPostExecute(String request) {
        super.onPostExecute(request);
        super.context.setMapGeoJsonCallback(request);
    }
}