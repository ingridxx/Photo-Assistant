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

    public double getMinZoom() {
        return minZoom;
    }

    public double getMaxZoom() {
        return maxZoom;
    }

    public double getApertureMinWide() {
        return apertureMinWide;
    }

    public double getApertureMinTele() {
        return apertureMinTele;
    }

    public double getApertureMaxWide() {
        return apertureMaxWide;
    }

    public double getApertureMaxTele() {
        return apertureMaxTele;
    }

    public double getMinFocusDistance() {
        return minFocusDistance;
    }
}