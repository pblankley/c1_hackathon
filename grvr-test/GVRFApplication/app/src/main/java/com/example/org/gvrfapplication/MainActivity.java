/* Copyright 2015 Samsung Electronics Co., LTD
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.org.gvrfapplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.gearvrf.GVRContext;
import org.gearvrf.GVRScene;
import org.gearvrf.GVRSceneObject;
import org.gearvrf.GVRMain;
import org.gearvrf.scene_objects.GVRCameraSceneObject;
import org.gearvrf.scene_objects.GVRTextViewSceneObject;
import org.gearvrf.scene_objects.GVRVideoSceneObject;

import android.util.Log;
import android.view.Gravity;

public class MainActivity extends GVRMain {
    private static final String TAG = MainActivity.class.getSimpleName();
    private List<GVRSceneObject> objectList = new ArrayList<GVRSceneObject>();

    private int currentObject = 0;
    private SceneObjectActivity mActivity;

    MainActivity(SceneObjectActivity activity) {
        mActivity = activity;
    }

    @Override
    public void onInit(GVRContext gvrContext) throws IOException {

        GVRScene scene = gvrContext.getMainScene();

        Log.i("Message:","in method onInit");
        // create a scene object (this constructor creates a rectangular scene
        // object that uses the standard 'unlit' shader)
        GVRCameraSceneObject cameraObject = null;
        try {
            cameraObject = new GVRCameraSceneObject(gvrContext, 3.6f, 2.0f);
            cameraObject.setUpCameraForVrMode(1); // set up 60 fps camera preview.
        } catch (GVRCameraSceneObject.GVRCameraAccessException e) {
            // Cannot open camera
            Log.e(TAG, "Cannot open the camera",e);
        }

        GVRTextViewSceneObject textViewSceneObject = new GVRTextViewSceneObject(gvrContext, "Hello World!");
        textViewSceneObject.setGravity(Gravity.CENTER);
        textViewSceneObject.setTextSize(12);
        if(cameraObject != null) {
            objectList.add(cameraObject);
        };
        objectList.add(textViewSceneObject);

        // turn all objects off, except the first one
        int listSize = objectList.size();
        for (int i = 1; i < listSize; i++) {
            objectList.get(i).getRenderData().setRenderMask(0);
        }


        // set the scene object positions
        cameraObject.getTransform().setPosition(0.0f, 0.0f, -4.0f);
        textViewSceneObject.getTransform().setPosition(0.0f, 0.0f, -2.0f);

        // add the scene objects to the scene graph.
        // deal differently with camera scene object: we want it to move
        // with the camera.
        for (GVRSceneObject object : objectList) {
                scene.getMainCameraRig().addChildObject(object);
        }
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
    }
}
