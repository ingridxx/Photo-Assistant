package com.example.photoassistant;

import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;

public class Intelligence {

    static class Current
    {
        private static Integer[] isoRange = {50,64,80,100,125,160,200,250,320,400,500,640,800,1000,1250,1600,2000,2500,3200,4000,5000,6400,8000,10000,12800};
        private static Double[] shutterSpeedRange ={1.0/8000,1.0/6400,1.0/5000,1.0/4000,1.0/3200,1.0/2500,1.0/2000,1.0/1600,1.0/1250,1.0/1000,1.0/800,1.0/640,1.0/500,1.0/400,1.0/320,1.0/250,1.0/200,1.0/160,1.0/125,1.0/100,1.0/80,1.0/60,1.0/50,1.0/40,1.0/30,1.0/25,1.0/20,1.0/15,1.0/13,1.0/10,1.0/8,1.0/6,1.0/5,1.0/4,1.0/3,1.0/2.5,1.0/2,1.0/1.6,1.0/1.3,1.0,1.3,1.6,2.0,2.5,3.0,4.0,5.0,6.0,8.0,10.0,13.0,15.0,20.0,25.0,30.0};
        private static Double[] apertureRange ={40.0,36.0,32.0,29.0,25.0,22.0,20.0,18.0,16.0,14.0,13.0,11.0,10.0,9.0,8.0,7.1,6.3,5.6,5.0,4.5,4.0,3.5,3.2,2.8,2.5,2.2,2.0,1.8,1.6,1.4,1.2,1.1,1.0,0.95,0.85,0.75};
        private static Double[] focalLengthRange = {9.0,10.0,12.0,14.0,15.0,18.0,20.0,24.0,28.0,30.0,35.0,40.0,50.0,55.0,60.0,70.0,75.0,100.0,105.0,125.0,135.0,150.0,200.0,225.0,275.0,300.0,400.0,450.0,500.0,550.0};
        private static ListItemBody body = MainActivity.body_al.get(1);
        private static ListItemLens lens = MainActivity.lens_al.get(1);
        private static int focalLengthStep = 0;
        private static int apertureStep = 0;
        private static int shutterSpeedStep = 0;
        private static int isoStep = 0;
        private static double focalLength = 50;
        private static double aperture = 2.8;
        private static double shutterSpeed = 1.0/100;
        private static double distance = 0.28;
        private static int ISO = 400;
        private static double previewISO = 400;
        private static double previewSS = 1.0/125;

        private static List<Double> currentApertureRange = Arrays.asList(apertureRange);
        private static List<Double> currentFocalLengthRange = Arrays.asList(shutterSpeedRange);
        private static List<Integer> currentIsoRange = Arrays.asList(isoRange);
        private static List<Double> currentshutterSpeedRange = Arrays.asList(shutterSpeedRange);
        public static ListItemBody getBody(){return body;}
        public static ListItemLens getLens(){return lens;}

        public static void setBody(ListItemBody b){body = b;}
        public static void setLens(ListItemLens l){lens = l;
        currentApertureRange = new ArrayList<Double>();

        currentApertureRange.add(lens.getApertureMaxWide());
        for (int i = 0;i<apertureRange.length;i++)
        {
            if(apertureRange[i]>lens.getApertureMaxWide() && apertureRange[i]<lens.getApertureMinTele())
            {
                currentApertureRange.add(apertureRange[i]);
            }
        }
        currentApertureRange.add(lens.getApertureMinTele());

        currentFocalLengthRange = new ArrayList<Double>();
        currentFocalLengthRange.add(lens.getMinZoom());
        for (int i = 0;i<focalLengthRange.length;i++)
        {
            if(focalLengthRange[i]>lens.getMinZoom() && focalLengthRange[i]<lens.getMaxZoom())
            {
                currentFocalLengthRange.add(focalLengthRange[i]);
            }
        }
        currentFocalLengthRange.add(lens.getMaxZoom());
        }

        public static double getFocalLength(){return focalLength;}
        public static double getAperture(){return aperture;}
        public static double getShutterSpeed(){return shutterSpeed;}
        public static int getISO(){return isoRange[isoStep];}
        public static double getDistnace(){return distance;}

        public static void focalLengthPlus(){focalLength = currentFocalLengthRange.get(++focalLengthStep);}
        public static void aperturePlus(){aperture = currentApertureRange.get(++apertureStep);}
        public static void shutterSpeedPlus(){shutterSpeed = currentshutterSpeedRange.get(++shutterSpeedStep);}
        public static void isoPlus(){ISO = currentIsoRange.get(++isoStep);}

        public static void focalLengthMinus(){focalLength = currentFocalLengthRange.get(--focalLengthStep);}
        public static void apertureMinus(){aperture = currentApertureRange.get(--apertureStep);}
        public static void shutterSpeedMinus(){shutterSpeed = currentshutterSpeedRange.get(--shutterSpeedStep);}
        public static void isoMinus(){ISO = currentIsoRange.get(--isoStep);}

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
        double focalLength = Current.focalLength;
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
