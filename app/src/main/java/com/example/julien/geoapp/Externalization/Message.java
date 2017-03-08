package com.example.julien.geoapp.Externalization;

/**
 * Created by Julien on 2017-02-28.
 */

public class Message {

    public static String[] FEATURES_JSON = {"features", "geometry", "type", "Point", "coordinates", "properties", "ref", "entrance", "doors: ", "path", "LineString"};
    public static String REQUEST_NULL = "";
    public static String ENTER = "\n";
    public static String[] FEATURES_JSON_PATH = {"name", "description", "floor", "point", "lat", "lng"};
    public static String[] ERROR = {"TAG", "Exception Loading GeoJSON "};
    public static String[] ENTITY_TYPE = {"doors", "stairs", "image"};
    public static String PATH_FILE = "data/data/com.example.julien.geoapp/files/";
    public static String EXETENSION = ".png";
    public static String TO_MARKER = ".png";
    public static String COLOR_WALL = "#ffffff";
    public static String COLOR_PATH = "#cb2c39";
    public static String FLOOR_TEXT = "Étage : ";
}
