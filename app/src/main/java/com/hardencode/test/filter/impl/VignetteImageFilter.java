package com.hardencode.test.filter.impl;

import android.content.Context;
import android.opengl.GLES20;

public class VignetteImageFilter extends BaseImageFilter{
    private int aColorPosition;
    private int aCenterPosition;
    private int aStartPosition;
    private int aEndPosition;

    private float[] color = {1.0f, 0.0f, 0.0f};
    private float[] center = {0.5f, 0.5f};
    private float start = 0.6f;
    private float end = 0.8f;

    public VignetteImageFilter(Context mContext) {
        super(mContext, "default_vertex_shader.sh", "vignette_frag_shader.sh");
    }

    @Override
    protected void onInit() {
        super.onInit();
        aColorPosition = GLES20.glGetUniformLocation(mProgram, "vignetteColor");
        aCenterPosition = GLES20.glGetUniformLocation(mProgram, "vignetteCenter");
        aStartPosition = GLES20.glGetUniformLocation(mProgram, "vignetteStart");
        aEndPosition = GLES20.glGetUniformLocation(mProgram, "vignetteEnd");
    }

    @Override
    protected void onDrawFramePre() {
        super.onDrawFramePre();
        GLES20.glUniform1f(aStartPosition, start);
        GLES20.glUniform1f(aEndPosition, end);
        GLES20.glUniform3fv(aColorPosition, 1, color, 0);
        GLES20.glUniform2fv(aCenterPosition, 1, center, 0);
    }

    @Override
    public void setProgressValue(float percent) {
        super.setProgressValue(percent);
        center = new float[]{
          percent, percent
        };

        runOnDraw(new Runnable() {
            @Override
            public void run() {
                GLES20.glUniform2fv(aCenterPosition, 1, center, 0);
            }
        });
    }
}
