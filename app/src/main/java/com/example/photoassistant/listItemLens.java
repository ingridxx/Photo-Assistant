package com.example.photoassistant;

public class listItemLens {

    private String minZoom;
    private String maxZoom;
    private String partName;


    public listItemLens(String minZoom, String maxZoom, String partName) {
        this.minZoom = minZoom;
        this.maxZoom = maxZoom;
        this.partName = partName;
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
