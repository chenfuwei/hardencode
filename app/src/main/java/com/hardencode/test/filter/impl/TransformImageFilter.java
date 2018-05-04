package com.hardencode.test.filter.impl;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class TransformImageFilter extends BaseImageFilter{
    private int aTransformMatrixPosition;
    private int aOrthoMatrixPosition;

    private float[] transform;
    private float[] ortho;
    public TransformImageFilter(Context mContext) {
        super(mContext, "transform_vertex_shader.sh", "default_fragment_shader.sh");
    }

    @Override
    public void onOutputSizeChange(int nWidth, int nHeight) {
        super.onOutputSizeChange(nWidth, nHeight);
        Matrix.orthoM(ortho, 0, -1.0f, 1.0f, -1.0f * (float) nWidth / (float) nHeight, 1.0f * (float) nWidth / (float) nHeight, -1.0f, 1.0f);
       runOnDraw(new Runnable() {
           @Override
           public void run() {
               GLES20.glUniformMatrix4fv(aOrthoMatrixPosition, 1, false, ortho,0);
           }
       });
    }

    @Override
    protected void onInit() {
        super.onInit();
        aTransformMatrixPosition = GLES20.glGetUniformLocation(mProgram, "transformMatrix");
        aOrthoMatrixPosition = GLES20.glGetUniformLocation(mProgram, "orthoMatrix");

        transform = new float[16];
        Matrix.setRotateM(transform, 0, 30.0f, 0.0f, 0.0f, 1.0f);

        ortho = new float[16];
        Matrix.orthoM(ortho, 0, -1.0f, 1.0f, -1.0f, 1.0f, -1.0f, 1.0f);

        runOnDraw(new Runnable() {
            @Override
            public void run() {
                GLES20.glUniformMatrix4fv(aTransformMatrixPosition, 1, false, transform, 0);
                GLES20.glUniformMatrix4fv(aOrthoMatrixPosition, 1, false, ortho, 0);
            }
        });
    }

    @Override
    protected void onDrawFramePre() {
        super.onDrawFramePre();

    }

    @Override
    public void setProgressValue(float percent) {
        super.setProgressValue(percent);
        final float[] tmpTransform = new float[16];
        Matrix.setRotateM(tmpTransform, 0, percent * 360, 0.0f, 0.0f, 1.0f);
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                GLES20.glUniformMatrix4fv(aTransformMatrixPosition, 1, false, tmpTransform, 0);
            }
        });
    }

    @Override
    public int onDrawFrame(int glTexture, ByteBuffer vertexBuffer, ByteBuffer coordBuffer) {

        FloatBuffer cubeBuffer = vertexBuffer.asFloatBuffer();


            float[] adjustedVertices = new float[12];

            cubeBuffer.position(0);
            cubeBuffer.get(adjustedVertices);

            float normalizedHeight = (float) nOutputHeight / (float) nOutputWidth;
            adjustedVertices[1] *= normalizedHeight;
            adjustedVertices[4] *= normalizedHeight;
            adjustedVertices[7] *= normalizedHeight;
            adjustedVertices[10] *= normalizedHeight;

        vertexBuffer = ByteBuffer.allocateDirect(adjustedVertices.length * 4)
                    .order(ByteOrder.nativeOrder());
        cubeBuffer = vertexBuffer.asFloatBuffer();

        cubeBuffer.put(adjustedVertices).position(0);

        return super.onDrawFrame(glTexture, vertexBuffer, coordBuffer);
    }
}
