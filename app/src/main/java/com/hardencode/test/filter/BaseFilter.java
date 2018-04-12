package com.hardencode.test.filter;

import android.graphics.Bitmap;
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
import java.util.Calendar;

import javax.microedition.khronos.opengles.GL10;

public class BaseFilter {
    private static final String TAG = "BaseFilter";
    protected int program;
    protected int aPosition;
    protected int aTexCoord;
    protected ByteBuffer vertexBuffer;
    protected ByteBuffer texBuffer;

    protected static final String VERTEX_DEFAULT_FRAGMENT = "" +
            "attribute vec2 aPosition;" + "\n"+
            "attribute vec2 aTexCoord;" + "\n"+
            "varying vec2 aVaryingTexCoord;" + "\n" +
            "void main(){"+"\n"+
            "gl_Position = vec4(aPosition, 0.0, 1.0);" + "\n" +
            "aVaryingTexCoord = aTexCoord;" + "\n" +
            "}";

    protected static final String FRAG_DEFAULT_FRAGMENT = "" +
            "precision mediump float;" + "\n" +
            "uniform sampler2D  sTexture;" + "\n" +
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

    private String vertextSource = VERTEX_DEFAULT_FRAGMENT;
    private String fragSource = FRAG_DEFAULT_FRAGMENT;
    protected int mViewWidth;
    protected int mViewHeight;

    public BaseFilter()
    {
        this(VERTEX_DEFAULT_FRAGMENT, FRAG_DEFAULT_FRAGMENT);
    }

    public BaseFilter(String vertextSource, String fragSource)
    {
        this.vertextSource = vertextSource;
        this.fragSource = fragSource;
    }

    public void onInit() {
        int vertexShader = loadSharder(vertextSource, GLES20.GL_VERTEX_SHADER);
        int fragShader = loadSharder(fragSource, GLES20.GL_FRAGMENT_SHADER);
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

    public void onInputChange(int width, int height) {
        mViewWidth = width;
        mViewHeight = height;
    }

    public void onDrawFrame(GL10 gl, int mTexId) {
        GLES20.glUseProgram(program);
        GLES20.glEnableVertexAttribArray(aPosition);
        GLES20.glVertexAttribPointer(aPosition, 2, GLES20.GL_FLOAT, false, 2 * 4, vertexBuffer);
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
