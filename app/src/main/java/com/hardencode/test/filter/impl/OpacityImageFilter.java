package com.hardencode.test.filter.impl;

import android.content.Context;
import android.opengl.GLES20;

public class OpacityImageFilter extends  BaseImageFilter{
    private int aOpacity;
    private float opacity = 0.5f;
    public OpacityImageFilter(Context mContext) {
        super(mContext, "default_vertex_shader.sh", "opacity_frag_shader.sh");
    }

    @Override
    protected void onInit() {
        super.onInit();
        aOpacity = GLES20.glGetUniformLocation(mProgram, "opacity");
    }

    @Override
    protected void onDrawFramePre() {
        super.onDrawFramePre();
    }

    @Override
    public void setProgressValue(float percent) {
        super.setProgressValue(percent);
        opacity = percent;
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                GLES20.glUniform1f(aOpacity, opacity);
            }
        });
    }
}
