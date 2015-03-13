package hr.kodbiro.marko.cameratesting.fragments;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import hr.kodbiro.marko.cameratesting.R;
import hr.kodbiro.marko.cameratesting.views.CameraPreview;

/**
 * CameraFragment - handles CameraPreview inflating and contains camera control methods
 * TODO smoother restart on rotation
 * Created by Marko on 10/03/2015.
 */
public class CameraFragment extends Fragment implements Camera.PictureCallback, Camera.AutoFocusCallback, Camera.PreviewCallback, Camera.ShutterCallback {

    private static final String TAG = "CameraFragment";
    private static Camera.AutoFocusCallback autoFocusCallback;
    private static Camera.PreviewCallback previewCallback;

    private Camera camera;
    private CameraPreview cameraPreview;
    private View viewOnTop;
    private FrameLayout preview;


    private int cameraId;

    public CameraFragment() {
        Log.d("TEST", TAG);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TEST", TAG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        viewOnTop = inflater.inflate(R.layout.fragment_picture, container, false);

        preview = (FrameLayout) view.findViewById(R.id.camera_preview);

        autoFocusCallback = this;
        previewCallback = this;

        boolean cameraStarted = startCameraInView(view);
        if (!cameraStarted) {
            Log.i(TAG, "Failed to start camera");
            return view;
        }
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        releaseCameraAndPreview();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseCameraAndPreview();
    }

    private boolean startCameraInView(View view) {
        boolean cameraOpen;
        releaseCameraAndPreview();
        camera = getCameraInstance();
        cameraOpen = (camera != null);

        if (cameraOpen) {
            // TODO add shutterCallback to constructor parameters
            cameraPreview = new CameraPreview(getActivity(), cameraId, camera, view, autoFocusCallback, previewCallback);
            preview.addView(cameraPreview);
            cameraPreview.startCameraPreview();
            // inflate subfragment's layout over camera layout
            FrameLayout content = (FrameLayout) view.findViewById(R.id.camera_surface);
            content.addView(viewOnTop);
        }
        return cameraOpen;
    }

    private Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(cameraId);
        } catch (Exception e) {
            Log.d(TAG, Log.getStackTraceString(e));
        }
        return c;
    }

    private void releaseCameraAndPreview() {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
        if (cameraPreview != null) {
            cameraPreview.destroyDrawingCache();
            cameraPreview.camera = null;
            preview.removeAllViews();
        }
    }

    public void setCameraOptions(int cameraId) {
        this.cameraId = cameraId;
    }

    /**
     * Method for taking picture called from extending fragment
     *
     * ADD CALLBACKS AS GLOBALLY NECESSARY
     * TODO check callbacks and what do they do to camera
     */
    public void takePhoto() {
        camera.takePicture(null, null, null, this);
    }


    /**
     * Methods to override in subfragment
     */
    // PictureCallback
    @Override
    public void onPictureTaken(byte[] data, Camera camera) {

    }

    // AutoFocusCallback
    @Override
    public void onAutoFocus(boolean success, Camera camera) {

    }

    // PreviewCallback
    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {

    }

    // ShutterCallback
    @Override
    public void onShutter() {

    }
}
