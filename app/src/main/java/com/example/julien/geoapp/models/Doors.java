package com.example.julien.geoapp.models;

/**
 * Created by Julien on 2017-02-10.
 */

public class Doors extends Point {
    private int etage;
    private String teacher;
    private String tag;

    public Doors(String Title, String Description, String teacher, int etage, double lati, double longi, String tagForDoor) {
        super(Title, Description, lati, longi);
        this.etage = etage;
        this.teacher = teacher;
        this.tag = tagForDoor;
    }

    public String getTag() {
        return tag;
    }
    public int getEtage() {
        return etage;
    }

    public String getTeacher() {
        return teacher;
    }


}