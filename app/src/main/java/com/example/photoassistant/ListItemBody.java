package com.example.photoassistant;

import androidx.annotation.NonNull;

public class ListItemBody extends ListItem{

    public static int resourceID = R.raw.mount;
    public static final int fullFrameSensorArea = 36*24;
    private String type;
    private String hint;
    private float ffd;
    private float sensorSizeX;
    private float sensorSizeY;


//    public ListItemBody(String partName, String ffd, String sensorSizeX, String sensorSizeY, String type, String hint) {
//        super(partName);
//        this.partName = partName;
//        this.ffd = Float.valueOf(ffd);
//        this.sensorSizeX = Float.valueOf(sensorSizeX);
//        this.sensorSizeY = Float.valueOf(sensorSizeY);
//        this.type = type;
//        this.hint = hint;
//    }

    public ListItemBody(String partName,String[] array) {
        super(array[0]);
        this.ffd = Float.valueOf(array[1]);
        this.sensorSizeX = Float.valueOf(array[2]);
        this.sensorSizeY = Float.valueOf(array[3]);
        this.type = array[4];
        this.hint = array[5];
    }


    protected void HandleClickEvent(){}

    @Override
    protected String TopLineText() {
        return getPartName();
    }

    @Override
    protected String MiddleLineText() {
        return getHint();
    }

    @Override
    protected String BottomLineText() {
        return String.format("%.1f", getCropFactor());
    }


    public String getPartName() {
        return partName;
    }


    public String getFfd() {
        return partName;
    }
    public float getSensorSizeX() {
        return sensorSizeX;
    }
    public float getSensorSizeY() {
        return sensorSizeY;
    }
    public float getCropFactor() { return (float)Math.sqrt(fullFrameSensorArea/this.getSensorArea()); }
    public float getSensorArea() { return sensorSizeX*sensorSizeY; }
    public float getSensorAspectRatio() { return (float)(1.0*sensorSizeX/sensorSizeY); }
    public String getType() { return type; }
    public String getHint() { return hint; }

    @NonNull
    @Override
    public String toString(){
//String partName, String ffd, String sensorSizeX, String sensorSizeY, String type, String hint
        String retString="";

        retString+= "partname " + getPartName();
        retString+= "FFD " + getFfd();
        retString+= "SENSOR X " + getSensorSizeX();
        retString+= "SENSOR Y " + getSensorSizeY();
        retString+= "TYPE " + getType();
        retString+= "HINT " + getHint();


        return retString;
    }


}
