package com.example.photoassistant;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


public class Intelligence{

    static class Current
    {
        static DecimalFormat format = new DecimalFormat();
        private static final Integer[] isoRange = {50,64,80,100,125,160,200,250,320,400,500,640,800,1000,1250,1600,2000,2500,3200,4000,5000,6400,8000,10000,12800};
        private static final Double[] shutterSpeedRange ={1.0/8000,1.0/6400,1.0/5000,1.0/4000,1.0/3200,1.0/2500,1.0/2000,1.0/1600,1.0/1250,1.0/1000,1.0/800,1.0/640,1.0/500,1.0/400,1.0/320,1.0/250,1.0/200,1.0/160,1.0/125,1.0/100,1.0/80,1.0/60,1.0/50,1.0/40,1.0/30,1.0/25,1.0/20,1.0/15,1.0/13,1.0/10,1.0/8,1.0/6,1.0/5,1.0/4,1.0/3,1.0/2.5,1.0/2,1.0/1.6,1.0/1.3,1.0,1.3,1.6,2.0,2.5,3.0,4.0,5.0,6.0,8.0,10.0,13.0,15.0,20.0,25.0,30.0};
        private static final Double[] apertureRange ={40.0,36.0,32.0,29.0,25.0,22.0,20.0,18.0,16.0,14.0,13.0,11.0,10.0,9.0,8.0,7.1,6.3,5.6,5.0,4.5,4.0,3.5,3.2,2.8,2.5,2.2,2.0,1.8,1.6,1.4,1.2,1.1,1.0,0.95,0.85,0.75};
        private static final Double[] focalLengthRange = {9.0,10.0,12.0,14.0,15.0,18.0,20.0,24.0,28.0,30.0,35.0,40.0,50.0,55.0,60.0,70.0,75.0,100.0,105.0,125.0,135.0,150.0,200.0,225.0,275.0,300.0,400.0,450.0,500.0,550.0};
        private static List<Double> currentApertureRange = Arrays.asList(apertureRange);
        private static List<Double> currentFocalLengthRange = Arrays.asList(shutterSpeedRange);
        private static List<Integer> currentIsoRange = Arrays.asList(isoRange);
        private static List<Double> currentshutterSpeedRange = Arrays.asList(shutterSpeedRange);
        private static ListItemBody body = new ListItemBody("Body",new String[]{"Body","50","36","24","DSLR","Sample"});
        private static ListItemLens lens = new ListItemLens("Lens", new String[]{"Lens","50","50","2.8","2.8","22","22"});
        private static int focalLengthStep = 0;
        private static int apertureStep = 0;
        private static int shutterSpeedStep = shutterSpeedRange.length/2;
        private static int isoStep = isoRange.length/2;
        private static double focalLength = 50;
        private static double aperture = 2.8;
        private static double shutterSpeed = 1.0/100;
        private static double distance = 0.28;
        private static int ISO = 400;
        private static double previewISO = 400;
        private static double previewSS = 1.0/125;
        private static double dofNear = 0,dofFar = 0;

        public static ListItemBody getBody(){return body;}
        public static ListItemLens getLens(){return lens;}

        public static void setBody(ListItemBody b) {
            body = b;
            focalLengthStep = 0;
            apertureStep = 0;
        }
        public static void setLens(ListItemLens l){lens = l;
        currentApertureRange = new ArrayList<Double>();
        currentApertureRange.add(Math.round(lens.getApertureMinTele()*10)/10.0);
        for (int i = 0;i<apertureRange.length;i++)
        {if(apertureRange[i]>Math.round(lens.getApertureMaxWide()*10)/10.0 && apertureRange[i]<Math.round(lens.getApertureMinTele()*10)/10.0)
            {currentApertureRange.add(apertureRange[i]);}
        }
        currentApertureRange.add(Math.round(lens.getApertureMaxWide()*10)/10.0);

        currentFocalLengthRange = new ArrayList<Double>();
        currentFocalLengthRange.add(lens.getMinZoom());
        for (int i = 0;i<focalLengthRange.length;i++)
        {if(focalLengthRange[i]>lens.getMinZoom() && focalLengthRange[i]<lens.getMaxZoom())
            {currentFocalLengthRange.add(focalLengthRange[i]);}
        }
        currentFocalLengthRange.add(lens.getMaxZoom());
        }

