package com.ant.vkgeoclasstest;

import android.app.Application;

import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by apple on 03.12.14.
 */
public class MyApplication extends Application {

    // Store some global arrays and vars in Application class to be able access them from all activities and fragments
    // Not sure if it's a best posibile solution, but i think it is an easiest one )

    private ArrayList<User> Users;
    private SortedSet<City> Cities;
    private SortedSet<Country> Countries;
    private User Profile;
    private boolean loaded = false;

    public MyApplication() {
        clearData();
    }

    public void clearData() {
        this.Cities = new TreeSet<City>();
        this.Countries = new TreeSet<Country>();
        this.Users = new ArrayList<User>();
        //this.Profile = new User();
    }

    // Getters/setters

    public void setCities (SortedSet<City> cities) {
        this.Cities = cities;
    }

    public SortedSet<City> getCities() {
        return this.Cities;
    }

    public void setCountries (SortedSet<Country> countries) {
        this.Countries = countries;
    }

    public SortedSet<Country> getCountries() {
        return this.Countries;
    }

    public void setUsers (ArrayList<User> users) {
        this.Users = users;
    }

    public ArrayList<User> getUsers () {
        return this.Users;
    }

    public void setProfile (User profile) {
        this.Profile = profile;
    }

    public User getProfile () {
        return Profile;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public boolean isLoaded() {
        return loaded;
    }
}
