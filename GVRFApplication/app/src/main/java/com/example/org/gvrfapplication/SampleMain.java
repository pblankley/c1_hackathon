package com.example.org.gvrfapplication;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.gearvrf.GVRAndroidResource;
import org.gearvrf.GVRContext;
import org.gearvrf.GVRMaterial;
import org.gearvrf.GVRRenderData;
import org.gearvrf.GVRScene;
import org.gearvrf.GVRSceneObject;
import org.gearvrf.GVRMain;
import org.gearvrf.GVRTexture;
import org.gearvrf.scene_objects.GVRCameraSceneObject;
import org.gearvrf.scene_objects.GVRConeSceneObject;
import org.gearvrf.scene_objects.GVRCubeSceneObject;
import org.gearvrf.scene_objects.GVRCylinderSceneObject;
import org.gearvrf.scene_objects.GVRSphereSceneObject;
import org.gearvrf.scene_objects.GVRTextViewSceneObject;
import org.gearvrf.scene_objects.GVRVideoSceneObject;
import org.gearvrf.scene_objects.GVRVideoSceneObject.GVRVideoType;
import org.gearvrf.scene_objects.GVRViewSceneObject;
import org.gearvrf.scene_objects.view.GVRWebView;

import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import static org.gearvrf.GVRRenderData.GVRRenderingOrder.BACKGROUND;
import static org.gearvrf.GVRRenderData.GVRRenderingOrder.OVERLAY;
import static org.gearvrf.GVRRenderData.GVRRenderingOrder.TRANSPARENT;

public class SampleMain extends GVRMain {
    private static final String TAG = SampleMain.class.getSimpleName();
    private List<GVRSceneObject> objectList = new ArrayList<GVRSceneObject>();
    private GVRTextViewSceneObject textViewSceneObject;

    private int currentObject = 0;
    private SceneObjectActivity mActivity;

    SampleMain(SceneObjectActivity activity) {
        mActivity = activity;
    }

    public void setTextObject(String text){
        textViewSceneObject.setText(text);
        textViewSceneObject.detachCollider();
    }
    @Override
    public void onInit(GVRContext gvrContext) throws IOException {

        GVRScene scene = gvrContext.getMainScene();

        // load texture asynchronously
        GVRTexture futureTexture = gvrContext
                .getAssetLoader().loadTexture(new GVRAndroidResource(gvrContext,
                        R.drawable.gearvr_logo));
        GVRTexture futureTextureTop = gvrContext
                .getAssetLoader().loadTexture(new GVRAndroidResource(gvrContext,
                        R.drawable.top));
        GVRTexture futureTextureBottom = gvrContext
                .getAssetLoader().loadTexture(new GVRAndroidResource(gvrContext,
                        R.drawable.bottom));
        ArrayList<GVRTexture> futureTextureList = new ArrayList<GVRTexture>(
                3);
        futureTextureList.add(futureTextureTop);
        futureTextureList.add(futureTexture);
        futureTextureList.add(futureTextureBottom);

        // setup material
        GVRMaterial material = new GVRMaterial(gvrContext);
        material.setMainTexture(futureTexture);

        // create a scene object (this constructor creates a rectangular scene
        // object that uses the standard 'unlit' shader)
        GVRSceneObject quadObject = new GVRSceneObject(gvrContext, 4.0f, 2.0f);
        GVRCubeSceneObject cubeObject = new GVRCubeSceneObject(gvrContext,
                true, material);
        GVRSphereSceneObject sphereObject = new GVRSphereSceneObject(
                gvrContext, true, material);
        GVRCylinderSceneObject cylinderObject = new GVRCylinderSceneObject(
                gvrContext, true, material);
        GVRConeSceneObject coneObject = new GVRConeSceneObject(gvrContext,
                true, material);
        GVRViewSceneObject webViewObject = createWebViewObject(gvrContext);
        GVRCameraSceneObject cameraObject = null;
        try {
            cameraObject = new GVRCameraSceneObject(gvrContext, 3.6f, 2.0f);
            cameraObject.setUpCameraForVrMode(1); // set up 60 fps camera preview.
        } catch (GVRCameraSceneObject.GVRCameraAccessException e) {
            // Cannot open camera
            Log.e(TAG, "Cannot open the camera",e);
        }

        GVRVideoSceneObject videoObject = createVideoObject(gvrContext);
        textViewSceneObject = new GVRTextViewSceneObject(gvrContext, "Preparing the recognizer...");
        textViewSceneObject.setGravity(Gravity.CENTER);
        textViewSceneObject.setTextSize(12);
        GVRRenderData mRend = new GVRRenderData(gvrContext);
        textViewSceneObject.attachRenderData(mRend.setRenderingOrder(OVERLAY));
        cameraObject.attachRenderData(mRend.setRenderingOrder(BACKGROUND));
//        objectList.add(quadObject);
//        objectList.add(cubeObject);
//        objectList.add(sphereObject);
//        objectList.add(cylinderObject);
//        objectList.add(coneObject);
//        objectList.add(webViewObject);
        if(cameraObject != null) {
            objectList.add(cameraObject);
        }
//        objectList.add(videoObject);
//        objectList.add(textViewSceneObject);

        // turn all objects off, except the first one
        int listSize = objectList.size();
        for (int i = 1; i < listSize; i++) {
            objectList.get(i).getRenderData().setRenderMask(0);
        }

        quadObject.getRenderData().setMaterial(material);

        // set the scene object positions
//        quadObject.getTransform().setPosition(0.0f, 0.0f, -3.0f);
//        cubeObject.getTransform().setPosition(0.0f, -1.0f, -3.0f);
//        cylinderObject.getTransform().setPosition(0.0f, 0.0f, -3.0f);
//        coneObject.getTransform().setPosition(0.0f, 0.0f, -3.0f);
//        sphereObject.getTransform().setPosition(0.0f, -1.0f, -3.0f);
        cameraObject.getTransform().setPosition(0.0f, 0.0f, -4.0f);
//        videoObject.getTransform().setPosition(0.0f, 0.0f, -4.0f);
        textViewSceneObject.setTextSize(3);
        textViewSceneObject.detachCollider();
        //textViewSceneObject.getTransform().setScale(12,12,12);
        textViewSceneObject.getTransform().setPosition(0.0f, -0.66f, -2.0f);
        Drawable tDrawable = new ColorDrawable(Color.TRANSPARENT);
        textViewSceneObject.setBackGround(tDrawable);

        // add the scene objects to the scene graph.
        // deal differently with camera scene object: we want it to move
        // with the camera.
        for (GVRSceneObject object : objectList) {
            if (object instanceof GVRCameraSceneObject) {
                scene.getMainCameraRig().addChildObject(object);
            } else {
                scene.addSceneObject(object);
            }
        }
        scene.getMainCameraRig().addChildObject(textViewSceneObject);
    }

