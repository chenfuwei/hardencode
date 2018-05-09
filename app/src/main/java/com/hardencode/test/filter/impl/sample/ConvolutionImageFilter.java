package com.hardencode.test.filter.impl.sample;

import android.content.Context;
import android.opengl.GLES20;

import com.hardencode.test.filter.impl.BaseImageFilter;

public class ConvolutionImageFilter extends Image3x3TextureSampingFilter{
    private int mFactorLocation;

    private float[] mat9;

    public ConvolutionImageFilter(Context mContext) {
        super(mContext, "image_3x3_vertex_shader.sh", "convolution_frag_shader.sh");
    }

    @Override
    protected void onInit() {
        super.onInit();
        mFactorLocation = GLES20.glGetUniformLocation(mProgram, "coordFactor");
        mat9 = new float[]{
                0.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 0.0f
        };
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                GLES20.glUniformMatrix3fv(mFactorLocation, 1, false, mat9, 0);
            }
        });
    }

    public void setMat9(final float[] mat9) {
        this.mat9 = mat9;
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                GLES20.glUniformMatrix3fv(mFactorLocation, 1, false, mat9, 0);
            }
        });
    }
}
