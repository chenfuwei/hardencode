package com.hardencode.test;

import android.graphics.Bitmap;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.os.Environment;
import android.util.Log;

import com.hardencode.test.filter.BaseFilter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Calendar;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class CameraInputFilter extends BaseFilter{
    private static final String TAG = "GlVideoRender";
    private static final String CAMERA_INPUT_FRAG_FRAGMENT = "" +
            "#extension GL_OES_EGL_image_external : require\n"+
            "precision mediump float;" + "\n" +
            "uniform samplerExternalOES  sTexture;" + "\n" +
            "varying vec2 aVaryingTexCoord;" +"\n" +
            "void main(){" + "\n" +
            "gl_FragColor = texture2D(sTexture, aVaryingTexCoord);" + "\n" +
            "}";

    private int[] frameBuffer = new int[1];
    private int[] textureBuffer = new int[1];

    private int frameWidth;
    private int frameHeight;

    public CameraInputFilter()
    {
        super(VERTEX_DEFAULT_FRAGMENT, CAMERA_INPUT_FRAG_FRAGMENT);
    }

    public void initFrameBuffer(int imageWidth, int imageHeight)
    {
        frameWidth = imageWidth;
        frameHeight = imageHeight;

        GLES20.glGenFramebuffers(1, frameBuffer, 0);
        GLES20.glGenTextures(1,textureBuffer, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureBuffer[0]);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, imageWidth, imageHeight, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBuffer[0]);
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, textureBuffer[0], 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl, int mTexId) {
        GLES20.glUseProgram(program);
        GLES20.glEnableVertexAttribArray(aPosition);
        GLES20.glVertexAttribPointer(aPosition, 2, GLES20.GL_FLOAT, false, 2 * 4, vertexBuffer);
        GLES20.glVertexAttribPointer(aTexCoord, 2, GLES20.GL_FLOAT, false, 2 * 4, texBuffer);
        GLES20.glEnableVertexAttribArray(aTexCoord);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexId);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
                IntBuffer ib = IntBuffer.allocate(frameHeight * frameWidth);
//        GLES20.glReadPixels(0, 0, frameWidth, frameHeight, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, ib);
//                      Bitmap result = Bitmap.createBitmap(frameWidth, frameHeight, Bitmap.Config.ARGB_8888);
//              result.copyPixelsFromBuffer(ib);
//              saveBitmap(result);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0);
        GLES20.glDisableVertexAttribArray(aPosition);
        GLES20.glDisableVertexAttribArray(aTexCoord);
    }

    //纹理的拷贝
    public int onDrawToTexture(int mTexId)
    {
        GLES20.glViewport(0, 0, frameWidth, frameHeight);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBuffer[0]);
        GLES20.glUseProgram(program);
        GLES20.glEnableVertexAttribArray(aPosition);
        GLES20.glVertexAttribPointer(aPosition, 2, GLES20.GL_FLOAT, false, 2 * 4, vertexBuffer);
        GLES20.glVertexAttribPointer(aTexCoord, 2, GLES20.GL_FLOAT, false, 2 * 4, texBuffer);
        GLES20.glEnableVertexAttribArray(aTexCoord);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, mTexId);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        IntBuffer ib = IntBuffer.allocate(frameHeight * frameWidth);
        GLES20.glReadPixels(0, 0, frameWidth, frameHeight, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, ib);
                      Bitmap result = Bitmap.createBitmap(frameWidth, frameHeight, Bitmap.Config.ARGB_8888);
              result.copyPixelsFromBuffer(ib);
              saveBitmap(result);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0);
        GLES20.glDisableVertexAttribArray(aPosition);
        GLES20.glDisableVertexAttribArray(aTexCoord);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        GLES20.glViewport(0, 0, mViewWidth, mViewHeight);
        return textureBuffer[0];
    }




}
