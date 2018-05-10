package com.hardencode.test.encode.gl;

import android.graphics.Bitmap;
import android.graphics.Path;
import android.opengl.EGLConfig;
import android.opengl.GLES20;
import android.os.Environment;
import android.util.Log;

import com.hardencode.test.filter.OptionGlUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Calendar;

public class GlEncodeVideoRender implements GlRender{
    private static final String TAG = "GlVideoRender";
    private int program;
    private int aPosition;
    private int aTexCoord;
    private ByteBuffer vertexBuffer;
    private ByteBuffer texBuffer;

    private int[] frameBuffer = new int[1];
    private int[] textureBuffer = new int[1];

    private static final String VERTEX_FRAGMENT = "" +
            "attribute vec3 aPosition;" + "\n"+
            "attribute vec2 aTexCoord;" + "\n"+
            "varying vec2 aVaryingTexCoord;" + "\n" +
            "void main(){"+"\n"+
            "gl_Position = vec4(aPosition, 1.0);" + "\n" +
            "aVaryingTexCoord = aTexCoord;" + "\n" +
            "}";
    private static final String FRAG_FRAGMENT = "" +
            "precision mediump float;" + "\n" +
            "uniform sampler2D  sTexture;" + "\n" +
            "varying vec2 aVaryingTexCoord;" +"\n" +
            "void main(){" + "\n" +
            "gl_FragColor = texture2D(sTexture, aVaryingTexCoord);" + "\n" +
            "}";

    public void onSurfaceCreated(EGLConfig config) {
        int vertexShader = loadSharder(VERTEX_FRAGMENT, GLES20.GL_VERTEX_SHADER);
        int fragShader = loadSharder(FRAG_FRAGMENT, GLES20.GL_FRAGMENT_SHADER);
        program = createProgram(vertexShader, fragShader);
        aPosition = GLES20.glGetAttribLocation(program, "aPosition");
        aTexCoord = GLES20.glGetAttribLocation(program, "aTexCoord");

//        vertexBuffer = ByteBuffer.allocateDirect(vertexs.length * 4);
//        vertexBuffer.order(ByteOrder.nativeOrder());
//        FloatBuffer buffer = vertexBuffer.asFloatBuffer();
//        buffer.put(vertexs);
//        vertexBuffer.position(0);
        vertexBuffer = OptionGlUtils.getDatasByteBuffer(OptionGlUtils.vertexs);
        float[] coords = OptionGlUtils.getDatasByteBuffer(OptionGlUtils.coordRotation90, false, false);
        texBuffer = OptionGlUtils.getDatasByteBuffer(coords);

//        texBuffer = ByteBuffer.allocateDirect(texCoord.length * 4);
//        texBuffer.order(ByteOrder.nativeOrder());
//        FloatBuffer buffer1 = texBuffer.asFloatBuffer();
//        buffer1.put(texCoord);
//        texBuffer.position(0);
    }


    public void onSurfaceChanged(int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }


    public void onDrawFrame(int mTexId) {
        GLES20.glUseProgram(program);
        GLES20.glEnableVertexAttribArray(aPosition);
        GLES20.glVertexAttribPointer(aPosition, 3, GLES20.GL_FLOAT, false, 3 * 4, vertexBuffer);
        GLES20.glVertexAttribPointer(aTexCoord, 2, GLES20.GL_FLOAT, false, 2 * 4, texBuffer);
        GLES20.glEnableVertexAttribArray(aTexCoord);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexId);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glDisableVertexAttribArray(aPosition);
        GLES20.glDisableVertexAttribArray(aTexCoord);
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
        File fileRoot = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/yyy/");
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
