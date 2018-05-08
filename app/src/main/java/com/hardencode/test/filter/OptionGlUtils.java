package com.hardencode.test.filter;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class OptionGlUtils {

    public static float[] vertexs = {
            -1.0f , -1.0f, 0.0f,
            1.0f, -1.0f, 0.0f,
            -1.0f, 1.0f, 0.0f,
            1.0f, 1.0f, 0.0f
    };

    public static float[] coordRotation0 = {
            0.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f
    };

    public static float[] coordRotation90 = new float[]{
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            1.0f, 1.0f
    };

    public static float[] coordRotation270 = new float[]{
            1.1f, 1.1f,
            1.0f, 0.0f,
            0.0f, 0.0f,
            0.0f, 1.0f
    };

    public static ByteBuffer getDatasByteBuffer(float[] vertexs)
    {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertexs.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
        floatBuffer.put(vertexs);
        byteBuffer.position(0);
        return byteBuffer;
    }

    public static int loadBitamp(Bitmap bitmap)
    {
        int [] tex = new int[1];
        GLES20.glGenTextures(1, tex, 0);
        int texId = tex[0];
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        return texId;
    }
}
