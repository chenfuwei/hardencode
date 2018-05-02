package com.hardencode.test.filter.impl;

import android.content.Context;
import android.opengl.GLES20;

//nBrightness 的值从零到10
public class BrightnessImageFilter extends BaseImageFilter {
    private int aBrightnessPosition;
    private float nBrightness = 1.0f;
    public BrightnessImageFilter(Context mContext) {
        super(mContext, "default_vertex_shader.sh", "brightness_frag_shader.sh");
    }

    @Override
    protected void onInit() {
        super.onInit();
        aBrightnessPosition = GLES20.glGetUniformLocation(mProgram, "aBrightness");
    }

    @Override
    protected void onDrawFramePre() {
        super.onDrawFramePre();
        GLES20.glUniform1f(aBrightnessPosition, nBrightness);
    }

    @Override
    public void setProgressValue(float percent) {
        nBrightness = percent * 2;
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                GLES20.glUniform1f(aBrightnessPosition, nBrightness);
            }
        });
    }
}
