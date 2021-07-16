package com.example.next2me.viewmodel;

import static java.lang.Math.round;

public class CardItem {

    private double distanceKm;
    private String id;
    private String name;
    private String birthday;

    public CardItem(String name, String id, String birthday, double distanceKm ) {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
        this.distanceKm = distanceKm;
    }

    public double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
