package com.hardencode.test.filter.impl.group;

import android.content.Context;
import android.opengl.GLES20;

import com.hardencode.test.filter.impl.BaseImageFilter;

public class TwoPassTextureSamplingImageFilter extends TwoPassImageFilter{
    public TwoPassTextureSamplingImageFilter(Context mContext, String firstVertexShader, String firstFragShader, String secondVertexShader, String secondFragShader) {
        super(mContext, firstVertexShader, firstFragShader, secondVertexShader, secondFragShader);
    }

    protected void initTexelOffsets() {
        final float ratio = getHorizontalTexelOffsetRatio();
        BaseImageFilter filter = filters.get(0);
        final int texelWidthOffsetLocation = GLES20.glGetUniformLocation(filter.getProgram(), "texelWidthOffset");
        final  int texelHeightOffsetLocation = GLES20.glGetUniformLocation(filter.getProgram(), "texelHeightOffset");
        filter.runOnDraw(new Runnable() {
            @Override
            public void run() {
                GLES20.glUniform1f(texelWidthOffsetLocation, ratio / nOutputWidth);
                GLES20.glUniform1f(texelHeightOffsetLocation, 0);
            }
        });

        final float ratio1 = getVerticalTexelOffsetRatio();
        filter = filters.get(1);
        final int texelWidthOffsetLocation1 = GLES20.glGetUniformLocation(filter.getProgram(), "texelWidthOffset");
        final int texelHeightOffsetLocation1 = GLES20.glGetUniformLocation(filter.getProgram(), "texelHeightOffset");
        filter.runOnDraw(new Runnable() {
            @Override
            public void run() {
                GLES20.glUniform1f(texelWidthOffsetLocation1, 0);
                GLES20.glUniform1f(texelHeightOffsetLocation1, ratio1 / nOutputHeight);
            }
        });
    }

    @Override
    public void onOutputSizeChange(int nWidth, int nHeight) {
        super.onOutputSizeChange(nWidth, nHeight);
        initTexelOffsets();
    }

    public float getVerticalTexelOffsetRatio() {
        return 1f;
    }

    public float getHorizontalTexelOffsetRatio() {
        return 1f;
    }
}
