package com.example.photoassistant;

public class listItemBody {
    private String minZoom;
    private String maxZoom;
    private String aperture;
    private String partName;
    private String shutterSpeed;


    public listItemBody(String _minZoom, String _maxZoom, String _aperture, String _partName, String _shutterSpeed){

        minZoom = _minZoom;
        maxZoom = _maxZoom;
        partName =  _partName;

    }

    public String getMinZoom() {
        return minZoom;
    }

    public String getMaxZoom() {
        return maxZoom;
    }

    public String getPartName() {
        return partName;
    }

    public void setMinZoom(String minZoom) {
        this.minZoom = minZoom;
    }

    public void setMaxZoom(String maxZoom) {
        this.maxZoom = maxZoom;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

}