        public static double getPreviewSS(){return previewSS;}
        public static double getPreviewISO(){return previewISO;}
        public static void setPreviewSS(double ss){previewSS = ss;}
        public static void setPreviewISO(double iso){previewISO = iso;}
        public static double getFocalLength(){focalLength = currentFocalLengthRange.get(focalLengthStep); return focalLength;}
        public static double getAperture(){aperture = currentApertureRange.get(apertureStep); return aperture;}
        public static double getShutterSpeed(){shutterSpeed = currentshutterSpeedRange.get(shutterSpeedStep); return shutterSpeed;}
        public static int getISO(){ISO = currentIsoRange.get(isoStep); return ISO;}
        public static double getDistance(){return distance;}
        public static String getFocalLengthString() { format.setDecimalSeparatorAlwaysShown(false); return format.format(getFocalLength()); }
        public static String getApertureString() { format.setDecimalSeparatorAlwaysShown(false); return format.format(getAperture()); }
        public static String getShutterSpeedString() { format.setDecimalSeparatorAlwaysShown(false); format.setGroupingUsed(false);if(getShutterSpeed()>=1) return format.format(getShutterSpeed())+"\"";else return format.format(1/getShutterSpeed());}
        public static String getISOString() { return Integer.toString(getISO());}
        public static boolean isPrimeLens(){return lens.getMaxZoom()==lens.getMinZoom();}
        public static boolean isFixedApertureLens(){return lens.getApertureMaxTele()==lens.getApertureMinTele() && lens.getApertureMaxWide()== lens.getApertureMinWide();}
        public static String focalLengthPlus(){focalLengthStep++;if(focalLengthStep>currentFocalLengthRange.size()-1){focalLengthStep = currentFocalLengthRange.size()-1;}focalLength = currentFocalLengthRange.get(focalLengthStep); return getFocalLengthString();}
        public static String aperturePlus(){apertureStep++; if(apertureStep>currentApertureRange.size()-1){apertureStep = currentApertureRange.size()-1;}aperture = currentApertureRange.get(apertureStep); focusRefresh(); return getApertureString();}
        public static String shutterSpeedPlus(){shutterSpeedStep++; if(shutterSpeedStep>currentshutterSpeedRange.size()-1){shutterSpeedStep = currentshutterSpeedRange.size()-1;}shutterSpeed = currentshutterSpeedRange.get(shutterSpeedStep);return getShutterSpeedString();}
        public static String isoPlus(){isoStep++; if(isoStep>currentIsoRange.size()-1){isoStep = currentIsoRange.size()-1;}ISO = currentIsoRange.get(isoStep);return getISOString();}
        public static String focalLengthMinus(){focalLengthStep--; if(focalLengthStep<1){focalLengthStep = 0;}focalLength = currentFocalLengthRange.get(focalLengthStep);return getFocalLengthString();}
        public static String apertureMinus(){apertureStep--; if(apertureStep<1){apertureStep = 0;}aperture = currentApertureRange.get(apertureStep); focusRefresh(); return getApertureString();}
        public static String shutterSpeedMinus(){shutterSpeedStep--; if(shutterSpeedStep<1){shutterSpeedStep = 0;}shutterSpeed = currentshutterSpeedRange.get(shutterSpeedStep);return getShutterSpeedString();}
        public static String isoMinus(){isoStep--;if(isoStep<1){isoStep = 0;}ISO = currentIsoRange.get(isoStep);return getISOString();}
        public static String focusPlus(){distance = distance * 1.25; if(distance>HyperfocalCalculator()){distance=HyperfocalCalculator();}return focusRefresh();}
        public static String focusMinus(){distance = distance / 1.25;return focusRefresh(); }
        public static String focusRefresh()
        {
            if(distance>HyperfocalCalculator()){distance=HyperfocalCalculator();}
            if(distance<lens.getMinFocusDistance()){distance=lens.getMinFocusDistance();}
            dofNear = DofNearCalculator();
            dofFar = DofFarCalculator();
            if(dofFar>Double.MAX_VALUE-1 || dofFar<0){dofFar = Double.POSITIVE_INFINITY;}
            if(dofNear>Double.MAX_VALUE-1 || dofNear<0){dofNear = Double.POSITIVE_INFINITY;}
            return String.valueOf(distance);
        }
        public static String getDofNear(){return String.format("%.02f", dofNear);}
        public static String getDofFar(){return String.format("%.02f", dofFar);}
        public static void refreshDistance(){distance = Intelligence.HyperfocalCalculator();}
    }

    private static double CoCCalculator(){
        //CoC (mm) = viewing distance (cm) / desired final-image resolution (lp/mm) for a 25 cm viewing distance / enlargement / 25
        //assuming worst case 60cm viewing distance on a 27inch 4k monitor
        double sensorSizeX = Current.getBody().getSensorSizeX();
        double sensorSizeY = Current.getBody().getSensorSizeY();
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

        return viewingDistance/lpmm/enlargement/25.0;
    }
    public static double HyperfocalCalculator(){
        //double x= (Current.getFocalLength()*Current.getFocalLength()/(CoCCalculator()*Current.getAperture())+Current.getFocalLength())/1000;
        return (Current.getFocalLength()*Current.getFocalLength()/(CoCCalculator()*Current.getAperture())+Current.getFocalLength())/1000;
    }
    protected static double DofNearCalculator(){
        return (Current.getDistance()*(HyperfocalCalculator())/(HyperfocalCalculator()+Current.getDistance()));
    }
    protected static double DofFarCalculator(){
        return (Current.getDistance()*(HyperfocalCalculator())/(HyperfocalCalculator()-Current.getDistance()));
    }
    public static double ExposureCalculator(){
        return (1/0.44)*Math.log10((1.0/Current.getAperture())*(1.0/Current.getAperture())*Current.getShutterSpeed()*Current.getISO())/Math.log(2)
        //return 0
        -(1/0.44)*Math.log10(Current.getPreviewSS()*Current.getPreviewISO())/Math.log(2)+30;
    }


}
