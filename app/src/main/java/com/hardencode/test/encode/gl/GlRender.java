package com.hardencode.test.encode.gl;


import android.opengl.EGLConfig;

public interface GlRender {
    void onSurfaceCreated(EGLConfig config);
    void onSurfaceChanged(int width, int height);
    void onDrawFrame(int mTexId);
}
