package com.hardencode.test.filter.impl;

import android.content.Context;
import android.opengl.GLES20;

public class GammaImageFilter extends BaseImageFilter{
    private int aGamma;
    private float nGamma;
    public GammaImageFilter(Context mContext) {
        super(mContext, "default_vertex_shader.sh", "gamma_frag_shader.sh");
    }

    @Override
    protected void onInit() {
        super.onInit();
        aGamma = GLES20.glGetUniformLocation(mProgram, "aGamma");
    }

    @Override
    protected void onDrawFramePre() {
        super.onDrawFramePre();
        GLES20.glUniform1f(aGamma, nGamma);
    }

    @Override
    public void setProgressValue(float percent) {
        nGamma = percent * 4;
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                GLES20.glUniform1f(aGamma, nGamma);
            }
        });
    }
}
