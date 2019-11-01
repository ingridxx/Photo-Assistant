package com.example.photoassistant;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class Calculator extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    FloatingActionButton fab;
    private Button aperturePlusButton, shutterSpeedPlusButton, isoPlusButton, zoomPlusButton;
    Button apertureMinusButton, shutterSpeedMinusButton, isoMinusButton, zoomMinusButton;
    TextView apertureTV, shutterSpeedTV, isoTV, zoomTV, desiredDistanceTV, nearDistanceTV, farDistanceTV;
    Button bodyButton, lensButton;
    TextView evTextView;

    Activity activity;
    public Calculator() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }
    private Size mPreviewSize;
    private String mCameraId;
    private TextureView mTextureView;

    private HandlerThread mBackgroundThread;
    private Handler mBackgroundHandler;
    private TotalCaptureResult mCaptureResult;
    private CameraDevice.StateCallback mCameraDeviceStateCallBack = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            mCameraDevice = camera;
            createCameraPreviewSession(1.0);
            WAIT = false;
            // Toast.makeText(activity.getApplicationContext(), "Camera Opened!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            camera.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            camera.close();
            mCameraDevice = null;
        }
    };
    Button button;
    CameraCharacteristics mCameraCharacteristics;


    public void delayCamera()
    {
        if(WAIT) return;
        WAIT = true;
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                createCameraPreviewSession(zoomFactor());
                WAIT = false;
            }
        }, 1000);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_calculator, container, false);
        fab = rootView.findViewById(R.id.floatingActionButton);
        mTextureView = (TextureView) rootView.findViewById(R.id.textureview);

        //createCameraPreviewSession();
        final Handler handler=new Handler();
        handler.post(new Runnable(){
            @Override
            public void run() {
                if(mCaptureResult!=null) {

//                    lensButton.setText("ISO:"+mCaptureResult.get(TotalCaptureResult.SENSOR_SENSITIVITY).toString()+
//                            " SPD:"+mCaptureResult.get(TotalCaptureResult.SENSOR_EXPOSURE_TIME)+
//                            " DIST:"+0.61/mCaptureResult.get(TotalCaptureResult.LENS_FOCUS_DISTANCE)+
//                            " MM:"+mCaptureResult.get(TotalCaptureResult.LENS_FOCAL_LENGTH));
                    Intelligence.Current.setPreviewSS(mCaptureResult.get(TotalCaptureResult.SENSOR_EXPOSURE_TIME));
                    Intelligence.Current.setPreviewISO(mCaptureResult.get(TotalCaptureResult.SENSOR_SENSITIVITY));
//                    lensButton.setText(String.valueOf(Intelligence.ExposureCalculator())+"ISO:"+mCaptureResult.get(TotalCaptureResult.SENSOR_SENSITIVITY).toString()+
//                            " SPD:"+mCaptureResult.get(TotalCaptureResult.SENSOR_EXPOSURE_TIME));
                    updateUI();
                }
                handler.postDelayed(this,250); // set time here to refresh textView
            }
        });
        rootView.setOnTouchListener(new View.OnTouchListener() {
            private long startClickTime;
            float offsetY, offsetX, currentPosY, currentPosX;
            float initialY,initialX, sensitivityX = 75, sensitivityY = 50;
            float moverX, moverY;
            boolean tracking = true;
            int box;
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                int[] rect = new int[]{0,0};
                rect = new int[]{0,0};shutterSpeedTV.getLocationOnScreen(rect);
                Rect shutterRect= new Rect(rect[0], rect[1], rect[0]+shutterSpeedTV.getWidth(), rect[1]+shutterSpeedTV.getHeight());
                rect = new int[]{0,0};apertureTV.getLocationOnScreen(rect);
                Rect apertureRect= new Rect(rect[0], rect[1], rect[0]+apertureTV.getWidth(), rect[1]+apertureTV.getHeight());
                rect = new int[]{0,0};isoTV.getLocationOnScreen(rect);
                Rect isoRect= new Rect(rect[0], rect[1], rect[0]+isoTV.getWidth(), rect[1]+isoTV.getHeight());
                rect = new int[]{0,0};zoomTV.getLocationOnScreen(rect);
                Rect focalLengthRect= new Rect(rect[0], rect[1], rect[0]+zoomTV.getWidth(), rect[1]+zoomTV.getHeight());
                rect = new int[]{0,0}; desiredDistanceTV.getLocationOnScreen(rect);
                Rect focusRect= new Rect(rect[0], rect[1], rect[0]+desiredDistanceTV.getWidth(), rect[1]+desiredDistanceTV.getHeight());
                //Log.d("TAG", String.valueOf(event.getAction()));
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    initialY = event.getAxisValue(MotionEvent.AXIS_Y);
                    initialX = event.getAxisValue(MotionEvent.AXIS_X);
                    int x = (int)event.getX(); int y = (int)event.getY();
                    moverX=0;moverY=0;
                    if (apertureRect.contains(x,y)) {
                        box = 1;
                    }
                    else if (shutterRect.contains(x,y)) {
                        box = 2;
                    }
                    else if (isoRect.contains(x,y)) {
                        box = 3;
                    }
                    else if (focalLengthRect.contains(x,y)) {
                        box = 4;
                    }
                    else if (focusRect.contains(x,y)) {
                        box = 5;
                    }
                    else
                        box=0;
                    Log.d("TAG", String.valueOf(box));

                }

                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    currentPosY=event.getAxisValue(MotionEvent.AXIS_Y);
                    offsetY = (int)(currentPosY-initialY)/sensitivityY;
                    currentPosX=event.getAxisValue(MotionEvent.AXIS_X);
                    offsetX = (int)(currentPosX-initialX)/sensitivityX;

                    if(moverY<offsetY)
                        for(moverY = moverY;moverY<offsetY;moverY++)
                        {
                            switch(box)
                            {
                                case 1:Intelligence.Current.apertureMinus(); break;
                                case 2:Intelligence.Current.shutterSpeedMinus();break;
                                case 3:Intelligence.Current.isoMinus();break;
                                case 4:Intelligence.Current.focalLengthMinus();
                                    delayCamera();break;
                            }
                        }
                    else
                    {
                        for(moverY = moverY;moverY>offsetY;moverY--)
                        {
                            switch(box)
                            {
                                case 1:Intelligence.Current.aperturePlus();break;
                                case 2:Intelligence.Current.shutterSpeedPlus();break;
                                case 3:Intelligence.Current.isoPlus();break;
                                case 4:Intelligence.Current.focalLengthPlus();
                                    delayCamera();break;
                            }
                        }
                    }
                    if(moverX<offsetX)
                        for(moverX = moverX;moverX<offsetX;moverX++)
                        {
                            switch(box)
                            {
                                case 5:Intelligence.Current.focusPlus();break;
                            }
                        }
                    else
                    {
                        for(moverX = moverX;moverX>offsetX;moverX--)
                        {
                            switch(box)
                            {
                                case 5:Intelligence.Current.focusMinus();break;
                            }
                        }
                    }

                }
                if(event.getAction() == MotionEvent.ACTION_UP)
                {

                }
                return true;
            }
        });
        return rootView;
    }



    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        //hide notification bar
        View tempView = getActivity().getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE;
        tempView.setSystemUiVisibility(uiOptions);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("myDebugTag", "onClickOfFAB:");
                getActivity().onBackPressed();
            }
        });
        //force landscape
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //bind ui elements to java objects
        aperturePlusButton = view.findViewById(R.id.aperturePlusButton);
        shutterSpeedPlusButton = view.findViewById(R.id.shutterSpeedPlusButton);
        zoomPlusButton = view.findViewById(R.id.zoomPlusButton);
        isoPlusButton = view.findViewById(R.id.isoPlusButton);
        apertureMinusButton = view.findViewById(R.id.apertureMinusButton);
        shutterSpeedMinusButton = view.findViewById(R.id.shutterSpeedMinusButton);
        zoomMinusButton = view.findViewById(R.id.zoomMinusButton);
        isoMinusButton = view.findViewById(R.id.isoMinusButton);
        apertureTV = view.findViewById(R.id.apertureTV);
        shutterSpeedTV = view.findViewById(R.id.shutterSpeedTV);
        zoomTV = view.findViewById(R.id.zoomTV);
        isoTV = view.findViewById(R.id.isoTV);
        desiredDistanceTV = view.findViewById(R.id.DesiredDisanceTV);
        nearDistanceTV = view.findViewById(R.id.NearDistanceTV);
        farDistanceTV = view.findViewById(R.id.FarDistanceTV);
        bodyButton = view.findViewById(R.id.cameraSelectButton);
        lensButton = view.findViewById(R.id.lensSelectButton);
        evTextView = view.findViewById(R.id.evTextView);
        mTextureView = view.findViewById(R.id.textureview);
        if(BodySelector.isParsable(BodySelector.getBodySlot(BodySelector.getWhichSlot()))){
            Intelligence.Current.setBody(BodySelector.getBodySlot(BodySelector.getWhichSlot()));
            Intelligence.Current.setLens(BodySelector.getLensSlot(BodySelector.getWhichSlot()));
        }


        bodyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                LinearLayout equipmentRelativeLayout = view.findViewById(R.id.equipmentRelativeLayout);
