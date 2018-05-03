package com.hardencode.test.filter.impl.colormatrix;

import android.content.Context;

public class SepiaImageFilter extends ColorMatrixImageFilter{
    public SepiaImageFilter(Context mContext) {
        super(mContext, new float[]{
                0.3588f, 0.7044f, 0.1368f, 0.0f,
                0.2990f, 0.5870f, 0.1140f, 0.0f,
                0.2392f, 0.4696f, 0.0912f, 0.0f,
                0f, 0f, 0f, 1.0f
        });
    }
}
