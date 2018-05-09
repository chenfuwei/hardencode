package com.hardencode.test.filter.impl.group;

import android.content.Context;

import com.hardencode.test.filter.impl.group.TwoPassTextureSamplingImageFilter;

public class GaussianBlurImageFilter extends TwoPassTextureSamplingImageFilter{
    float blurSize = 1.0f;
    public GaussianBlurImageFilter(Context mContext) {
        super(mContext, "gaussian_blur_vertex_shader.sh", "gaussian_blur_frag_shader.sh", "gaussian_blur_vertex_shader.sh", "gaussian_blur_frag_shader.sh");
    }


    @Override
    public float getVerticalTexelOffsetRatio() {
        return blurSize;
    }

    @Override
    public float getHorizontalTexelOffsetRatio() {
        return blurSize;
    }

    @Override
    public void setProgressValue(float percent) {
        super.setProgressValue(percent);

        blurSize = 10 * percent + 1;
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                initTexelOffsets();
            }
        });
    }
}
