package hr.kodbiro.marko.cameratesting.views;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.io.IOException;
import java.util.List;

/**
 * Camera-preview view that CameraFragment inflates into FrameLayout
 * Created by Marko on 10/03/2015.
 */

@SuppressWarnings("deprecation")
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = CameraPreview.class.getSimpleName();
    private Camera.AutoFocusCallback autoFocusCallback;
    private Camera.PreviewCallback previewCallback;
    private Camera.PictureCallback pictureCallback;

    public Camera camera;
    private int cameraId;
    private Context context;
    private SurfaceHolder holder;
    private View cameraView;

    private Camera.Parameters parameters;
    private List<Camera.Size> supportedPreviewSizes;
    private Camera.Size previewSize;
    private List<String> supportedFocusModes;
    private String focusMode;


    public CameraPreview(Context context, int cameraId, Camera camera, View cameraView, Camera.AutoFocusCallback autoFocusCallback, Camera.PreviewCallback previewCallback) {
        super(context);

        this.autoFocusCallback = autoFocusCallback;
        this.previewCallback = previewCallback;

        this.context = context;
        this.camera = camera;
        this.cameraView = cameraView;
        this.cameraId = cameraId;

        holder = getHolder();
        holder.addCallback(this);
        holder.setKeepScreenOn(true);
    }

    public void startCameraPreview() {
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(holder);
        } catch (IOException ioe) {
            Log.i(TAG, ioe.getMessage());
            ioe.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (holder.getSurface() == null) {
            return;
        }

        try {
            parameters = camera.getParameters();
            supportedFocusModes = camera.getParameters().getSupportedFocusModes();
            supportedPreviewSizes = camera.getParameters().getSupportedPreviewSizes();

            focusMode = getFocusMode(supportedFocusModes);
            previewSize = getPreviewSize(supportedPreviewSizes);

            setCameraDisplayOrientation(context, cameraId, camera);

            parameters.setFocusMode(focusMode);
            parameters.setPreviewSize(previewSize.width, previewSize.height);
            camera.setParameters(parameters);
            camera.autoFocus(autoFocusCallback);
            camera.setPreviewCallback(previewCallback);
            camera.startPreview();
        } catch (Exception e) {
            Log.i(TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (camera != null) {
            camera.stopPreview();
        }
    }

    public static void setCameraDisplayOrientation(Context context, int cameraId, android.hardware.Camera camera) {
        Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);

        int rotation = ((Activity) context).getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    private Camera.Size getPreviewSize(List<Camera.Size> sizesList) {
        Camera.Size largestSize = sizesList.get(0);

        for (Camera.Size size : sizesList) {
            if (size.width > largestSize.width) {
                largestSize = size;
            }
        }

        return largestSize;
    }

    private String getFocusMode(List<String> supportedFocusModes) {
        return supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE) ? Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE : Camera.Parameters.FOCUS_MODE_AUTO;
    }
}