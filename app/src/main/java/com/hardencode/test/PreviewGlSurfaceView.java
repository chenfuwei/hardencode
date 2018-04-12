package com.hardencode.test;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.EGL14;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;

import com.hardencode.test.encode.ViEncode;
import com.hardencode.test.encode.gl.CustomGlViewRender;
import com.hardencode.test.encode.gl.GlEncodeVideoRender;
import com.hardencode.test.filter.BaseFilter;

import javax.microedition.khronos.egl.EGL;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class PreviewGlSurfaceView extends GLSurfaceView implements GLSurfaceView.Renderer{
    private static final String TAG = "PreviewGlSurfaceView";
    private CameraPreview cameraPreview;
    private SurfaceTexture surfaceTexture;
    private int textId;

    private CameraInputFilter videoRender;  //用于纹理拷贝，用于将OES纹理转换为普通纹理
    private BaseFilter video2Render; //用于本地画面显示

    private CustomGlViewRender glViewRender;   //构造GL线程，用于更新编码器的Surface用于录制
    private GlEncodeVideoRender encodeVideoRender; //渲染编码器的surface
    private ViEncode viEncode;  //编码器

    public PreviewGlSurfaceView(Context context) {
        this(context, null);
    }

    public PreviewGlSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(2);
        setRenderer(this);

        cameraPreview = new CameraPreview();

        viEncode = new ViEncode();
        viEncode.setWidth(320);
        viEncode.setHeight(240);
        viEncode.initEncoder();
        glViewRender = new CustomGlViewRender();
        encodeVideoRender = new GlEncodeVideoRender();
        glViewRender.setGlRender(encodeVideoRender);
    }

    private SurfaceTexture.OnFrameAvailableListener onFrameAvailableListener = new SurfaceTexture.OnFrameAvailableListener() {
        @Override
        public void onFrameAvailable(SurfaceTexture surfaceTexture) {
            requestRender();
        }
    };

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        videoRender = new CameraInputFilter();
        video2Render = new BaseFilter();
        int[] textures = new int[1];
        GLES20.glGenTextures(1, textures, 0);
        textId = textures[0];
        Log.i(TAG, "textId = " + textId);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textId);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        surfaceTexture = new SurfaceTexture(textId);
        surfaceTexture.setOnFrameAvailableListener(onFrameAvailableListener);

        cameraPreview.setSurfaceTexture(surfaceTexture);
        cameraPreview.startPreview(getContext());

        videoRender.onInit();
        video2Render.onInit();

        videoRender.initFrameBuffer(320, 240);

        glViewRender.setShareEGLContext(EGL14.eglGetCurrentContext());
        glViewRender.onSurfaceCreated(viEncode.getEncodeSurface(), 320, 240);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0,0,width, height);
        videoRender.onInputChange(width, height);
        video2Render.onInputChange(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        try {
            surfaceTexture.updateTexImage();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        int id = textId;
        id = videoRender.onDrawToTexture(textId);
        video2Render.onDrawFrame(gl, id);


        glViewRender.setmTexId(id);
        glViewRender.requestRender();
        viEncode.getH264Data();
    }
}
