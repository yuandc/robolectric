package com.xtremelabs.robolectric.shadows;

import android.hardware.Camera;
import android.view.SurfaceHolder;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.internal.Implementation;
import com.xtremelabs.robolectric.internal.Implements;
import com.xtremelabs.robolectric.internal.RealObject;

/**
 * Shadows the Android {@code Camera} class.
 */
@Implements(Camera.class)
public class ShadowCamera {

    private boolean locked = true;
    private boolean previewing = false;
    private boolean released = false;
    private Camera.Parameters parameters;
    private Camera.PreviewCallback previewCallback;
    private SurfaceHolder surfaceHolder;

    @RealObject
    private Camera realCamera;

    @Implementation
    public static Camera open() {
        return Robolectric.newInstanceOf(Camera.class);
    }

    @Implementation
    public void unlock() {
        locked = false;
    }

    @Implementation
    public void reconnect() {
        locked = true;
    }

    @Implementation
    public Camera.Parameters getParameters() {
        if (null == parameters) {
            parameters = Robolectric.newInstanceOf(Camera.Parameters.class);
        }
        return parameters;
    }

    @Implementation
    public void setParameters(Camera.Parameters params) {
        parameters = params;
    }

    @Implementation
    public void setPreviewDisplay(SurfaceHolder holder) {
        surfaceHolder = holder;
    }

    @Implementation
    public void startPreview() {
        previewing = true;
    }

    @Implementation
    public void stopPreview() {
        previewing = false;
    }

    @Implementation
    public void release() {
        released = true;
    }

    @Implementation
    public void setPreviewCallback(Camera.PreviewCallback cb) {
        previewCallback = cb;
    }

    @Implementation
    public void setOneShotPreviewCallback(Camera.PreviewCallback cb) {
        previewCallback = cb;
    }

    @Implementation
    public void setPreviewCallbackWithBuffer(Camera.PreviewCallback cb) {
        previewCallback = cb;
    }

    /**
     * Allows test cases to invoke the preview callback, to simulate a frame of camera data.
     *
     * @param data byte buffer of simulated camera data
     */
    public void invokePreviewCallback(byte[] data) {
        if (previewCallback != null) {
            previewCallback.onPreviewFrame(data, realCamera);
        }
    }

    public boolean isLocked() {
        return locked;
    }

    public boolean isPreviewing() {
        return previewing;
    }

    public boolean isReleased() {
        return released;
    }

    public SurfaceHolder getPreviewDisplay() {
        return surfaceHolder;
    }
}
