package com.hardencode.test.filter.impl;

import android.content.Context;
import android.opengl.GLES20;


public class BulgeDistortionImageFilter extends BaseImageFilter{
    private int aAspectRatioPosition;
    private int aScalePosition;
    private int aRadiusPosition;
    private int aCenterPosition;

    private float aspectRation = 0.5f;  //用于扩大变形区域， >= 1.0时， 半径内缩放，< 1.0时，半径外缩放
    private float scale = 0.5f;
    private float radius = 0.5f;
    private float[] center = {0.5f, 0.5f};
    public BulgeDistortionImageFilter(Context mContext) {
        super(mContext, "default_vertex_shader.sh", "bulge_distortion_frag_shader.sh");
    }

    @Override
    public void onOutputSizeChange(int nWidth, int nHeight) {
        super.onOutputSizeChange(nWidth, nHeight);
        aspectRation = (float) nHeight/nWidth ;
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                GLES20.glUniform1f(aAspectRatioPosition, aspectRation);
            }
        });
    }

    @Override
    protected void onInit() {
        super.onInit();

        aAspectRatioPosition = GLES20.glGetUniformLocation(mProgram, "aspectRatio");
        aScalePosition = GLES20.glGetUniformLocation(mProgram, "scale");
        aRadiusPosition = GLES20.glGetUniformLocation(mProgram, "radius");
        aCenterPosition = GLES20.glGetUniformLocation(mProgram, "center");
    }

    @Override
    protected void onDrawFramePre() {
        super.onDrawFramePre();

        GLES20.glUniform1f(aRadiusPosition, radius);
        GLES20.glUniform1f(aScalePosition, scale);
        GLES20.glUniform2fv(aCenterPosition, 1, center, 0);
    }

    @Override
    public void setProgressValue(float percent) {
        super.setProgressValue(percent);
        scale = percent;

        runOnDraw(new Runnable() {
            @Override
            public void run() {
                GLES20.glUniform1f(aScalePosition, scale);
            }
        });
    }
}
