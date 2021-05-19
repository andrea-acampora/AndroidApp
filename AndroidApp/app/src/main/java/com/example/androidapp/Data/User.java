package com.example.androidapp.Data;
import java.util.Date;

public class User {


    private String name;
    private String surname;
    private String birthdate;
    private String description;
    private String gender;
    private String preferences;


    public User() {
    }

    public User(String name, String surname, String birthdate, String description, String gender, String preferences) {
        this.name = name;
        this.surname = surname;
        this.birthdate = birthdate;
        this.description = description;
        this.gender = gender;
        this.preferences = preferences;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public String getDescription() {
        return description;
    }

    public String getGender() {
        return gender;
    }

    public String getPreferences() {
        return preferences;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }
}
