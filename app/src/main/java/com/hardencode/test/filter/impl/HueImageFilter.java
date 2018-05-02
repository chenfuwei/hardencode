package com.hardencode.test.filter.impl;

import android.content.Context;
import android.opengl.GLES20;

public class HueImageFilter extends BaseImageFilter{
    private int aHueAdjust;

    private float nHueAdjust = ((0.25f * 360) % 360) * (float) Math.PI / 180.0f;
    public HueImageFilter(Context mContext) {
        super(mContext,"default_vertex_shader.sh", "hue_frag_shader.sh");
    }

    @Override
    protected void onInit() {
        super.onInit();
        aHueAdjust = GLES20.glGetUniformLocation(mProgram, "aHueAdjust");
    }

    @Override
    protected void onDrawFramePre() {
        super.onDrawFramePre();
        GLES20.glUniform1f(aHueAdjust, nHueAdjust);
    }

    @Override
    public void setProgressValue(float percent) {
        nHueAdjust =((percent * 360) % 360) * (float) Math.PI / 180.0f;
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                GLES20.glUniform1f(aHueAdjust, nHueAdjust);
            }
        });
    }
}
