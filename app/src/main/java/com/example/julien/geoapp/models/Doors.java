package com.example.julien.geoapp.models;

/**
 * Created by Julien on 2017-02-10.
 */

public class Doors extends Point {
    private int etage;

    public Doors(String Title, String Description, int etage, double lati, double longi) {
        super(Title, Description, lati, longi);
        this.etage = etage;
    }

    public int getEtage() {
        return etage;
    }
}
