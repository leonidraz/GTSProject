package com.project_gts.project_gts.models;

import java.util.Date;

public class Request {
    private int id;
    private int subscriberId;
    private Date date;
    private String type;
    private String description;
    private String prioritet;
    private String status;

    public Request(int id, int subscriberId, Date date, String type, String description, String prioritet, String status) {
        this.id = id;
        this.subscriberId = subscriberId;
        this.date = date;
        this.type = type;
        this.description = description;
        this.prioritet = prioritet;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(int subscriberId) {
        this.subscriberId = subscriberId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrioritet() {
        return prioritet;
    }

    public void setPrioritet(String prioritet) {
        this.prioritet = prioritet;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

