package com.ant.vkgeoclasstest;

/**
 * Created by apple on 25.11.14.
 */
public class Country extends Location {
    private int countUsers=0;

    public Country () {

    }

    public Country (int id, String name) {
        super(id, name);
    }

    public void addUser() {
        countUsers++;
    }

    public int getCountUsers() {
        return countUsers;
    }

}
