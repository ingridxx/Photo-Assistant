package com.example.photoassistant;

public class listItemLens {
    private String minZoom;
    private String maxZoom;
    private String aperture;
    private String partName;
    private String shutterSpeed;


    public listItemLens(String _minZoom, String _maxZoom, String _aperture, String _partName, String _shutterSpeed){

        minZoom = _minZoom;
        maxZoom = _maxZoom;
        aperture = _aperture;
        partName =  _partName;
        shutterSpeed = _shutterSpeed;

    }

    public String getMinZoom() {
        return minZoom;
    }

    public String getMaxZoom() {
        return maxZoom;
    }

    public String getAperture() {
        return aperture;
    }

    public String getPartName() {
        return partName;
    }

    public String getShutterSpeed() {
        return shutterSpeed;
    }

    public void setMinZoom(String minZoom) {
        this.minZoom = minZoom;
    }

    public void setMaxZoom(String maxZoom) {
        this.maxZoom = maxZoom;
    }

    public void setAperture(String aperture) {
        this.aperture = aperture;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public void setShutterSpeed(String shutterSpeed) {
        this.shutterSpeed = shutterSpeed;
    }
}
