package com.hardencode.test.filter.impl;

import android.content.Context;
import android.opengl.GLES20;

public class ContrastImageFilter extends BaseImageFilter{
    private int aContrast;
    private float nContrast;
    public ContrastImageFilter(Context mContext) {
        super(mContext,"default_vertex_shader.sh", "contrast_frag_shader.sh");
    }

    @Override
    protected void onInit() {
        super.onInit();
        aContrast = GLES20.glGetUniformLocation(mProgram, "aContrast");
    }

    @Override
    protected void onDrawFramePre() {
        super.onDrawFramePre();
    }

    @Override
    public void setProgressValue(float percent) {
        nContrast = percent * 4;
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                GLES20.glUniform1f(aContrast, nContrast);
            }
        });
    }
}