//                FrameLayout frameLayout1 = view.findViewById(R.id.frame_layout_1);
//                ConstraintLayout calc_body_screen = view.findViewById(R.id.calc_body_screen);
//                double newWidth = equipmentRelativeLayout.getWidth()+frameLayout1.getWidth();
//                double newHeight = calc_body_screen.getHeight()-frameLayout1.getHeight();
//                double newX = equipmentRelativeLayout.getX();
//                double newY = equipmentRelativeLayout.getY()+equipmentRelativeLayout.getHeight();
//                newHeight = newWidth/1.5;
////                if(1.0*newWidth/newHeight>1.5)
////                {
////                    newHeight = newHeight*(1/1.5)*newWidth/newHeight;
////                }
////                else {
////                    newWidth = newWidth*(1/1.5)*newWidth/newHeight;
////                }
//                mTextureView.setLeft((int)newX);
//                mTextureView.setTop((int)newY);
//                mTextureView.setRight((int)(newHeight+newX));
//                mTextureView.setBottom((int)(newWidth+newY));
//                Log.d("TAG",newWidth+","+newHeight+","+newX+","+newY);
                //mTextureView.setLayoutParams(new ConstraintLayout.LayoutParams((int)newWidth, (int)newHeight));
                if(!BodySelector.empty())
                {
                    BodySelector.nextSlot();
                    Intelligence.Current.setBody(BodySelector.getBodySlot(BodySelector.getWhichSlot()));
                    Intelligence.Current.setLens(BodySelector.getLensSlot(BodySelector.getWhichSlot()));
                    Intelligence.Current.refreshDistance();
                    delayCamera();
                }



            }
        });

        lensButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!BodySelector.empty())
                {
                    BodySelector.nextLens();
                    Intelligence.Current.setLens(BodySelector.getLensSlot(BodySelector.getWhichSlot()));
                    Intelligence.Current.refreshDistance();
                    delayCamera();
                }

            }
        });

        aperturePlusButton.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { Intelligence.Current.aperturePlus();}});
        shutterSpeedPlusButton.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { Intelligence.Current.shutterSpeedPlus(); }});
        zoomPlusButton.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { Intelligence.Current.focalLengthPlus(); delayCamera();}});
        isoPlusButton.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { Intelligence.Current.isoPlus();}});
        apertureMinusButton.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { Intelligence.Current.apertureMinus();}});
        shutterSpeedMinusButton.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { Intelligence.Current.shutterSpeedMinus(); }});
        zoomMinusButton.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { Intelligence.Current.focalLengthMinus(); delayCamera(); }});
        isoMinusButton.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { Intelligence.Current.isoMinus(); }});

        updateUI();
        int count=0,maxTries=10;
        createCameraPreviewSession(1.0);
