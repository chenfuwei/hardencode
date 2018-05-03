package com.hardencode.test.filter.impl.colormatrix;

import android.content.Context;
import android.opengl.GLES20;

import com.hardencode.test.filter.impl.BaseImageFilter;

public class ColorMatrixImageFilter extends BaseImageFilter {
    private int aDensityPosition;
    private int aColorMatrixPosition;

    private float nDensity = 1.0f;
    private float[] nColorMatrix;
    public ColorMatrixImageFilter(Context mContext) {
        this(mContext, new float[]{
                1.0f, 0.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f, 0.0f,
                0.0f, 0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
        });
    }

    public ColorMatrixImageFilter(Context mContext, float[] colorMatrix) {

        super(mContext, "default_vertex_shader.sh", "color_matrix_frag_shader.sh");
        nColorMatrix = colorMatrix;
    }
    @Override
    protected void onInit() {
        super.onInit();

        aDensityPosition = GLES20.glGetUniformLocation(mProgram, "indensity");
        aColorMatrixPosition = GLES20.glGetUniformLocation(mProgram, "aColorMatrix");
    }

    @Override
    protected void onDrawFramePre() {
        super.onDrawFramePre();
        GLES20.glUniform1f(aDensityPosition, nDensity);
        GLES20.glUniformMatrix4fv(aColorMatrixPosition, 1, false, nColorMatrix, 0);
    }

    @Override
    public void setProgressValue(float percent) {
        super.setProgressValue(percent);
        nDensity = percent;
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                GLES20.glUniform1f(aDensityPosition, nDensity);
            }
        });
    }
}
