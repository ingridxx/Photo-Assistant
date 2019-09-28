package com.example.photoassistant;

public class ListItemBody {

    public static final int fullFrameSensorArea = 36*24;
    private String partName;
    private String type;
    private String hint;
    private float ffd;
    private float sensorSizeX;
    private float sensorSizeY;

    public ListItemBody(String partName, String ffd, String sensorSizeX, String sensorSizeY, String type, String hint) {
        this.partName = partName;
        this.ffd = Float.valueOf(ffd);
        this.sensorSizeX = Float.valueOf(sensorSizeX);
        this.sensorSizeY = Float.valueOf(sensorSizeY);
        this.type = type;
        this.hint = hint;
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
    public float getSensorAspectRatio() { return (float)(1.0*sensorSizeY/sensorSizeX); }
    public String getType() { return type; }
    public String getHint() { return hint; }

}
