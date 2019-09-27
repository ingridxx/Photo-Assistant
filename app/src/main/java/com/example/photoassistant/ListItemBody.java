package com.example.photoassistant;

public class ListItemBody {

    public static final int fullFrameSensorArea = 36*24;
    private String partName;
    private float ffd;
    private float sensorSizeX;
    private float sensorSizeY;

    public ListItemBody(String partName, String ffd, String sensorSizeX, String sensorSizeY) {
        this.partName = partName;
        this.ffd = Float.valueOf(ffd);
        this.sensorSizeX = Float.valueOf(sensorSizeX);
        this.sensorSizeY = Float.valueOf(sensorSizeY);
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
        return fullFrameSensorArea/this.getSensorArea();
    }
    public float getSensorArea() {
        return sensorSizeX*sensorSizeY;
    }
    public float getSensorAspectRatio() {
        return (float)(1.0*sensorSizeY/sensorSizeX);
    }

}
