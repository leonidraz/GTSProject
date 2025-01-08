package com.project_gts.project_gts.models;

public class Ats {
    private int id;
    private String name;
    private String type;
    private String district;
    private boolean internalNetwork;
    private int maxLoad;
    private int gtsId;

    public Ats(int id, String name, String type, String district, boolean internalNetwork, int maxLoad, int gtsId) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.district = district;
        this.internalNetwork = internalNetwork;
        this.maxLoad = maxLoad;
        this.gtsId = gtsId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public Boolean getInternalNetwork() {
        return internalNetwork;
    }

    public void setInternalNetwork(boolean internalNetwork) {
        this.internalNetwork = internalNetwork;
    }

    public int getMaxLoad() {
        return maxLoad;
    }

    public void setMaxLoad(int maxLoad) {
        this.maxLoad = maxLoad;
    }

    public int getGtsId() {
        return gtsId;
    }

    public void setGtsId(int gtsId) {
        this.gtsId = gtsId;
    }
}
