package com.example.org.gvrfapplication;

import android.os.Bundle;

import org.gearvrf.GVRActivity;
import org.gearvrf.GVRContext;
import org.gearvrf.GVRMain;
import org.gearvrf.GVRScene;
import org.gearvrf.scene_objects.GVRCameraSceneObject;
import org.gearvrf.utility.Log;

public class MainActivity extends GVRActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * Set Main Scene
         * It will be displayed when app starts
         */
        setMain(new Main());
    }

    private final class Main extends GVRMain {

        @Override
        public void onInit(GVRContext gvrContext) throws Throwable {

            //Load texture
            //GVRTexture texture = gvrContext.getAssetLoader().loadTexture(new GVRAndroidResource(gvrContext, R.drawable.__default_splash_screen__));


            GVRScene scene = gvrContext.getMainScene();
            //Create a rectangle with the camera view
            GVRCameraSceneObject cameraObject = null;
            // Check if user has given permission to record audio
//            final int MY_CAMERA_REQUEST_CODE = 100;
//
//            if (checkSelfPermission(Manifest.permission.CAMERA)
//                    != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(new String[]{Manifest.permission.CAMERA},
//                        MY_CAMERA_REQUEST_CODE);
//            }
//            @Override
//
//            public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//
//                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//                if (requestCode == MY_CAMERA_REQUEST_CODE) {
//
//                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                        Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
//
//                    } else {
//
//                        Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
//
//                    }
//
//                }}//end onRequestPermissionsResult

            try {
                cameraObject = new GVRCameraSceneObject(gvrContext, 3.6f, 2.0f);
                cameraObject.setUpCameraForVrMode(1); // set up 60 fps camera preview.
            } catch (GVRCameraSceneObject.GVRCameraAccessException e) {
                // Cannot open camera
                Log.e(TAG, "Cannot open the camera",e);
            }
            cameraObject.getTransform().setPosition(0, 0, -3);

            //Add rectangle to the scene
            gvrContext.getMainScene().addSceneObject(cameraObject);
        }

        @Override
        public SplashMode getSplashMode() {
            return SplashMode.NONE;
        }

        @Override
        public void onStep() {
            //Add update logic here
        }
    }
}
