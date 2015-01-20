package com.ant.vkgeoclasstest;

import java.net.URL;
/**
 * Created by Mike Antipiev on 25.11.14.
 *
 * User class to store all users and set bindings to countries and cities
 */

public class User extends Location{

    private City city;
    private Country country;
    private String photo;

//    Static variable to count all users
    private static int count=0;

    public User (int id, String name) {
        super(id, name);
    }

    public void setCountry(Country country) {

//        Check if it's a first assignment, then add user to country users counter
        if (this.country == null) { country.addUser(); }
        this.country = country;
    }

    public Country getCountry() {
        return country;
    }

    public void setCity(City city) {
//        Check if it's a first assignment, then add user to city users counter
        if (this.city == null) { city.addUser(); }
        this.city = city;
    }

    public City getCity() {
        return city;
    }

    public static int getCount() {
        return count;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
