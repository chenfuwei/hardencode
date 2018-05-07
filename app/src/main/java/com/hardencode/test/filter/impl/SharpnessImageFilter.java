package com.hardencode.test.filter.impl;

import android.content.Context;
import android.opengl.GLES20;

public class SharpnessImageFilter extends BaseImageFilter{
    private int aWidthFactor;
    private int aHeightFactor;
    private int aSharpness;

    private float widthFactor;
    private float heightFactor;
    private float sharpness = 1.0f;

    public SharpnessImageFilter(Context mContext) {
        super(mContext, "sharpness_vertex_shader.sh", "sharpness_frag_shader.sh");
    }

    @Override
    protected void onInit() {
        super.onInit();

        aWidthFactor = GLES20.glGetUniformLocation(mProgram, "widthFactor");
        aHeightFactor = GLES20.glGetAttribLocation(mProgram, "heightFactor");
        aSharpness = GLES20.glGetUniformLocation(mProgram, "sharpness");
    }

    @Override
    protected void onDrawFramePre() {
        super.onDrawFramePre();
        GLES20.glUniform1f(aSharpness, sharpness);
    }

    @Override
    public void onOutputSizeChange(int nWidth, int nHeight) {
        super.onOutputSizeChange(nWidth, nHeight);
        widthFactor = 1.0f / nWidth;
        heightFactor = 1.0f / nHeight;
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                GLES20.glUniform1f(aWidthFactor, widthFactor);
                GLES20.glUniform1f(aHeightFactor, heightFactor);
            }
        });
    }

    @Override
    public void setProgressValue(float percent) {
        super.setProgressValue(percent);
        sharpness = (float)(8.0 * percent - 4.0);
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                GLES20.glUniform1f(aSharpness, sharpness);
            }
        });
    }
}
