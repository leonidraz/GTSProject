package com.project_gts.project_gts.models;

public class Phone {
    private int id;
    private String number;
    private String type;
    private int addressId;
    private int subscriberId;
    private int atsId;

    public Phone(int id, String number, String type, int addressId, int subscriberId, int atsId) {
        this.id = id;
        this.number = number;
        this.type = type;
        this.addressId = addressId;
        this.subscriberId = subscriberId;
        this.atsId = atsId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public int getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(int subscriberId) {
        this.subscriberId = subscriberId;
    }

    public int getAtsId() {
        return atsId;
    }

    public void setAtsId(int atsId) {
        this.atsId = atsId;
    }
}

