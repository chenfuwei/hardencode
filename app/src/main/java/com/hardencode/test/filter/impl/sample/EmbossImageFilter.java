package com.hardencode.test.filter.impl.sample;

import android.content.Context;

public class EmbossImageFilter extends ConvolutionImageFilter{
    private float intensity = 1.0f;
    public EmbossImageFilter(Context mContext) {
        super(mContext);
    }


    @Override
    public void setProgressValue(float percent) {
        super.setProgressValue(percent);
        intensity = 10 * percent + 1;
        setMat9(new float[]{
                intensity * (-2.0f), -intensity, 0.0f,
                -intensity, 1.0f, intensity,
                0.0f, intensity, intensity * 2.0f,
        });
    }
}
