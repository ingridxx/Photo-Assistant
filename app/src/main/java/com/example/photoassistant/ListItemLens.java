package com.example.photoassistant;

public class ListItemLens extends ListItem {

    public static int resourceID = R.raw.lens;
    private float minZoom;
    private float maxZoom;
    private float apertureMinWide;
    private float apertureMinTele;
    private float apertureMaxWide;
    private float apertureMaxTele;
    private float minFocusDistance;

    public ListItemLens(String partName, String minZoom, String maxZoom, String apertureMaxWide, String apertureMaxTele, String apertureMinWide, String apertureMinTele, String minFocusDistance) {
        super(partName);
        this.minZoom = Float.valueOf(minZoom);
        this.maxZoom = Float.valueOf(maxZoom);
        this.apertureMaxWide = Float.valueOf(apertureMaxWide);
        this.apertureMaxTele = Float.valueOf(apertureMaxTele);
        this.apertureMinWide = Float.valueOf(apertureMinWide);
        this.apertureMinTele = Float.valueOf(apertureMinTele);
        this.minFocusDistance = Float.valueOf(minFocusDistance);

    }

    public ListItemLens(String[] array) {
        super(array[0]);
    }
//, String minZoom, String maxZoom, String apertureMaxWide, String apertureMaxTele, String apertureMinWide, String apertureMinTele
    public ListItemLens(String partName,String[] array) {
        super(array[0]);
        this.minZoom = Float.valueOf(array[1]);
        this.maxZoom = Float.valueOf(array[2]);
        this.apertureMaxWide = Float.valueOf(array[3]);
        this.apertureMaxTele = Float.valueOf(array[4]);
        this.apertureMinWide = Float.valueOf(array[5]);
        this.apertureMinTele = Float.valueOf(array[6]);
        if (array.length == 8){
            this.minFocusDistance = Float.valueOf(array[7]);
        } else {
            this.minFocusDistance = (maxZoom /1000);
        }

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

    public String getSimpleName() {
        String finalString = "";
        if(apertureMaxWide==apertureMaxTele)
        {
            finalString = finalString+apertureMaxWide;
        }
        else
        {
            finalString = apertureMaxWide+"-"+apertureMaxTele;
        }
        finalString = finalString+"/";
        double minZoomDisplay, maxZoomDisplay;
        if(Math.ceil(minZoom) == Math.floor(minZoom))
        {

        }
        String minZoomStr, maxZoomStr;
        if(Math.ceil(minZoom) == Math.floor(minZoom))
        {
            minZoomStr = String.valueOf((int)minZoom);
        }
        else
        {
            minZoomStr =  String.format( "%.1f", minZoom );

        }if(Math.ceil(maxZoom) == Math.floor(maxZoom))
        {
            maxZoomStr = String.valueOf((int)maxZoom);
        }
        else
        {
            maxZoomStr =  String.format( "%.1f", maxZoom );
        }
        if(minZoomStr.equals(maxZoomStr))
        {
            finalString = finalString+minZoomStr;
        }
        else
        {
            finalString = minZoomStr+"-"+maxZoomStr;
        }
        return finalString;
    }

    @Override
    protected String TopLineText() {
        String retVal="";

        retVal += getPartName();

        return retVal;
    }

    @Override
    protected String MiddleLineText() {
        return super.MiddleLineText();
    }

    @Override
    protected String BottomLineText() {
        return super.BottomLineText();
    }

    @Override
    public String toString() {

        String retString ="";

        retString += getPartName();
        retString += getMinZoom();
        retString += getMaxZoom();
        retString += getApertureMaxWide();
        retString += getApertureMaxTele();
        retString += getApertureMinWide();
        retString += getApertureMinTele();
        retString += getMinFocusDistance();

        return retString;
    }

}