package com.hardencode.test.filter.impl;

import android.content.Context;
import android.opengl.GLES20;

public class WhiteBalanceImageFilter extends BaseImageFilter{
    private int aTemperaturePosition;
    private int aTinyPosition;

    private float nTiny = 0.5f;
    private int nTemperature = 5000;

    public WhiteBalanceImageFilter(Context mContext) {
        super(mContext, "default_vertex_shader.sh", "white_balance_frag_shader.sh");
    }

    public WhiteBalanceImageFilter(Context mContext, String vertexShader, String fragShader) {
        super(mContext, vertexShader, fragShader);
    }

    @Override
    protected void onInit() {
        super.onInit();
        aTemperaturePosition = GLES20.glGetUniformLocation(mProgram, "temperature");
        aTinyPosition = GLES20.glGetUniformLocation(mProgram, "tiny");
    }

    public void setTemperature(int aTemperature) {
        this.nTemperature = aTemperature;
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                GLES20.glUniform1f(aTemperaturePosition, nTemperature < 5000 ? (float)(0.0004 * (nTemperature-5000.0)) : (float)(0.00006 * (nTemperature-5000.0)));

            }
        });

    }

    @Override
    protected void onDrawFramePre() {
        super.onDrawFramePre();
        setTemperature(nTemperature);
        GLES20.glUniform1f(aTinyPosition, nTiny);
    }

    @Override
    public void setProgressValue(float percent) {
        super.setProgressValue(percent);

        setTemperature((int)(percent * 10000));
    }
}
