package com.example.photoassistant;

public class listItemBody {

    private String aperture;
    private String partName;
    private String shutterSpeed;
    private String iso;

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public listItemBody(String aperture, String partName, String shutterSpeed,String iso) {
        this.aperture = aperture;
        this.partName = partName;
        this.shutterSpeed = shutterSpeed;
        this.iso = iso;
    }

    public String getPartName() {
        return partName;
    }

    public String getAperture() {
        return aperture;
    }

    public void setAperture(String aperture) {
        this.aperture = aperture;
    }

    public String getShutterSpeed() {
        return shutterSpeed;
    }

    public void setShutterSpeed(String shutterSpeed) {
        this.shutterSpeed = shutterSpeed;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

}
