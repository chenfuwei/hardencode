package com.hardencode.test.filter.impl;

import android.content.Context;
import android.opengl.GLES20;

public class KuwaharaImageFilter extends BaseImageFilter{
    private int aRadius;

    private int radius = 3;
    public KuwaharaImageFilter(Context mContext) {
        super(mContext, "default_vertex_shader.sh", "kuwahara_frag_shader.sh");
    }

    @Override
    protected void onInit() {
        super.onInit();
        aRadius = GLES20.glGetUniformLocation(mProgram, "radius");
    }

    @Override
    protected void onDrawFramePre() {
        super.onDrawFramePre();
        GLES20.glUniform1i(aRadius, radius);
    }

    @Override
    public void setProgressValue(float percent) {
        super.setProgressValue(percent);

        radius = (int)(9 * percent + 1);
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                GLES20.glUniform1i(aRadius, radius);
            }
        });
    }
}
