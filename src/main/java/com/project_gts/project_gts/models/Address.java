package com.project_gts.project_gts.models;

public class Address {
    private int id;
    private String postalCode;
    private String city;
    private String district;
    private String street;
    private int building;
    private int apartment;

    public Address(int id, String postalCode, String city, String district, String street, int building, int apartment) {
        this.id = id;
        this.postalCode = postalCode;
        this.city = city;
        this.district = district;
        this.street = street;
        this.building = building;
        this.apartment = apartment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getBuilding() {
        return building;
    }

    public void setBuilding(int building) {
        this.building = building;
    }

    public int getApartment() {
        return apartment;
    }

    public void setApartment(int apartment) {
        this.apartment = apartment;
    }
}

