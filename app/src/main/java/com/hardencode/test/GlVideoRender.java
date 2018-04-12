package com.hardencode.test;

import android.graphics.Bitmap;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.os.Environment;
import android.util.Log;

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

public class GlVideoRender{
    private static final String TAG = "GlVideoRender";
    private int program;
    private int aPosition;
    private int aTexCoord;
    private ByteBuffer vertexBuffer;
    private ByteBuffer texBuffer;

    private int[] frameBuffer = new int[1];
    private int[] textureBuffer = new int[1];

    private int frameWidth;
    private int frameHeight;

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

    private static final String VERTEX_FRAGMENT = "" +
            "attribute vec2 aPosition;" + "\n"+
            "attribute vec2 aTexCoord;" + "\n"+
            "varying vec2 aVaryingTexCoord;" + "\n" +
            "void main(){"+"\n"+
            "gl_Position = vec4(aPosition, 0.0, 1.0);" + "\n" +
            "aVaryingTexCoord = aTexCoord;" + "\n" +
            "}";
    private static final String FRAG_FRAGMENT = "" +
            "#extension GL_OES_EGL_image_external : require\n"+
            "precision mediump float;" + "\n" +
            "uniform samplerExternalOES  sTexture;" + "\n" +
            "varying vec2 aVaryingTexCoord;" +"\n" +
            "void main(){" + "\n" +
            "gl_FragColor = texture2D(sTexture, aVaryingTexCoord);" + "\n" +
            "}";

    private float[] vertexs = new float[]{
            -1.0f, -1.0f,
            1.0f, -1.0f,
            -1.0f, 1.0f,
            1.0f, 1.0f

    };

    private float[] texCoord = new float[]{
//            0.0f, 0.0f,
//            0.0f, 1.0f,
//            1.0f, 0.0f,
//            1.0f, 1.0f

            0.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f
    };

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        int vertexShader = loadSharder(VERTEX_FRAGMENT, GLES20.GL_VERTEX_SHADER);
        int fragShader = loadSharder(FRAG_FRAGMENT, GLES20.GL_FRAGMENT_SHADER);
        program = createProgram(vertexShader, fragShader);
        aPosition = GLES20.glGetAttribLocation(program, "aPosition");
        aTexCoord = GLES20.glGetAttribLocation(program, "aTexCoord");

        vertexBuffer = ByteBuffer.allocateDirect(vertexs.length * 4);
        vertexBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer buffer = vertexBuffer.asFloatBuffer();
        buffer.put(vertexs);
        vertexBuffer.position(0);


        texBuffer = ByteBuffer.allocateDirect(texCoord.length * 4);
        texBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer buffer1 = texBuffer.asFloatBuffer();
        buffer1.put(texCoord);
        texBuffer.position(0);
    }


    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }


    public void onDrawFrame(GL10 gl, int mTexId) {
        GLES20.glUseProgram(program);
        GLES20.glEnableVertexAttribArray(aPosition);
        GLES20.glVertexAttribPointer(aPosition, 2, GLES20.GL_FLOAT, false, 2 * 4, vertexBuffer);
        GLES20.glVertexAttribPointer(aTexCoord, 2, GLES20.GL_FLOAT, false, 2 * 4, texBuffer);
        GLES20.glEnableVertexAttribArray(aTexCoord);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
//        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, mTexId);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexId);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
                IntBuffer ib = IntBuffer.allocate(frameHeight * frameWidth);
        GLES20.glReadPixels(0, 0, frameWidth, frameHeight, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, ib);
                      Bitmap result = Bitmap.createBitmap(frameWidth, frameHeight, Bitmap.Config.ARGB_8888);
              result.copyPixelsFromBuffer(ib);
              saveBitmap(result);
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
        return textureBuffer[0];
    }

    private int loadSharder(String frag, int type)
    {
        int shader = GLES20.glCreateShader(type);
        if(shader != 0)
        {
            GLES20.glShaderSource(shader, frag);
            GLES20.glCompileShader(shader);

            int[] status = new int[1];
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, status, 0);
            if(status[0] == 0)
            {
                String log = GLES20.glGetShaderInfoLog(shader);
                Log.e(TAG, "loadSharder frag = " + frag + " type = " + type  + " log = " + log);
                GLES20.glDeleteShader(shader);
                shader = 0;
            }
        }
        return shader;
    }

    private int createProgram(int vertexShader, int fragShader)
    {
        int program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragShader);
        GLES20.glLinkProgram(program);

        int[] stats = new int[1];
        GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, stats, 0);
        if(stats[0] == 0)
        {
            String link = GLES20.glGetProgramInfoLog(program);
            Log.e(TAG, "createProgram link = " + link);
            GLES20.glDeleteProgram(program);
            program = 0;
        }
        return program;
    }

    public static String saveBitmap(Bitmap bitmap) {
        File fileRoot = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/rrr/");
        if(!fileRoot.exists())
        {
            fileRoot.mkdirs();
        }
        File file = new File(fileRoot.getPath() , Calendar.getInstance().getTimeInMillis() + ".png");
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            bitmap.recycle();
            return file.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
