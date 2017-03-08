package com.example.julien.geoapp.services.doorsService;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

import com.example.julien.geoapp.Externalization.Message;
import com.example.julien.geoapp.R;
import com.example.julien.geoapp.models.Entity;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Julien on 2017-01-30.
 */

public class DrawGeoJsonDoorsService implements IDrawGeoJsonDoorsService {


    private MapboxMap mapboxMap;
    private Context context;
    private String request;
    private ArrayList<Entity> doorsInformationForPins;
    private ArrayList<MarkerViewOptions> markers;

    public DrawGeoJsonDoorsService(MapboxMap mapboxMap, Context context, String geojson) {
        this.mapboxMap = mapboxMap;
        this.context = context;
        this.request = geojson;
        doorsInformationForPins = new ArrayList<>();
        markers = new ArrayList<>();
        saveDoors();
    }

    private void saveDoors() {
        try {
            JSONObject json = new JSONObject(request);
            JSONArray features = json.getJSONArray(Message.FEATURES_JSON[0]);
            for (int fn = 0; fn <= features.length(); fn++) {
                JSONObject feature = features.getJSONObject(fn);
                JSONObject geometry = feature.getJSONObject(Message.FEATURES_JSON[1]);
                if (geometry != null) {
                    String type = geometry.getString(Message.FEATURES_JSON[2]);
                    if (!TextUtils.isEmpty(type) && type.equalsIgnoreCase(Message.FEATURES_JSON[3])) {
                        JSONArray coords = geometry.getJSONArray(Message.FEATURES_JSON[4]);
                        try {
                            JSONObject description = feature.getJSONObject(Message.FEATURES_JSON[5]);
                            Entity door = new Entity(description.getString(Message.FEATURES_JSON[6]), description.getString(Message.FEATURES_JSON[7]), description.getString(Message.FEATURES_JSON[2]), coords.getDouble(1), coords.getDouble(0));
                            doorsInformationForPins.add(door);
                        } catch (Exception exception) {
                            Log.e(Message.ERROR[0], Message.ERROR[1]);
                        }
                    }
                }
            }
        } catch (Exception exception) {
            Log.e(Message.ERROR[0], exception.toString());
        }
        createMarkers();
    }

    private void createMarkers() {
        if (doorsInformationForPins.size() > 0) {
            for (int i = 0; i < doorsInformationForPins.size(); i++) {
                if (doorsInformationForPins.get(i).getType().equals(Message.ENTITY_TYPE[2])) {
                    Bitmap bitmap = BitmapFactory.decodeFile(Message.PATH_FILE + doorsInformationForPins.get(i).getTitle() + Message.EXETENSION);
                    if (bitmap == null) {
                        createIcon(doorsInformationForPins.get(i).getTitle());
                        bitmap = BitmapFactory.decodeFile(Message.PATH_FILE + doorsInformationForPins.get(i).getTitle() + Message.EXETENSION);
                    }
                    Drawable iconLocal = new BitmapDrawable(context.getResources(), bitmap);
                    IconFactory iconFactory = IconFactory.getInstance(context);
                    Icon icon = iconFactory.fromDrawable(iconLocal);
                    addMarkersCustomIconNoDescriptionAndTitle(i, icon);
                } else if (doorsInformationForPins.get(i).getType().equals(Message.ENTITY_TYPE[0])) {
//                    IconFactory iconFactory = IconFactory.getInstance(context);
//                    Drawable iconDrawable = ContextCompat.getDrawable(context, R.drawable.pin);
//                    Icon icon = iconFactory.fromDrawable(iconDrawable);
//                    addMarkersCustomIcon(i, icon);
                } else if (doorsInformationForPins.get(i).getType().equals(Message.ENTITY_TYPE[1])) {
                    IconFactory iconFactory = IconFactory.getInstance(context);
                    Drawable iconDrawable = ContextCompat.getDrawable(context, R.drawable.stairs2);
                    Icon icon = iconFactory.fromDrawable(iconDrawable);
                    addMarkersCustomIcon(i, icon);
                }
            }
        }
    }

    private void addMarkersCustomIconNoDescriptionAndTitle(int i, Icon icon) {
        MarkerViewOptions mark = new MarkerViewOptions()
                .position(new LatLng(doorsInformationForPins.get(i).getLati(), doorsInformationForPins.get(i).getlongi()))
                //.title(doorsInformationForPins.get(i).getTitle())
                .icon(icon);
        markers.add(mark);
    }

    private void addMarkersCustomIcon(int i, Icon icon) {
        MarkerViewOptions mark = new MarkerViewOptions()
                .position(new LatLng(doorsInformationForPins.get(i).getLati(), doorsInformationForPins.get(i).getlongi()))
                .title(doorsInformationForPins.get(i).getTitle())
                //.snippet(doorsInformationForPins.get(i).getDescription())
                .icon(icon);
        markers.add(mark);
    }

    public String getLocalName(Marker localToFind){
        for(Entity door : doorsInformationForPins){
            if(door.getLati() == localToFind.getPosition().getLatitude() && door.getlongi() == localToFind.getPosition().getLongitude()){
                return door.getTitle();
            }
        }
        return "";
    }

    public void drawDoors() {
        if (mapboxMap.getMarkers().isEmpty()) {
            for (int i = 0; i < markers.size(); i++) {
                mapboxMap.addMarker(markers.get(i));
            }
        }
    }

    public void hideDoors() {
        if (!mapboxMap.getMarkers().isEmpty()) {
            for (int i = 0; i < mapboxMap.getMarkers().size(); i++)
                mapboxMap.removeMarker(mapboxMap.getMarkers().get(i));
        }
    }

    public void addFromToMarkers(String pathGeoJson) {
        try {
            JSONObject json = new JSONObject(pathGeoJson);
            JSONArray path = json.getJSONArray(Message.FEATURES_JSON[9]);
            JSONArray to = path.getJSONArray(path.length() - 1);
            LatLng latLngTo = new LatLng(to.getDouble(1), to.getDouble(0));
            addTo(latLngTo);
        } catch (Exception exception) {
            Log.e(Message.ERROR[0], exception.toString());
        }
    }

    private void addTo(LatLng latLong) {
        for (int i = 0; i < markers.size(); i++) {
            if (markers.get(i).getTitle() != null && markers.get(i).getTitle().equals(Message.TO_MARKER)) {
                markers.remove(i);
            }
        }
        IconFactory iconFactory = IconFactory.getInstance(context);
        Drawable iconDrawable = ContextCompat.getDrawable(context, R.drawable.flag2);
        Icon icon = iconFactory.fromDrawable(iconDrawable);
        MarkerViewOptions mark = new MarkerViewOptions()
                .position(new LatLng(latLong.getLatitude(), latLong.getLongitude()))
                .title(Message.TO_MARKER)
                .icon(icon);
        markers.add(mark);
    }

    private void createIcon(String local) {
        Bitmap src = BitmapFactory.decodeResource(context.getResources(), R.drawable.llocal);
        Bitmap dest = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);
        String yourText = local;
        Canvas cs = new Canvas(dest);
        Paint tPaint = new Paint();
        tPaint.setTextSize(25);
        Rect r = new Rect();
        cs.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        tPaint.setTextAlign(Paint.Align.LEFT);
        tPaint.getTextBounds(yourText, 0, yourText.length(), r);
        float x = cWidth / 2f - r.width() / 2f - r.left;
        float y = cHeight / 2f + r.height() / 2f - r.bottom + 33;
        cs.drawText(yourText, x, y, tPaint);
        try {
            dest.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(new File(context.getFilesDir(), local + Message.TO_MARKER)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}