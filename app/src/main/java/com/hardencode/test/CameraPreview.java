package com.hardencode.test;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.Size;
import android.view.Surface;

import java.util.Arrays;

public class CameraPreview {
    private String cameraId;
    private CameraDevice mCameraDevice;
    private CaptureRequest.Builder mPreviewBuilder;
    private CameraCaptureSession mSession;
    private static final String TAG = "CameraPreview";

    private Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.i(TAG, "handleMessage msg.what = " + msg.what);
        }
    };

    private SurfaceTexture surfaceTexture;

    public void setSurfaceTexture(SurfaceTexture surfaceTexture) {
        this.surfaceTexture = surfaceTexture;
    }

    public void startPreview(Context context)
    {
        cameraId = "" + CameraCharacteristics.LENS_FACING_BACK;
        CameraManager cameraManager = (CameraManager)context.getSystemService(Context.CAMERA_SERVICE);
        try {
            int permission = ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
            if(permission == PackageManager.PERMISSION_GRANTED) {
                CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);
                StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                int[] outPutFormats = map.getOutputFormats();
                for(int outFormats : outPutFormats)
                {
                    Log.i(TAG, "getOutputFormats outFormats = " + outFormats);
                }
                Size[] sizes = map.getOutputSizes(SurfaceTexture.class);
                for(Size size : sizes)
                {
                    Log.i(TAG, " size width = " + size.getWidth() +" height = " + size.getHeight());
                }
//                imageReader = ImageReader.newInstance(320, 240, ImageFormat.YUV_420_888, 2);
//                imageReader.setOnImageAvailableListener(onImageAvailableListener, mHandler);
                cameraManager.openCamera(cameraId, stateCallback, mHandler);
            }
        }catch (Exception e)
        {
            Log.e(TAG, "open camera exception msg = " + e.getMessage());
        }
    }

    private CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            Log.i(TAG, "onConfigured camera = " + camera.getId());
            mCameraDevice = camera;
            try {
                mPreviewBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                if(null != surfaceTexture) {
                    surfaceTexture.setDefaultBufferSize(320, 240);
                    Surface mSurface = new Surface(surfaceTexture);
                    mPreviewBuilder.addTarget(mSurface);

//                Surface mSurface = mSurfaceHolder.getSurface();
//                mPreviewBuilder.addTarget(mSurface);
//                mPreviewBuilder.addTarget(imageReader.getSurface());
                    mCameraDevice.createCaptureSession(Arrays.asList(mSurface), captureStateCallback, mHandler);
                }
            }catch (CameraAccessException e)
            {
                Log.e(TAG, "createCaptureRequest msg = " + e.getMessage());
            }
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            camera.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            Log.i(TAG, "cameradevice error = " + error);
        }
    };

    private CameraCaptureSession.StateCallback captureStateCallback = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(@NonNull CameraCaptureSession session) {
            Log.i(TAG, "onConfigured configured = " + session.getDevice());
            mSession = session;
            mPreviewBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
            try {
                mSession.setRepeatingRequest(mPreviewBuilder.build(), captureCallback, mHandler);
            }catch (CameraAccessException e)
            {
                Log.e(TAG, "onConfigured message = " + e.getMessage());
            }
        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
            Log.e(TAG, "onConfigureFailed session = " + session.toString());
        }
    };

    private CameraCaptureSession.CaptureCallback captureCallback = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureStarted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, long timestamp, long frameNumber) {
            super.onCaptureStarted(session, request, timestamp, frameNumber);
            Log.i(TAG, "onCaptureStarted timestamp = " + timestamp);
        }

        @Override
        public void onCaptureProgressed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureResult partialResult) {
            super.onCaptureProgressed(session, request, partialResult);
            Log.i(TAG, "onCaptureProgressed session = " + session.getDevice());
        }

        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);
            Log.i(TAG, "onCaptureCompleted session = " + session.getDevice());
        }

        @Override
        public void onCaptureFailed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureFailure failure) {
            super.onCaptureFailed(session, request, failure);
            Log.i(TAG, "onCaptureFailed session = " + session.getDevice());
        }

        @Override
        public void onCaptureSequenceCompleted(@NonNull CameraCaptureSession session, int sequenceId, long frameNumber) {
            super.onCaptureSequenceCompleted(session, sequenceId, frameNumber);
            Log.i(TAG, "onCaptureSequenceCompleted session = " + session.getDevice());
        }

        @Override
        public void onCaptureSequenceAborted(@NonNull CameraCaptureSession session, int sequenceId) {
            super.onCaptureSequenceAborted(session, sequenceId);
            Log.i(TAG, "onCaptureSequenceAborted session = " + session.getDevice());

        }

        @Override
        public void onCaptureBufferLost(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull Surface target, long frameNumber) {
            super.onCaptureBufferLost(session, request, target, frameNumber);
            Log.i(TAG, "onCaptureBufferLost session = " + session.getDevice());

        }
    };
}
