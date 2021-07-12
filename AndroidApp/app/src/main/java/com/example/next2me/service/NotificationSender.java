package com.example.next2me.service;

public class NotificationSender {

    public Notification notification;
    public String to;

    public NotificationSender(Notification notification, String to) {
        this.notification = notification;
        this.to = to;
    }

    public NotificationSender() {
    }
}