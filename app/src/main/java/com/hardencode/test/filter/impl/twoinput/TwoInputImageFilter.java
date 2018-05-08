package com.hardencode.test.filter.impl.twoinput;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;

import com.hardencode.test.R;
import com.hardencode.test.filter.OptionGlUtils;
import com.hardencode.test.filter.impl.BaseImageFilter;

import java.nio.ByteBuffer;

public class TwoInputImageFilter extends BaseImageFilter{
    private int aCoordinate2;
    private int aInputImageTexture2;

    private Bitmap bitmap;
    private int nActive2Tex;

    public TwoInputImageFilter(Context mContext) {
        this(mContext, "default_twoinput_vertex_shader.sh", "default_fragment_shader.sh");
    }

    public TwoInputImageFilter(Context mContext, String vertexShader, String fragShader) {
        super(mContext, vertexShader, fragShader);
        setCoordinate2();

        bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);
        setActive2TexImg(bitmap);
    }

    @Override
    protected void onInit() {
        super.onInit();
        aCoordinate2 = GLES20.glGetAttribLocation(mProgram, "aCoordinate2");
        aInputImageTexture2 = GLES20.glGetUniformLocation(mProgram, "aInputImageTexture2");
    }

    @Override
    protected void onDrawFramePre() {
        super.onDrawFramePre();
        if(nActive2Tex > 0)
        {
            GLES20.glEnableVertexAttribArray(aCoordinate2);
            GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, nActive2Tex);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            GLES20.glUniform1i(aInputImageTexture2, 1);
        }
    }

    @Override
    public void setProgressValue(float percent) {
        super.setProgressValue(percent);
    }

    public void setActive2TexImg(final Bitmap bitmap)
    {
        if(null != this.bitmap && bitmap != this.bitmap)
        {
            this.bitmap.recycle();
        }
        this.bitmap = bitmap;

        runOnDraw(new Runnable() {
            @Override
            public void run() {
                nActive2Tex = OptionGlUtils.loadBitamp(bitmap);
            }
        });
    }

    private void setCoordinate2()
    {
        ByteBuffer byteBuffer = OptionGlUtils.getDatasByteBuffer(OptionGlUtils.coordRotation270);
        GLES20.glVertexAttribPointer(aCoordinate2, 2, GLES20.GL_FLOAT, false, 2 * 4, byteBuffer);
    }
}
