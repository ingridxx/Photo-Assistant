package com.example.photoassistant;

public class listItem {
    private String minZoom;
    private String maxZoom;
    private String aperture;
    private String partName;
    private String shutterSpeed;


    public listItem(String _minZoom, String _maxZoom, String _aperture, String _partName,String _shutterSpeed){

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
}
