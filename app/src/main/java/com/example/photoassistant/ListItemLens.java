package com.example.photoassistant;

public class ListItemLens {

    private String partName;
    private float minZoom;
    private float maxZoom;
    private float apertureMinWide;
    private float apertureMinTele;
    private float apertureMaxWide;
    private float apertureMaxTele;
    private float minFocusDistance;

    public ListItemLens(String partName, String minZoom, String maxZoom, String apertureMaxWide, String apertureMaxTele, String apertureMinWide, String apertureMinTele, String minFocusDistance) {
        this.minZoom = Float.valueOf(minZoom);
        this.maxZoom = Float.valueOf(maxZoom);
        this.apertureMaxWide = Float.valueOf(apertureMaxWide);
        this.apertureMaxTele = Float.valueOf(apertureMaxTele);
        this.apertureMinWide = Float.valueOf(apertureMinWide);
        this.apertureMinTele = Float.valueOf(apertureMinTele);
        this.minFocusDistance = Float.valueOf(minFocusDistance);
        this.partName = partName;
    }


    public String getPartName() {
        return partName;
    }

    public float getMinZoom() {
        return minZoom;
    }

    public float getMaxZoom() {
        return maxZoom;
    }

    public float getApertureMinWide() {
        return apertureMinWide;
    }

    public float getApertureMinTele() {
        return apertureMinTele;
    }

    public float getApertureMaxWide() {
        return apertureMaxWide;
    }

    public float getApertureMaxTele() {
        return apertureMaxTele;
    }

    public float getMinFocusDistance() {
        return minFocusDistance;
    }
}