    private GVRVideoSceneObject createVideoObject(GVRContext gvrContext) throws IOException {
        final AssetFileDescriptor afd = gvrContext.getActivity().getAssets().openFd("tron.mp4");
        final MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        mediaPlayer.prepare();
        GVRVideoSceneObject video = new GVRVideoSceneObject(gvrContext, 8.0f,
                4.0f, mediaPlayer, GVRVideoType.MONO);
        video.setName("video");
        return video;
    }

    private GVRViewSceneObject createWebViewObject(GVRContext gvrContext) {
        GVRWebView webView = mActivity.getWebView();
        GVRViewSceneObject webObject = new GVRViewSceneObject(gvrContext,
                webView, 8.0f, 4.0f);
        webObject.setName("web view object");
        webObject.getRenderData().getMaterial().setOpacity(1.0f);
        webObject.getTransform().setPosition(0.0f, 0.0f, -4.0f);

        return webObject;
    }

    public void onPause() {
        if (objectList.isEmpty()) {
            return;
        }

        GVRSceneObject object = objectList.get(currentObject);
        if (object instanceof GVRVideoSceneObject) {
            GVRVideoSceneObject video = (GVRVideoSceneObject) object;
            video.getMediaPlayer().pause();
        }
    }

    public void onTap() {

        GVRSceneObject object = objectList.get(currentObject);
        object.getRenderData().setRenderMask(0);
        if (object instanceof GVRVideoSceneObject) {
            GVRVideoSceneObject video = (GVRVideoSceneObject) object;
            video.getMediaPlayer().pause();
        }

        currentObject++;
        int totalObjects = objectList.size();
        if (currentObject >= totalObjects) {
            currentObject = 0;
        }

        object = objectList.get(currentObject);
        if (object instanceof GVRVideoSceneObject) {
            GVRVideoSceneObject video = (GVRVideoSceneObject) object;
            video.getMediaPlayer().start();
        }

        object.getRenderData().setRenderMask(
                GVRRenderData.GVRRenderMaskBit.Left
                        | GVRRenderData.GVRRenderMaskBit.Right);

    }
}

/*
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import org.gearvrf.GVRActivity;
import org.gearvrf.GVRContext;
import org.gearvrf.GVRMain;
import org.gearvrf.GVRScene;
import org.gearvrf.GVRSceneObject;
import org.gearvrf.scene_objects.GVRCameraSceneObject;
import org.gearvrf.utility.Log;

import java.util.ArrayList;
import java.util.List;

//public class MainActivity extends GVRActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//
//        setMain(new Main());
//    }

    public class MainActivity extends GVRMain {
        private List<GVRSceneObject> objectList = new ArrayList<GVRSceneObject>();

        private int currentObject = 0;
        private GVRActivity mActivity;

        MainActivity(SceneObjectActivity activity) {
            mActivity = activity;
        }

        @Override
        public void onInit(GVRContext gvrContext) throws Throwable {

            //Load texture
            //GVRTexture texture = gvrContext.getAssetLoader().loadTexture(new GVRAndroidResource(gvrContext, R.drawable.__default_splash_screen__));


            GVRScene scene = gvrContext.getMainScene();
            //Create a rectangle with the camera view
            GVRCameraSceneObject cameraObject = null;
            // Check if user has given permission to record audio
            final int MY_CAMERA_REQUEST_CODE = 100;
            final int PERMISSIONS_REQUEST_CAMERA = 1;

            if (mActivity.checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                mActivity.requestPermissions(new String[]{Manifest.permission.CAMERA},
                        MY_CAMERA_REQUEST_CODE);
            }
            // Check if user has given permission to record audio
            int permissionCheck = ContextCompat.checkSelfPermission(mActivity.getApplicationContext(), Manifest.permission.CAMERA);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(gvrContext.getActivity(), new String[]{Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_CAMERA);
                //GvrPermissionsRequester.RequestPermissions
                return;
            }
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
                System.out.println("PROBLEMS !!!!!!!!!!!!!!!!!!!");
                //Log.e(mActivity.TAG, "Cannot open the camera",e);
            }
            if(cameraObject != null) {
                objectList.add(cameraObject);
            }
            cameraObject.getTransform().setPosition(0, 0, -3);

            //Add rectangle to the scene
            gvrContext.getMainScene().addSceneObject(cameraObject);
            for (GVRSceneObject object : objectList) {
                if (object instanceof GVRCameraSceneObject) {
                    scene.getMainCameraRig().addChildObject(object);
                } else {
                    scene.addSceneObject(object);
                }
            }
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
//}
*/
