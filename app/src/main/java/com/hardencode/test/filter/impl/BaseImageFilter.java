package com.hardencode.test.filter.impl;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import com.hardencode.test.filter.OptionGlUtils;
import com.hardencode.test.utils.ShaderUtils;

import java.nio.ByteBuffer;
import java.util.LinkedList;

public class BaseImageFilter {
    private static final String TAG = "BaseImageFilter";
    private Context mContext;
    protected int mProgram;

    private int aPosition;
    private int aCoordinate;

    private ByteBuffer vertexBuffer;
    private ByteBuffer coordBuffer;

    private LinkedList<Runnable> runnables;

    private int nInputImageWidth;
    private int nInputImageHeight;

    protected int nOutputWidth;
    protected int nOutputHeight;

    private int saveTextureId;
    private int saveFrameBufferId;

   public BaseImageFilter(Context mContext)
    {
        this(mContext, "default_vertex_shader.sh", "default_fragment_shader.sh");
    }

    public BaseImageFilter(Context mContext, String vertexShader, String fragShader)
    {
        runnables = new LinkedList<>();
        this.mContext = mContext;
        init(vertexShader, fragShader);
    }

    private void init(String vertexShader, String fragShader)
    {
        String sVertexShader = ShaderUtils.getShaderStr(mContext, vertexShader);
        String sFragShader = ShaderUtils.getShaderStr(mContext, fragShader);
        mProgram = ShaderUtils.createProgram(sVertexShader, sFragShader);
        if(mProgram <= 0)
        {
            Log.e(TAG, "mProgram = " + mProgram);
        }
        aPosition = GLES20.glGetAttribLocation(mProgram, "aPosition");
        aCoordinate = GLES20.glGetAttribLocation(mProgram, "aCoordinate");

        vertexBuffer = OptionGlUtils.getDatasByteBuffer(OptionGlUtils.vertexs);
        coordBuffer = OptionGlUtils.getDatasByteBuffer(OptionGlUtils.coordRotation0);
        onInit();
    }

    private void initSaveTextureId()
    {
        int[] buffers = new int[1];
        GLES20.glGenFramebuffers(1, buffers, 0);
        saveFrameBufferId = buffers[0];

        int[] textures = new int[1];
        GLES20.glGenTextures(1, textures, 0);
        saveTextureId = textures[0];

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, saveFrameBufferId);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, saveTextureId);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, nInputImageWidth, nInputImageHeight, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, saveTextureId, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }

    public int onDrawFrameToTexture(int glTexture)
    {
        if(saveTextureId <= 0 || saveFrameBufferId <= 0)
        {
            initSaveTextureId();
        }
        GLES20.glViewport(0, 0, nInputImageWidth, nInputImageHeight);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, saveFrameBufferId);
        GLES20.glUseProgram(mProgram);

        GLES20.glEnableVertexAttribArray(aPosition);
        GLES20.glEnableVertexAttribArray(aCoordinate);

        GLES20.glVertexAttribPointer(aPosition, 3, GLES20.GL_FLOAT, false, 3 * 4, vertexBuffer);
        GLES20.glVertexAttribPointer(aCoordinate, 2, GLES20.GL_FLOAT, false, 2 * 4, coordBuffer);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, glTexture);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        runOnDrawTask();
        onDrawFramePre();

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);


        GLES20.glDisableVertexAttribArray(aPosition);
        GLES20.glDisableVertexAttribArray(aCoordinate);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        return saveTextureId;
    }


    public int onDrawFrame(int glTexture, ByteBuffer vertexBuffer, ByteBuffer coordBuffer)
    {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glUseProgram(mProgram);

        GLES20.glEnableVertexAttribArray(aPosition);
        GLES20.glEnableVertexAttribArray(aCoordinate);

        GLES20.glVertexAttribPointer(aPosition, 3, GLES20.GL_FLOAT, false, 3 * 4, vertexBuffer);
        GLES20.glVertexAttribPointer(aCoordinate, 2, GLES20.GL_FLOAT, false, 2 * 4, coordBuffer);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, glTexture);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        onDrawFramePre();
        runOnDrawTask();

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);


        GLES20.glDisableVertexAttribArray(aPosition);
        GLES20.glDisableVertexAttribArray(aCoordinate);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        onDrawFrameLast();
        return saveTextureId;
    }

    protected void runOnDrawTask()
    {
        synchronized (runnables)
        {
            while (runnables.size() > 0) {
                runnables.removeFirst().run();
            }
        }
    }

    public void runOnDraw(Runnable runnable)
    {
        synchronized (runnables)
        {
            runnables.add(runnable);
        }
    }


    public void onInputImageSizeChange(int nWidth, int nHeight)
    {
        this.nInputImageWidth = nWidth;
        this.nInputImageHeight = nHeight;
    }

    public void onOutputSizeChange(int nWidth, int nHeight)
    {
        this.nOutputWidth = nWidth;
        this.nOutputHeight = nHeight;
    }

    protected void onInit()
    {
        //子类实现
    }

    protected void onDrawFramePre()
    {
        //子类实现
    }

    protected void onDrawFrameLast()
    {

    }

    public void setProgressValue(float percent)
    {

    }

    public int getProgram() {
        return mProgram;
    }

    public int getInputImageWidth() {
        return nInputImageWidth;
    }

    public int getInputImageHeight() {
        return nInputImageHeight;
    }
}
