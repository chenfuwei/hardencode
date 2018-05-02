package com.hardencode.test.filter;

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

    public static ByteBuffer getDatasByteBuffer(float[] vertexs)
    {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertexs.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
        floatBuffer.put(vertexs);
        byteBuffer.position(0);
        return byteBuffer;
    }
}
