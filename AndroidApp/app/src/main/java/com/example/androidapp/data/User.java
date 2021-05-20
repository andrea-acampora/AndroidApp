package com.example.androidapp.data;

import java.util.UUID;

public class User {


    private String name;
    private String email;
    private String surname;
    private String birthdate;
    private String description;
    private String gender;
    private String preferences;
    private String id;


    public User() {
        this.id = UUID.randomUUID().toString();
    }

    public User(String name, String email, String surname, String birthdate, String description, String gender, String preferences) {
        this.name = name;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }
}
