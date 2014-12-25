package com.ant.vkgeoclasstest;

import java.util.Collection;

/**
 * Created by apple on 20.11.14.
 * Basic Location class
 */
public class Location implements Comparable<Location>{

    private int id;
    private String name;


    public Location() {
    }

    public Location(int id, String name) {
        this.id=id;
        this.name=name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return id+", "+name;
    }

    @Override
    public int compareTo(Location l) {
        return id - l.id;
    }

}
