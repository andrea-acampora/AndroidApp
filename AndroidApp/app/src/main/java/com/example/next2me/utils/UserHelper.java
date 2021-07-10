package com.example.next2me.utils;

import android.graphics.Bitmap;

import com.example.next2me.data.User;

public class UserHelper {

    private User appUser;
    private Bitmap profilePic;
    private String notificationsTokenId;

    private static UserHelper instance = null;

    private UserHelper(){
        appUser = new User();
    }

    public static UserHelper getInstance(){
        if(instance == null){
            instance = new UserHelper();
        }
        return instance;
    }

    public User getAppUser(){
        return appUser;
    }

    public String getUserName() {
        return appUser.getName();
    }

    public String getUserSurname() {
        return appUser.getSurname();
    }

    public String getBirthdate() {
        return appUser.getBirthdate();
    }

    public String getDescription() {
        return appUser.getDescription();
    }

    public String getGender() {
        return appUser.getGender();
    }

    public String getPreferences() {
        return appUser.getPreferences();
    }

    public void setName(String name) {
        appUser.setName(name);
    }

    public void setSurname(String surname) {
        appUser.setSurname(surname);
    }

    public void setBirthdate(String birthdate) {
       appUser.setBirthdate(birthdate);
    }

    public void setDescription(String description) {
        appUser.setDescription(description);
    }

    public void setGender(String gender) {
        appUser.setGender(gender);
    }

    public void setPreferences(String preferences) {
        appUser.setPreferences(preferences);
    }

    public String getEmail() {
        return appUser.getEmail();
    }

    public void setEmail(String email) {
        appUser.setEmail(email);
    }

    public void setProfilePic(Bitmap bitmap){
        profilePic = bitmap;
    }

    public Bitmap getProfilePic(){
        return profilePic;
    }

    public String getNotificationsTokenId() {
        return notificationsTokenId;
    }

    public void setNotificationsTokenId(String notificationsTokenId) {
        this.notificationsTokenId = notificationsTokenId;
    }
}
