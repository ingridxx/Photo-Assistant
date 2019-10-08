package com.example.photoassistant;

public class Intelligence {

    public static class Current
    {

    }
    //CoCCalculator(SensorSize)
    //HyperfocalCalculator(CoC)
    //DoFNearCalculator(Hyperfocal, subjectDistance, focalLength)
    //DoFFarCalculator(Hyperfocal, subjectDistance, focalLength)
    //EVCalculator(aperture, ss, ISO, previewSS, previewISO)

    public static double CoCCalculator(double SensorSizeX, double SensorSizeY)
    {
        //CoC (mm) = viewing distance (cm) / desired final-image resolution (lp/mm) for a 25 cm viewing distance / enlargement / 25
        //assuming worst case 60cm viewing distance on a 27inch 4k monitor

        int viewingDistance = 60;
        int monitorSize = 27;
        int monitorX = 3840;
        int monitorY = 2160;
        int monitorAspectRatioX = 16;
        int monitorAspectRatioY = 9;
        double monitorkMM =monitorSize*2.54*10/(monitorAspectRatioX*monitorAspectRatioX+monitorAspectRatioY*monitorAspectRatioY);//reverse pythagoras theorem
        double lpmm = 1.0*(monitorX/monitorAspectRatioX)/monitorkMM;

        double sensorSize = SensorSizeX*SensorSizeY;
        double viewSize = monitorAspectRatioX*monitorkMM*monitorAspectRatioY*monitorkMM;
        double enlargement = Math.sqrt(viewSize/sensorSize);

        return viewingDistance/lpmm/enlargement/25;
    }
    public static double HyperfocalCalculator(double CoC)
    {
        int focalLength = 50;
        double fNumber = 2.8;
        return focalLength*focalLength/CoC/fNumber+focalLength;
    }
    public static double DofNearCalculator(double hyperfocal, double distance, double focalLength){
        return distance*(hyperfocal-focalLength)/(hyperfocal+distance-2*focalLength);
    }
    public static double DofFarCalculator(double hyperfocal, double distance, double focalLength){
        return distance*(hyperfocal-focalLength)/(hyperfocal+distance);
    }
    public static double ExposureCalculator(double aperture, double ss, double iso, double previewSS, double previewISO){
        return aperture*aperture*ss*iso/(previewSS*previewISO);
    }
}
