package com.hardencode.test.filter.impl.twoinput.blend;

import android.content.Context;
import android.opengl.GLES20;

import com.hardencode.test.filter.impl.twoinput.TwoInputImageFilter;

public class MixBlendImageFilter extends TwoInputImageFilter{
    private int aMixPercent;

    private float mixPercent = 0.5f;
    public MixBlendImageFilter(Context mContext) {
        super(mContext);
    }

    public MixBlendImageFilter(Context mContext, String vertexShader, String fragShader) {
        super(mContext, vertexShader, fragShader);
    }

    @Override
    protected void onInit() {
        super.onInit();
        aMixPercent = GLES20.glGetUniformLocation(mProgram, "mixpercent");
    }

    @Override
    protected void onDrawFramePre() {
        super.onDrawFramePre();
        GLES20.glUniform1f(aMixPercent, mixPercent);
    }

    @Override
    public void setProgressValue(float percent) {
        super.setProgressValue(percent);
        mixPercent = percent;
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                GLES20.glUniform1f(aMixPercent, mixPercent);
            }
        });
    }
}
