package hr.kodbiro.marko.cameratesting.fragments;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import hr.kodbiro.marko.cameratesting.R;

/**
 * Created by Marko on 10/03/2015.
 *
 * Sample fragment extending CameraFragment and using it's methods to start fullscreen camera preview fragment
 * Override CameraFragment's Callback methods to implement actions
 * @version 0.2.1
 *
 */
public class PictureFragment extends CameraFragment {

    /**
     * Camera ID sent to CameraFragment to start a specific camera
     */
    private int cameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
    private static final String TAG = PictureFragment.class.getSimpleName();

    public PictureFragment() {
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Button takePhotoButton = (Button) view.findViewById(R.id.take_photo_button);
        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setCameraOptions(cameraId);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * Sample override of a callback (pictureCallback) method from CameraFragment
     *
     */
    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        Toast.makeText(getActivity(), "foto foto", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAutoFocus(boolean success, Camera camera) {
        Toast.makeText(getActivity(), "focus", Toast.LENGTH_SHORT).show();
    }
}
