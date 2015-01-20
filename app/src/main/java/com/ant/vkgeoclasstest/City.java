package com.ant.vkgeoclasstest;

import java.net.URL;
import com.google.android.gms.maps.model.LatLng;


/**
 * Created by Mike Antipiev on 21.11.14.
 *
 * City class to store cities information and bindngs to countries
 */

public class City extends Location {

    private Country country;
    private LatLng coords;
    private URL photo;

//    Storing users counter for this city
    private int countUsers=0;

//    Static variable to store cities count
    private static int count=0;

    public City () {

    }

    public City (int id, String name) {
        super(id, name);
        count++;
    }

    public City (int id, String name, LatLng coords) {
        this(id, name);
        this.coords = coords;
    }

    public City (int id, String name, LatLng coords, URL photo) {
        this(id, name);
        this.coords = coords;
        this.photo = photo;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Country getCountry() {
        return country;
    }

    public void setCoords(LatLng coords) {
        this.coords = coords;
    }

    public LatLng getCoords() {
        return coords;
    }

    public void addUser() {
        countUsers++;
    }

    public int getCountUsers() {
        return countUsers;
    }

    public int getCount() {return count; }

}
