package com.hardencode.test.encode.gl;

import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLSurface;
import android.opengl.GLUtils;
import android.util.Log;
import android.view.Surface;

import java.util.concurrent.atomic.AtomicBoolean;

import static android.opengl.EGL14.EGL_ALPHA_SIZE;
import static android.opengl.EGL14.EGL_BLUE_SIZE;
import static android.opengl.EGL14.EGL_DEFAULT_DISPLAY;
import static android.opengl.EGL14.EGL_DEPTH_SIZE;
import static android.opengl.EGL14.EGL_GREEN_SIZE;
import static android.opengl.EGL14.EGL_NONE;
import static android.opengl.EGL14.EGL_RED_SIZE;
import static android.opengl.EGL14.EGL_STENCIL_SIZE;


public class CustomGLRender implements Runnable{
    private static final String TAG = "CustomGLRender";
    protected GlRender glRender;
    EGLDisplay mEGLDisplay;
    EGLConfig[] mEGLConfigs;
    EGLConfig mEGLConfig;
    EGLContext mEGLContext;
    EGLSurface mEGLSurface;
//    EGL14 mGL;
    String mThreadOwner;
    Thread glThread;
    EGLContext mShareEGLContext;

    protected AtomicBoolean bRunning = new AtomicBoolean(false);

    protected Object object;
    protected AtomicBoolean bSurfaceChange = new AtomicBoolean(false);
    protected AtomicBoolean bSurfaceUpdate = new AtomicBoolean(false);

    protected boolean bEglSurfaceSuccess = false;

    public void onSurfaceCreated(Surface surfaceTexture, int mWidth, int mHeight) {
        if(!bRunning.get()) {
            object = new Object();
            bRunning.set(true);
            glThread = new Thread(this, this.getClass().getSimpleName());
            glThread.start();
        }else
        {
            requestRender();
        }
    }

    public void onSurfaceChanged(int width, int height)
    {
        bSurfaceChange.set(true);
        requestRender();
    }

    public void onSurfaceDestroy()
    {
        Log.i(TAG, "onSurfaceDestroy");
        release();
    }

    public void setGlRender(GlRender glRender)
    {
        this.glRender = glRender;
    }

    private EGLConfig chooseConfig(){
        int[] attribList = new int[]{
                EGL_DEPTH_SIZE, 0,
                EGL_STENCIL_SIZE, 0,
                EGL_RED_SIZE, 8,
                EGL_GREEN_SIZE, 8,
                EGL_BLUE_SIZE, 8,
                EGL_ALPHA_SIZE, 8,
                EGL14.EGL_RENDERABLE_TYPE, 4,
                EGL_NONE
        };

        int[] numConfig = new int[1];
        EGL14.eglChooseConfig(mEGLDisplay, attribList, 0, null, 0, 1, numConfig, 0);
        int configSize = numConfig[0];
        mEGLConfigs = new EGLConfig[configSize];
        EGL14.eglChooseConfig(mEGLDisplay, attribList, 0, mEGLConfigs , 0, configSize, numConfig, 0);
        return mEGLConfigs[0];
    }

    private EGLContext createContext(EGLDisplay eglDisplay, EGLConfig eglConfig)
    {
        int EGL_CONTEXT_CLIENT_VERSION = 0x3098;
        int[] attrs = {
                EGL_CONTEXT_CLIENT_VERSION, 2,
                EGL14.EGL_NONE
        };
        Log.i(TAG, mShareEGLContext == null ? "YES" : "NO");
        EGLContext context = null == mShareEGLContext ? EGL14.EGL_NO_CONTEXT : mShareEGLContext;
        return EGL14.eglCreateContext(eglDisplay, eglConfig, context, attrs, 0);
    }

    protected void initEglContext()
    {
        int[] version = new int[2];
        mEGLDisplay = EGL14.eglGetDisplay(EGL_DEFAULT_DISPLAY);
        EGL14.eglInitialize(mEGLDisplay, version, 0, version , 1);
        mEGLConfig = chooseConfig();
        mEGLContext = createContext(mEGLDisplay, mEGLConfig);
    }

    protected void initEglSurface()
    {
        createSurface();

        //设置当前的渲染环境
        try {
            if (mEGLSurface == null || mEGLSurface == EGL14.EGL_NO_SURFACE) {
                throw new RuntimeException("GL error:" + GLUtils.getEGLErrorString(EGL14.eglGetError()));
            }
            if (!EGL14.eglMakeCurrent(mEGLDisplay, mEGLSurface, mEGLSurface, mEGLContext)) {
                throw new RuntimeException("GL Make current Error"+ GLUtils.getEGLErrorString(EGL14.eglGetError()));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        // Record thread owner of OpenGL context
        mThreadOwner = Thread.currentThread().getName();

        bEglSurfaceSuccess = true;
    }

    public void requestRender()
    {
        synchronized (object) {
            object.notifyAll();
        }
    }

    synchronized public void release()
    {
        bRunning.set(false);
        if(null != glThread)
        {
            try{
                requestRender();
                glThread.interrupt();
//                glThread.join();
                glThread = null;
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    protected void destroyEglSurface()
    {
        if(!EGL14.eglMakeCurrent(mEGLDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT))
        {
            Log.w(TAG, "eglMakeCurrent failure");
        }

        if(!EGL14.eglDestroySurface(mEGLDisplay, mEGLSurface))
        {
            Log.w(TAG, "eglDestroySurface failure");
        }
        bEglSurfaceSuccess = false;
    }

    protected void destroy()
    {
        destroyEglSurface();
        if(!EGL14.eglDestroyContext(mEGLDisplay, mEGLContext))
        {
            Log.w(TAG, "eglDestroyContext failure");
        }

        if(!EGL14.eglTerminate(mEGLDisplay))
        {
            Log.w(TAG, "eglTerminate failure");
        }
    }

    protected void setRender(GlRender render)
    {

    }

//    public void updateSurfaceTexture(SurfaceTexture surfaceTexture, int mWidth, int mHeight)
//    {
//        this.mWidth = mWidth;
//        this.mHeight = mHeight;
//        this.surfaceTexture = surfaceTexture;
//        bSurfaceUpdate.set(true);
//        requestRender();
//        // bSurfaceChange.set(true);
//    }

    @Override
    public void run() {
        initEglContext();
        initEglSurface();
        setRender(glRender);

        while (bRunning.get())
        {
            if(bSurfaceUpdate.get())
            {
                bSurfaceUpdate.set(false);
                destroyEglSurface();
                initEglSurface();
                processSurfaceChange();
            }

            if(bSurfaceChange.get())
            {
                bSurfaceChange.set(false);
                processSurfaceChange();
            }

            synchronized (object)
            {
            onDrawFrame();
                EGL14.eglSwapBuffers(mEGLDisplay, mEGLSurface);


                try
                {
                    if(bRunning.get())
                    {
                        object.wait();
                    }
                }catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }

        destroy();
    }

    protected void createSurface()
    {

    }

    protected void processSurfaceChange()
    {

    }

    protected void onDrawFrame()
    {

    }

    public void setShareEGLContext(EGLContext mShareEGLContext) {
        this.mShareEGLContext = mShareEGLContext;
    }
}
