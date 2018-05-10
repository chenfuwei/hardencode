package com.hardencode.test;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.EGL14;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;

import com.hardencode.test.config.Config;
import com.hardencode.test.encode.ViEncode;
import com.hardencode.test.encode.gl.CustomGlViewRender;
import com.hardencode.test.encode.gl.GlEncodeVideoRender;
import com.hardencode.test.filter.BaseFilter;
import com.hardencode.test.filter.ImageFilter;
import com.hardencode.test.filter.ImageFilterFactory;
import com.hardencode.test.filter.ImageType;
import com.hardencode.test.filter.OptionGlUtils;
import com.hardencode.test.filter.Rotation;
import com.hardencode.test.filter.impl.BaseImageFilter;
import com.hardencode.test.filter.impl.group.BaseFilterGroup;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

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

    private int viewWidth, viewHeight;

    private ImageFilterFactory factory;
    private BaseImageFilter baseImageFilter;
    private BaseImageFilter defaultImageFilter;

    private Rotation rotation = Rotation.ROTATION90;
    private ImageType imageType = ImageType.CENTER;
    ByteBuffer vertexBuffer;
    ByteBuffer coordBuffer;


    public PreviewGlSurfaceView(Context context) {
        this(context, null);
    }

    public PreviewGlSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(2);
        setRenderer(this);

        cameraPreview = new CameraPreview();

        viEncode = new ViEncode();
        viEncode.setWidth(Config.VIDEO_WIDTH);
        viEncode.setHeight(Config.VIDEO_HEIGHT);
        viEncode.initEncoder();
        glViewRender = new CustomGlViewRender();
        encodeVideoRender = new GlEncodeVideoRender();
        glViewRender.setGlRender(encodeVideoRender);

        factory = new ImageFilterFactory();

        vertexBuffer = OptionGlUtils.getDatasByteBuffer(OptionGlUtils.vertexs);
        coordBuffer = OptionGlUtils.getDatasByteBuffer(OptionGlUtils.coordRotation90);
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

        videoRender.initFrameBuffer(Config.VIDEO_WIDTH, Config.VIDEO_HEIGHT);

        glViewRender.setShareEGLContext(EGL14.eglGetCurrentContext());
        glViewRender.onSurfaceCreated(viEncode.getEncodeSurface(), Config.VIDEO_WIDTH, Config.VIDEO_HEIGHT);

//        baseImageFilter = new BaseFilterGroup(getContext());
//        List<BaseImageFilter> filtes = new ArrayList<BaseImageFilter>();
//        filtes.add(factory.getImageFilter(ImageFilter.ALPHABLEND, getContext()));
//        filtes.add(factory.getImageFilter(ImageFilter.EMBOSS, getContext()));
//        ((BaseFilterGroup)baseImageFilter).setFilters(filtes);

        baseImageFilter = factory.getImageFilter(ImageFilter.ALPHABLEND, getContext());

        baseImageFilter.onInputImageSizeChange(Config.VIDEO_WIDTH, Config.VIDEO_HEIGHT);

        defaultImageFilter = factory.getImageFilter(ImageFilter.DEFAULT, getContext());
        defaultImageFilter.onInputImageSizeChange(Config.VIDEO_WIDTH, Config.VIDEO_HEIGHT);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        viewWidth = width;
        viewHeight = height;
        GLES20.glViewport(0,0,width, height);
        videoRender.onInputChange(width, height);
        video2Render.onInputChange(width, height);

        baseImageFilter.onOutputSizeChange(width, height);
        defaultImageFilter.onOutputSizeChange(width, height);

        adjustImageScale();
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
        id = videoRender.onDrawToTexture(id);
        id =  baseImageFilter.onDrawFrameToTexture(id);
        GLES20.glViewport(0, 0, viewWidth, viewHeight);

        defaultImageFilter.onDrawFrame(id,vertexBuffer,coordBuffer);


        glViewRender.setmTexId(id);
        glViewRender.requestRender();
        viEncode.getH264Data();
    }

    private void adjustImageScale()
    {
        int outputWidth = viewWidth;
        int outputHeight = viewHeight;
        int imageWidth = Config.VIDEO_WIDTH;
        int imageHeight = Config.VIDEO_HEIGHT;
        if(rotation == Rotation.ROTATION90 || rotation == Rotation.ROTATION270)
        {
            outputWidth = viewHeight;
            outputHeight = viewWidth;
        }

        float scaleWidth = outputWidth / (float)imageWidth;
        float scaleHeight = outputHeight / (float)imageHeight;
        float maxScale = Math.max(scaleWidth, scaleHeight);
        int scaleOutputWidth = Math.round(imageWidth * maxScale);
        int scaleOutputHeight = Math.round(imageHeight * maxScale);
        float ratioWidth = (float)scaleOutputWidth / outputWidth;
        float ratioHeight = (float)scaleOutputHeight / outputHeight;

        if(imageType == ImageType.CENTER)
        {
            float[] coords = OptionGlUtils.getDatasByteBuffer(OptionGlUtils.coordRotation90, false, true);
            float distanceWidth = (float)(1.0 - 1.0 / ratioWidth) / 2;
            float distanceHeight = (float)(1.0 - 1.0 / ratioHeight) / 2;

            coords = new float[]{
                    addDistance(coords[0], distanceWidth), addDistance(coords[1], distanceHeight),
                    addDistance(coords[2], distanceWidth), addDistance(coords[3], distanceHeight),
                    addDistance(coords[4], distanceWidth), addDistance(coords[5], distanceHeight),
                    addDistance(coords[6], distanceWidth), addDistance(coords[7], distanceHeight)
            };

            coordBuffer = OptionGlUtils.getDatasByteBuffer(coords);
        }else if(imageType == ImageType.INSIDE)
        {
            float[] vertexs = OptionGlUtils.vertexs;
            vertexs = new float[]{
                    vertexs[0] / ratioHeight, vertexs[1] / ratioWidth, 0.0f,
                    vertexs[3] / ratioHeight, vertexs[4] / ratioWidth, 0.0f,
                    vertexs[6] / ratioHeight, vertexs[7] / ratioWidth, 0.0f,
                    vertexs[9] / ratioHeight, vertexs[10] / ratioWidth, 0.0f
            };
            vertexBuffer = OptionGlUtils.getDatasByteBuffer(vertexs);
        }
    }

    private float addDistance(float value, float distance)
    {
        return value == 0.0 ? distance : (float)(1.0 - distance);
    }

    public void onDestroy()
    {
        viEncode.release();
    }

    public void setProgressValue(float percent)
    {
        if(null != baseImageFilter)
        {
            baseImageFilter.setProgressValue(percent);
        }
    }
}
