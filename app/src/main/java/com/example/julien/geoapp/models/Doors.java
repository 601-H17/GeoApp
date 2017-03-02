package com.example.julien.geoapp.models;

/**
 * Created by Julien on 2017-02-10.
 */

public class Doors extends Point {
    private int etage;
    private String teacher;

    public Doors(String Title, String Description, String teacher, int etage, double lati, double longi) {
        super(Title, Description, lati, longi);
        this.etage = etage;
        this.teacher = teacher;
    }

    public int getEtage() {
        return etage;
    }

    public String getTeacher() {
        return teacher;
    }
}