package com.example.next2me.data;

import android.graphics.Bitmap;

public class MatchRequest {

    private String Uid;
    private String name;
    private String age;
    private Bitmap profilePic;
    private Boolean isAccepted;


    public MatchRequest(){}

    public MatchRequest(String uid, String name, String age, Bitmap profilePic) {
        Uid = uid;
        this.name = name;
        this.age = age;
        this.profilePic = profilePic;
    }

    public Bitmap getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(Bitmap profilePic) {
        this.profilePic = profilePic;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public Boolean getAccepted() {
        return isAccepted;
    }

    public void setAccepted(Boolean accepted) {
        isAccepted = accepted;
    }
}
