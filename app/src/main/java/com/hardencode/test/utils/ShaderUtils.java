package com.hardencode.test.utils;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import java.io.InputStream;

/**
 * Created by Administrator on 2018/3/7.
 */

public class ShaderUtils {

    public static String getShaderStr(Context context, String assertName)
    {
        try {
            InputStream is = context.getAssets().open(assertName);
            int size = is.available();
            // Read the entire asset into a local byte buffer.
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            // Convert the buffer into a string.
            String text = new String(buffer, "utf-8");
            text=text.replaceAll("\\r\\n","\n");
            // Finally stick the string into the text view.
            return text;
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return "";
    }

    public static int loadShader(int shaderType, String shaderSource)
    {
        int shader = GLES20.glCreateShader(shaderType);
        if(shader != 0)
        {
            GLES20.glShaderSource(shader, shaderSource);
            GLES20.glCompileShader(shader);
            int a = GLES20.glGetError();
            int compiles[] = new int[1];
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiles, 0);
            if(compiles[0] == 0)
            {
                //若编译失败则显示错误日志并删除此shader
                Log.e("ES20_ERROR", "Could not compile shader " + shaderType + ":" + " a = " + a);
                Log.e("ES20_ERROR", GLES20.glGetShaderInfoLog(shader));
                GLES20.glDeleteShader(shader);
                shader = 0;
            }
        }
        return shader;
    }

    public static int createProgram(String vertexSource, String fragmentSource)
    {
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource);
        if(vertexShader == 0)
        {
            Log.e("ES20_ERROR", "vertexShader: " + vertexShader);
            return 0;
        }
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);
        if(fragmentShader == 0)
        {
            Log.e("ES20_ERROR", "fragmentShader: " + fragmentShader);
            return 0;
        }
        int program = GLES20.glCreateProgram();
        if(program != 0)
        {
            GLES20.glAttachShader(program, vertexShader);
            GLES20.glAttachShader(program, fragmentShader);
            GLES20.glLinkProgram(program);

            int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
            //若链接失败则报错并删除程序
            if (linkStatus[0] != GLES20.GL_TRUE)
            {
                Log.e("ES20_ERROR", "Could not link program: ");
                Log.e("ES20_ERROR", GLES20.glGetProgramInfoLog(program));
                GLES20.glDeleteProgram(program);
                program = 0;
            }
        }

        return program;
    }

}
