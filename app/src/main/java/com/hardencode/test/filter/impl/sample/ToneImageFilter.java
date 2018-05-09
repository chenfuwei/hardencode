package com.hardencode.test.filter.impl.sample;

import android.content.Context;
import android.opengl.GLES20;

public class ToneImageFilter extends Image3x3TextureSampingFilter{
    float mThreshold = 0.2f;
    int mThresholdLocation;
    float mQuantizationLevels = 10.0f;
    int mQuantizationLevelsLocation;

    public ToneImageFilter(Context mContext) {
        super(mContext, "image_3x3_vertex_shader.sh", "tone_frag_shader.sh");
    }

    @Override
    protected void onInit() {
        super.onInit();
        mThresholdLocation = GLES20.glGetUniformLocation(mProgram, "threshold");
        mQuantizationLevelsLocation = GLES20.glGetUniformLocation(mProgram, "quantizationLevels");
        mThreshold = 0.2f;
        mQuantizationLevels = 10.0f;
        setThreshold(mThreshold);
        setQuantizationLevels(mQuantizationLevels);
    }

    public void setThreshold(final float mThreshold) {
        this.mThreshold = mThreshold;
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                GLES20.glUniform1f(mThresholdLocation, mThreshold);
            }
        });
    }

    public void setQuantizationLevels(final float mQuantizationLevels) {
        this.mQuantizationLevels = mQuantizationLevels;
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                GLES20.glUniform1f(mQuantizationLevelsLocation, mQuantizationLevels);
            }
        });
    }

    @Override
    public void setProgressValue(float percent) {
        super.setProgressValue(percent);

        setThreshold(percent);
    }
}