//        while(count<maxTries)
//        {
//            try
//            {
//                createCameraPreviewSession(zoomFactor());
//            }catch (NullPointerException ee)
//            {
//                count++;if(count>=maxTries) throw ee;
//            }
//        }

    }
    public double zoomFactor()
    {
        try
        {
            double zoomFactor = Intelligence.Current.getEquivalentFocalLength()/getPhoneEquivalentFocalLength(mCameraCharacteristics);
            double maxZoom = mCameraCharacteristics.get(CameraCharacteristics.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM);
            if(zoomFactor<=1) return 1.0;
            else if(zoomFactor>maxZoom) return maxZoom;
            else return zoomFactor;

        }
        catch (NullPointerException e)
        {
            return 1.0;
        }



    }
    public double getPhoneEquivalentFocalLength(CameraCharacteristics mCameraCharacteristics)
    {
        try
        {
            return mCameraCharacteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS)[0]*
                    Math.sqrt(864/
                            (mCameraCharacteristics.get(CameraCharacteristics.SENSOR_INFO_PHYSICAL_SIZE).getWidth()
                                    *mCameraCharacteristics.get(CameraCharacteristics.SENSOR_INFO_PHYSICAL_SIZE).getHeight()));
        }catch (NullPointerException e)
        {
            return 1.0;
        }

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    int jumpStartCount=0,jumpStartMaxTries=2;
    public void updateUI()
    {
        //Intelligence.Current.setBody(Intelligence.Current.getBody());
        //Intelligence.Current.setLens(Intelligence.Current.getLens());
        if(Intelligence.Current.isPrimeLens())
        {
            zoomPlusButton.setEnabled(false);
            zoomPlusButton.setVisibility(View.INVISIBLE);
            zoomMinusButton.setEnabled(false);
            zoomMinusButton.setVisibility(View.INVISIBLE);
        }
        else
        {
            zoomPlusButton.setEnabled(true);
            zoomPlusButton.setVisibility(View.VISIBLE);
            zoomMinusButton.setEnabled(true);
            zoomMinusButton.setVisibility(View.VISIBLE);
        }
        if(Intelligence.Current.isFixedApertureLens())
        {
            aperturePlusButton.setEnabled(false);
            aperturePlusButton.setVisibility(View.INVISIBLE);
            apertureMinusButton.setEnabled(false);
            apertureMinusButton.setVisibility(View.INVISIBLE);
        }
        else
        {
            aperturePlusButton.setEnabled(true);
            aperturePlusButton.setVisibility(View.VISIBLE);
            apertureMinusButton.setEnabled(true);
            apertureMinusButton.setVisibility(View.VISIBLE);
        }
        apertureTV.setText(Intelligence.Current.getApertureString());
        shutterSpeedTV.setText(Intelligence.Current.getShutterSpeedString());
        zoomTV.setText(Intelligence.Current.getFocalLengthString());
        isoTV.setText(Intelligence.Current.getISOString());
        bodyButton.setText(Intelligence.Current.getBody().getPartName());
        lensButton.setText(Intelligence.Current.getLens().getSimpleName());
        evTextView.setText(String.format("%.02f", Intelligence.ExposureCalculator()));
        desiredDistanceTV.setText(String.format("%.02f", Intelligence.Current.getDistance()));
        Intelligence.Current.focusRefresh();
        nearDistanceTV.setText(Intelligence.Current.getDofNear());
        farDistanceTV.setText(Intelligence.Current.getDofFar());

        if(jumpStartCount<jumpStartMaxTries)
        {
            createCameraPreviewSession(zoomFactor());
            jumpStartCount++;
        }



    }








    public static boolean WAIT = false;



















    private static final int REQUEST_CAMERA_PERMISSION_RESULT=0;
    private void connectCamera()
    {
        CameraManager cameraManager = (CameraManager) getActivity().getSystemService(Context.CAMERA_SERVICE);
        try {
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
            {
                if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED)
                {
                    cameraManager.openCamera(mCameraId, mCameraDeviceStateCallBack, mBackgroundHandler);
                }
                else
                {
                    if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA))
                    {
                        Toast.makeText(getContext(), "Requires camera permissions", Toast.LENGTH_SHORT).show();
                    }
                    requestPermissions(new String[]{Manifest.permission.CAMERA},REQUEST_CAMERA_PERMISSION_RESULT);
                }
            }
            else
            {

            }

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    private TextureView.SurfaceTextureListener mSurfaceTextureListener =
            new TextureView.SurfaceTextureListener() {
                @Override
                public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {

                    setupCamera(width, height);

                    transformImage(width, height);

                    openCamera();
                }

                @Override
                public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

                }

                @Override
                public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                    return false;
                }

                @Override
                public void onSurfaceTextureUpdated(SurfaceTexture surface) {

                }
            };
    private CameraDevice mCameraDevice;

    private CaptureRequest mPreviewCaptureRequest;
    private CaptureRequest.Builder mPreviewCaptureRequestBuilder;
    private CameraCaptureSession mCameraCaptureSession;
    public CameraCaptureSession.CaptureCallback mSessionCaptureCallback
            = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureStarted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, long timestamp, long frameNumber) {
            super.onCaptureStarted(session, request, timestamp, frameNumber);

        }
    };


    private void setupCamera(int width, int height) {
        CameraManager cameraManager = (CameraManager) getActivity().getSystemService(Context.CAMERA_SERVICE);
        float minFocalLength=Float.MAX_VALUE;

        try {
            for (String cameraId : cameraManager.getCameraIdList()) {
                CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId);
                if (cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) ==
                        CameraCharacteristics.LENS_FACING_FRONT) {
                    continue;
                }
                if(cameraCharacteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS)[0]<minFocalLength)
                {
                    minFocalLength =cameraCharacteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS)[0];
                    StreamConfigurationMap map = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                    mPreviewSize = getPreferredPreviewSize(map.getOutputSizes(SurfaceTexture.class), width, height);
                    mCameraCharacteristics=cameraCharacteristics;
                    mPreviewSize = mPreviewSize;
                    mCameraId = cameraId;
                }




            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    private Size getPreferredPreviewSize(Size[] mapSizes, int width, int height) {
        List<Size> collectorSizes = new ArrayList<>();
        for (Size option : mapSizes) {
            if (width > height) {
                if (option.getWidth() > width && option.getHeight() > height) {
                    collectorSizes.add(option);
                }
            } else {
                if (option.getWidth() > height &&
                        option.getHeight() > width) {
                    collectorSizes.add(option);
                }
            }
        }
        if (collectorSizes.size() > 0) {
            return Collections.min(collectorSizes, new Comparator<Size>() {
                @Override
                public int compare(Size lhs, Size rhs) {
                    return Long.signum(lhs.getWidth() * lhs.getHeight() - rhs.getWidth() * rhs.getHeight());
                }
            });

        }
        return mapSizes[0];
    }



    @Override
    public void onResume() {
        super.onResume();

        openBackgroundThread();
        if (mTextureView.isAvailable()) {
            setupCamera(mTextureView.getWidth(), mTextureView.getHeight());

            transformImage(mTextureView.getWidth(), mTextureView.getHeight());

            openCamera();
        } else {
            mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
        }
    }
    @Override
    public void onPause() {

        closeCamera();
        closeBackgroundThread();

        super.onPause();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void openCamera() {
        CameraManager cameraManager = (CameraManager) getActivity().getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraManager.openCamera(mCameraId, mCameraDeviceStateCallBack, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void closeCamera() {
        if(mCameraCaptureSession != null) {
            mCameraCaptureSession.close();
            mCameraCaptureSession = null;
        }
        if (mCameraDevice != null){
            mCameraDevice.close();
            mCameraDevice = null;
        }
    }


    private void createCameraPreviewSession(double zoom) {
        if(mTextureView==null||mPreviewSize==null ||mCameraDevice==null) return;
        final SurfaceTexture surfaceTexture = mTextureView.getSurfaceTexture();
        surfaceTexture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
        Surface previewSurface = new Surface(surfaceTexture);


            try {
                double target = Intelligence.Current.getAspectRatio();
                double width = mCameraCharacteristics.get(CameraCharacteristics.SENSOR_INFO_PIXEL_ARRAY_SIZE).getWidth();
                double height = mCameraCharacteristics.get(CameraCharacteristics.SENSOR_INFO_PIXEL_ARRAY_SIZE).getHeight();
                double source = width/height;
                double newVar, margin;
                Rect rect;
                if(source<target)
                {
                    newVar = width/target;
                    margin = (height-newVar)/2;
                    rect = new Rect(0,(int)margin,(int)width, (int)(margin + newVar));
                }
                else
                {
                    newVar = height*target;
                    margin = (width-newVar)/2;
                    rect = new Rect((int)margin,0,(int)(margin+newVar), (int)(height));
                }
                rect = new Rect(rect.left, rect.top, (int)(rect.right*1.0/zoom), (int)(rect.bottom*1.0/zoom));


                mPreviewCaptureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                mPreviewCaptureRequestBuilder.set(CaptureRequest.SCALER_CROP_REGION, rect);
                mPreviewCaptureRequestBuilder.addTarget(previewSurface);
                mPreviewCaptureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                mPreviewCaptureRequestBuilder.set(CaptureRequest.CONTROL_AE_EXPOSURE_COMPENSATION, 0);

                mCameraDevice.createCaptureSession(Arrays.asList(previewSurface), new CameraCaptureSession.StateCallback() {
                    @Override
                    public void onConfigured(@NonNull CameraCaptureSession session) {
                        if (mCameraDevice == null) {
                            return;
                        }
                        int count = 0,maxTries = 100;
                        while(count<maxTries)
                        {
                            try {

                                session.setRepeatingRequest(mPreviewCaptureRequestBuilder.build(),  new CameraCaptureSession.CaptureCallback() {
                                    @Override
                                    public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                                        mCaptureResult = result;
                                    }
                                }, mBackgroundHandler);


                                break;
                            }
                            catch (IllegalStateException ee)
                            {
                                count++;if(count>=maxTries) throw ee;
                            }catch (CameraAccessException e) {
                                e.printStackTrace();}
                        }
                    }
                    @Override
                    public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                        Toast.makeText(getContext(), "create camera session failed!",
                                Toast.LENGTH_SHORT).show();
                    }
                },null);
            }catch (CameraAccessException e){e.printStackTrace();}



    }

    private void openBackgroundThread() {
        mBackgroundThread = new HandlerThread("Camera2 background thread");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }
    private void closeBackgroundThread() {
        mBackgroundThread.quitSafely();
        try{
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_CAMERA_PERMISSION_RESULT)
        {
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(getContext(),"Application wont run without camera services", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public CaptureRequest.Builder createCaptureRequest(int template) throws CameraAccessException {
        CameraDevice device = mCameraDevice;
        if (device == null) {
            throw new IllegalStateException("Can't get requests when no camera is open");
        }
        return device.createCaptureRequest(template);
    }



    private void transformImage(int width, int height) {
        if (mPreviewSize == null || mTextureView == null) {
            return;
        }
        Matrix matrix = new Matrix();
        int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
        RectF textureRectF = new RectF(0,0, width, height);
        RectF previewRectF = new RectF(0, 0, mPreviewSize.getWidth(), mPreviewSize.getHeight());
        float centerX = textureRectF.centerX();
        float centerY = textureRectF.centerY();
        if(rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) {
            //previewRectF.offset(centerX - previewRectF.centerX(), centerY - previewRectF.centerY());
            //matrix.setRectToRect(textureRectF, previewRectF, Matrix.ScaleToFit.CENTER);
            //float scale = Math.max((float)width / mPreviewSize.getWidth(), (float)height/ mPreviewSize.getHeight());
            //matrix.postScale(scale, scale, centerX, centerY);
            matrix.preRotate(90* (rotation-2), centerX, centerY);
            matrix.preScale((float)1.0*height/width, (float)1.0*width/height, centerX, centerY);

        }
        double previewAspect = 1.0*previewRectF.width()/previewRectF.height();
        double textureAspect = 1.0*width/height;
        if(previewAspect>textureAspect)
        {mTextureView.setScaleX((float) (1.0*previewAspect*1.0/textureAspect));
            }
        else
        {
            mTextureView.setScaleY((float)(1.0/(previewAspect*1.0/textureAspect)));
        }
        mTextureView.setTransform(matrix);
    }

}