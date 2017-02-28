package com.example.julien.geoapp.api;

import android.os.AsyncTask;
import android.util.Log;

import com.example.julien.geoapp.Externalization.Message;
import com.example.julien.geoapp.R;
import com.example.julien.geoapp.activity.MainActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Julien on 2017-02-10.
 */

abstract class ApiRequest extends AsyncTask<Void, Void, String> {


    protected MainActivity context;
    protected String routeMap;

    public ApiRequest(MainActivity context, String route) {
        this.context = context;
        this.routeMap = route;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(Void... voids) {

        String request = Message.REQUEST_NULL;
        try {
            URL url = new URL(context.getString(R.string.apiUrl) + routeMap);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty(context.getString(R.string.headerAuthorization), context.getString(R.string.headerToken));
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append(Message.ENTER);
                }
                bufferedReader.close();
                request = stringBuilder.toString();
            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception exception) {
            Log.e(Message.ERROR[0], exception.toString());
        }
        return request;
    }

    protected void onPostExecute(String request) {
        super.onPostExecute(request);
    }
}