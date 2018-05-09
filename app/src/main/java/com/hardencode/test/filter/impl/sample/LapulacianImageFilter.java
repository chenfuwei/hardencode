package com.hardencode.test.filter.impl.sample;

import android.content.Context;
import android.opengl.GLES20;

public class LapulacianImageFilter extends Image3x3TextureSampingFilter{
    private int mFactorLocation;

    private float[] mat9;
    public LapulacianImageFilter(Context mContext) {
        super(mContext, "image_3x3_vertex_shader.sh", "lapulacian_frag_shader.sh");
    }

    @Override
    protected void onInit() {
        super.onInit();
        mFactorLocation = GLES20.glGetUniformLocation(mProgram, "coordFactor");
        mat9 = new float[]{
                0.5f, 1.0f, 0.5f,
                1.0f, -6.0f, 1.0f,
                0.5f, 1.0f, 0.5f
        };
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                GLES20.glUniformMatrix3fv(mFactorLocation, 1, false, mat9, 0);
            }
        });
    }
}
