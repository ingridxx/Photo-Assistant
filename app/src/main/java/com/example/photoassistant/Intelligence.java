package com.example.photoassistant;

import android.widget.Toast;

import androidx.annotation.NonNull;

public class Intelligence {

    static class Current
    {
        static ListItemBody body = MainActivity.body_al.get(1);
        static ListItemLens lens = MainActivity.lens_al.get(1);
        static int focalLength = 50;
        static double aperture = 2.8;
        static double shutterSpeed = 1.0/100;
        static int ISO = 400;
        static double distance = 0.28;
        static double previewISO = 400;
        static double previewSS = 1.0/125;
        
    }
    //CoCCalculator(SensorSize)
    //HyperfocalCalculator(CoC)
    //DoFNearCalculator(Hyperfocal, subjectDistance, focalLength)
    //DoFFarCalculator(Hyperfocal, subjectDistance, focalLength)
    //EVCalculator(aperture, ss, ISO, previewSS, previewISO)

    public static double CoCCalculator()
    {
        //CoC (mm) = viewing distance (cm) / desired final-image resolution (lp/mm) for a 25 cm viewing distance / enlargement / 25
        //assuming worst case 60cm viewing distance on a 27inch 4k monitor
        double sensorSizeX = Current.body.getSensorSizeX();
        double sensorSizeY = Current.body.getSensorSizeY();
        int viewingDistance = 60;
        int monitorSize = 27;
        int monitorX = 3840;
        int monitorY = 2160;
        int monitorAspectRatioX = 16;
        int monitorAspectRatioY = 9;
        double monitorkMM =monitorSize*2.54*10/(monitorAspectRatioX*monitorAspectRatioX+monitorAspectRatioY*monitorAspectRatioY);//reverse pythagoras theorem
        double lpmm = 1.0*(monitorX/monitorAspectRatioX)/monitorkMM;

        double sensorSize = sensorSizeX*sensorSizeY;
        double viewSize = monitorAspectRatioX*monitorkMM*monitorAspectRatioY*monitorkMM;
        double enlargement = Math.sqrt(viewSize/sensorSize);

        return viewingDistance/lpmm/enlargement/25;
    }
    public static double HyperfocalCalculator(double CoC)
    {
        int focalLength = Current.focalLength;
        double fNumber = Current.aperture;
        return focalLength*focalLength/CoC/fNumber+focalLength;
    }
    public static double DofNearCalculator(double hyperfocal){
        return Current.distance*(hyperfocal-Current.focalLength)/(hyperfocal+Current.distance-2*Current.focalLength);
    }
    public static double DofFarCalculator(double hyperfocal){

        return Current.distance*(hyperfocal-Current.focalLength)/(hyperfocal+Current.distance);
    }
    public static double ExposureCalculator(){
        double exposure = Current.aperture*Current.aperture*Current.shutterSpeed*Current.ISO/(Current.previewSS*Current.previewISO);
        return exposure;

    }
}
