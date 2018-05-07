package com.hardencode.test.filter.impl;

import android.content.Context;
import android.opengl.GLES20;

public class PosterizeImageFilter extends BaseImageFilter{
    private int aColorLevel;

    private float colorLevel = 17.0f;
    public PosterizeImageFilter(Context mContext) {
        super(mContext, "default_vertex_shader.sh", "posterize_frag_shader.sh");
    }

    @Override
    protected void onInit() {
        super.onInit();
        aColorLevel = GLES20.glGetUniformLocation(mProgram, "colorLevel");
    }

    @Override
    protected void onDrawFramePre() {
        super.onDrawFramePre();
        GLES20.glUniform1f(aColorLevel, colorLevel);
    }

    @Override
    public void setProgressValue(float percent) {
        super.setProgressValue(percent);
        colorLevel = 255 * percent + 1;
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                GLES20.glUniform1f(aColorLevel, colorLevel);
            }
        });
    }
}
