package com.hardencode.test.filter.impl;

import android.content.Context;
import android.opengl.GLES20;

public class SwirlImageFilter extends BaseImageFilter{
    private int aCenterPosition;
    private int aRadiusPosition;
    private int aAngePosition;

    private float[] nCenter;
    private float nRadius;
    private float nAnge;
    public SwirlImageFilter(Context mContext) {
        super(mContext, "default_vertex_shader.sh", "swirl_frag_shader.sh");
        nCenter = new float[]{0.5f, 0.5f};
        nRadius = 0.5f;
        nAnge = 1.0f;
    }

    @Override
    protected void onInit() {
        super.onInit();
        aCenterPosition = GLES20.glGetUniformLocation(mProgram, "center");
        aRadiusPosition = GLES20.glGetUniformLocation(mProgram, "radius");
        aAngePosition = GLES20.glGetUniformLocation(mProgram, "ange");
    }

    @Override
    protected void onDrawFramePre() {
        super.onDrawFramePre();
        GLES20.glUniform1f(aRadiusPosition, nRadius);
        GLES20.glUniform2fv(aCenterPosition, 1, nCenter, 0);
        GLES20.glUniform1f(aAngePosition, nAnge);
    }

    @Override
    public void setProgressValue(float percent) {
        super.setProgressValue(percent);

        nCenter = new float[]{
          percent, percent
        };

        runOnDraw(new Runnable() {
            @Override
            public void run() {
                GLES20.glUniform2fv(aCenterPosition, 1, nCenter, 0);
            }
        });
    }
}
