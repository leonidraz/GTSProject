package com.project_gts.project_gts.models;

public class Subscriber {
    private int id;
    private String lastName;
    private String firstName;
    private String middleName;
    private String gender;
    private int age;
    private String type;
    private boolean isIntercity;

    public Subscriber(int id, String lastName, String firstName, String middleName, String gender, int age, String type,  boolean isIntercity) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.gender = gender;
        this.age = age;
        this.type = type;
        this.isIntercity = isIntercity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean getIntercity() {
        return isIntercity;
    }

    public void setIntercity(boolean isIntercity) {
        this.isIntercity = isIntercity;
    }
}
