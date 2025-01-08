package com.project_gts.project_gts.models;

import java.sql.Date;
import java.time.LocalTime;

public class Call {
    private int id;
    private int phoneId;
    private String dialedPhone;
    private Date date;
    private LocalTime time;
    private LocalTime duration;

    public Call(int id, int phoneId, String dialedPhone, Date date, LocalTime time, LocalTime duration) {
        this.id = id;
        this.phoneId = phoneId;
        this.dialedPhone = dialedPhone;
        this.date = date;
        this.time = time;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(int phoneId) {
        this.phoneId = phoneId;
    }

    public String getDialedPhone() {
        return dialedPhone;
    }

    public void setDialedPhone(String dialedPhone) {
        this.dialedPhone = dialedPhone;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public LocalTime getDuration() {
        return duration;
    }

    public void setDuration(LocalTime duration) {
        this.duration = duration;
    }
}
