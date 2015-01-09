package com.ant.vkgeoclasstest;

import java.net.URL;
/**
 * Created by apple on 25.11.14.
 */
public class User extends Location{
    private int id;
    private String name;
    private City city;
    private Country country;
    private String photo;

    private static int count=0;

    public User (int id, String name) {
        super(id, name);
    }

    public void setCountry(Country country) {

        if (this.country == null) { country.addUser(); }
        this.country = country;
    }

    public Country getCountry() {
        return country;
    }

    public void setCity(City city) {
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
