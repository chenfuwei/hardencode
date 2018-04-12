package com.hardencode.test.encode.gl;

import android.opengl.EGL14;
import android.util.Log;
import android.view.Surface;

public class CustomGlViewRender extends CustomGLRender{

    private static final String TAG = "CustomGlViewRender";
    private Surface surface;
    int mWidth, mHeight;

    private int mTexId;

    boolean bSurfaceCreate = false;


    synchronized public void onSurfaceCreated(Surface surface, int mWidth, int mHeight)
    {
        Log.i(TAG, "onSurfaceCreated mWidth = " + mWidth + " mHeight = " + mHeight);
        this.surface = surface;
        this.mWidth = mWidth;
        this.mHeight = mHeight;
        bSurfaceCreate = true;
        if(null != mShareEGLContext) {
            Log.i(TAG, "onSurfaceCreated 1");
            super.onSurfaceCreated(surface, mWidth, mHeight);
            bSurfaceUpdate.set(true);
        }else
        {
            Log.i(TAG, "onSurfaceCreated mShareContext is null");
        }
    }

    public void onSurfaceChanged(int width, int height)
    {
        Log.i(TAG, "onSurfaceChanged width = " + width + " height = " + height);
        if(this.mWidth != width || this.mHeight != height)
        {
            this.mWidth = width;
            this.mHeight = height;
            super.onSurfaceChanged(width, height);
        }
    }

    @Override
    public void onSurfaceDestroy() {
        //super.onSurfaceDestroy();
        bSurfaceCreate = false;
       // mShareEGLContext = null;
        //mTexId = 0;
    }

    protected void setRender(GlRender render)
    {
        if(!Thread.currentThread().getName().equals(mThreadOwner))
        {
            Log.e(TAG, "setGlRener: this thread does not own the Opengles Context");
            return;
        }


        glRender.onSurfaceCreated(mEGLConfig);
        glRender.onSurfaceChanged(mWidth, mHeight);
    }

    @Override
    protected void createSurface() {
        mEGLSurface = EGL14.eglCreateWindowSurface(mEGLDisplay, mEGLConfig, surface, null, 0);
    }

    @Override
    protected void processSurfaceChange() {
        glRender.onSurfaceChanged(mWidth, mHeight);
    }


    @Override
    protected void onDrawFrame() {
        glRender.onDrawFrame(mTexId);
    }

    public void setmTexId(int mTexId)
    {
        this.mTexId = mTexId;
    }
}
