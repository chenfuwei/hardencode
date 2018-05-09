package com.hardencode.test.filter.impl.sample;

import android.content.Context;
import android.opengl.GLES20;

import com.hardencode.test.filter.impl.BaseImageFilter;

public class Image3x3TextureSampingFilter extends BaseImageFilter{
    private int mWidthTexelLocation;
    private int mHeightTexelLocation;

    private float widthTexel;
    private float heightTexel;

    public Image3x3TextureSampingFilter(Context mContext) {
        this(mContext, "image_3x3_vertex_shader.sh", "default_fragment_shader.sh");
    }

    public Image3x3TextureSampingFilter(Context mContext, String vertexShader, String fragShader) {
        super(mContext, vertexShader, fragShader);
    }

    @Override
    protected void onInit() {
        super.onInit();
        mWidthTexelLocation = GLES20.glGetUniformLocation(mProgram, "widthTexel");
        mHeightTexelLocation = GLES20.glGetUniformLocation(mProgram, "heightTexel");
    }

    @Override
    public void onOutputSizeChange(int nWidth, int nHeight) {
        super.onOutputSizeChange(nWidth, nHeight);

        setWidthTexel((float) 1.0 / nWidth);
        setHeightTexel((float)1.0 / nHeight);
    }

    public void setWidthTexel(final float widthTexel) {
        this.widthTexel = widthTexel;
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                GLES20.glUniform1f(mWidthTexelLocation, widthTexel);
            }
        });
    }

    public void setHeightTexel(final float heightTexel) {
        this.heightTexel = heightTexel;
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                GLES20.glUniform1f(mHeightTexelLocation, heightTexel);
            }
        });
    }
}
