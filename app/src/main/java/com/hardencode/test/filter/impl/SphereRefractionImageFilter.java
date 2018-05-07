package com.hardencode.test.filter.impl;

import android.content.Context;
import android.opengl.GLES20;

public class SphereRefractionImageFilter extends BaseImageFilter{
    private int aCenterPosition;
    private int aRadiusPosition;
    private int aAspectRatioPosition;
    private int aRefractiveIndex;

    private float[] center = {0.5f, 0.5f};
    private float radius = 0.25f;
    private float refractiveIndex = 0.7f;
    private float aspectRatio = 1.0f;

    public SphereRefractionImageFilter(Context mContext) {
        super(mContext, "default_vertex_shader.sh", "sphere_refraction_frag_shader.sh");
    }

    @Override
    protected void onInit() {
        super.onInit();

        aCenterPosition = GLES20.glGetUniformLocation(mProgram, "center");
        aAspectRatioPosition = GLES20.glGetUniformLocation(mProgram, "aspectRatio");
        aRadiusPosition = GLES20.glGetUniformLocation(mProgram, "radius");
        aRefractiveIndex = GLES20.glGetAttribLocation(mProgram, "refractiveIndex");
    }

    @Override
    protected void onDrawFramePre() {
        super.onDrawFramePre();
        GLES20.glUniform2fv(aCenterPosition, 1, center, 0);
        GLES20.glUniform1f(aRadiusPosition, radius);
        GLES20.glUniform1f(aAspectRatioPosition, aspectRatio);
        GLES20.glUniform1f(aRefractiveIndex, refractiveIndex);
    }

    @Override
    public void setProgressValue(float percent) {
        super.setProgressValue(percent);
    }
}
