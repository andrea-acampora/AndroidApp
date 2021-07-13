package com.example.next2me.viewmodel;

public class CardItem {

    private String id;
    private String name;
    private String birthday;

    public CardItem(String name, String id, String birthday) {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
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
