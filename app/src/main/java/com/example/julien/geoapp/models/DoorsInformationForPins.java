package com.example.julien.geoapp.models;

/**
 * Created by Julien on 2017-02-09.
 */

public class DoorsInformationForPins extends Point {
    private String type;
    public DoorsInformationForPins(String Title, String Description,String type, double lati, double longi) {
        super(Title, Description, lati, longi);
        this.type = type;
    }
    public void setType(String type){
        this.type = type;
    }
    public String getType(){
       return type;
    }
}
