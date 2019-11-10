package com.example.photoassistant;

import android.media.Image;

import androidx.annotation.NonNull;

/**
 * ListItemBody is an instance of a camera body from the raw csv file 'mount.csv'.
 * It is heavily used in the calculations and the Recycler Adpater to symbolise
 * a users camera. It is a child class of the abstract class list item, and
 * inherits methods such as its constructor, and partName + getters and setters,
 * and TopLineText,BottomLineText, and MiddleLineText. This class contains all the
 * specs which relate to how a photo will turn out. it also contains getters for
 * each variable.
 */

public class ListItemBody extends ListItem {

    public static final int fullFrameSensorArea = 36 * 24;
    public static int resourceID = R.raw.mount;
    private String type;
    private String hint;
    private float ffd;
    private float sensorSizeX;
    private float sensorSizeY;

    /***
     * @param array which contains all the values in string which relate to
     *              camera functions. in order the array contains
     *              [0] = partname -- which is passed to super
     *              [1] = ffd --
     *              [2] = sensorSizeX --
     *              [3] = sensorSizeY --
     *              [4] = type -- which relates to what type of body this is
     *              e.g. range finder, slr, dslr etc
     *              [5] = hint --
     */
    public ListItemBody(String partName, String[] array) {
        super(array[0]);
        this.ffd = Float.valueOf(array[1]);
        this.sensorSizeX = Float.valueOf(array[2]);
        this.sensorSizeY = Float.valueOf(array[3]);
        this.type = array[4];
        this.hint = array[5];
    }

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

    public float getCropFactor() {
        return (float) Math.sqrt(fullFrameSensorArea / this.getSensorArea());
    }

    public float getSensorArea() {
        return sensorSizeX * sensorSizeY;
    }

    public float getSensorAspectRatio() {
        return (float) (1.0 * sensorSizeX / sensorSizeY);
    }

    public String getType() {
        return type;
    }

    public String getHint() {
        return hint;
    }


    /**
     * used to print the values of this object to an easy and understandable line
     *
     * @return string which contains all the values of each variable
     */
    @NonNull
    @Override
    public String toString() {
        String retString = "";

        retString += "partname " + getPartName();
        retString += "FFD " + getFfd();
        retString += "SENSOR X " + getSensorSizeX();
        retString += "SENSOR Y " + getSensorSizeY();
        retString += "TYPE " + getType();
        retString += "HINT " + getHint();


        return retString;
    }


}
