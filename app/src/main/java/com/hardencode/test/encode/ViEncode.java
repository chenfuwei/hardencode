package com.hardencode.test.encode;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.util.Log;
import android.view.Surface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import static android.content.ContentValues.TAG;
import static android.media.MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface;

public class ViEncode {
    private static final String TAG = "ViEncode";
    private static final String TYPE = "video/avc";

    private MediaCodec mediaCodec;

    private int width, height;

    private Surface encodeSurface;

    public void initEncoder()
    {
        try {
            initFile();
        }catch (Exception e)
        {

        }
        try {
            MediaFormat mediaFormat = MediaFormat.createVideoFormat(TYPE, width,height);
            mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, 400 * 1000);
            mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, 15);
            mediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, COLOR_FormatSurface );
            mediaFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 5);
//			mediaFormat.setInteger("profile", MediaCodecInfo.CodecProfileLevel.AVCProfileHigh);
//			mediaFormat.setInteger("level", MediaCodecInfo.CodecProfileLevel.AVCLevel41);


            mediaCodec = MediaCodec.createEncoderByType(TYPE);
            mediaCodec.configure(mediaFormat, null, null,MediaCodec.CONFIGURE_FLAG_ENCODE);
            try {
                encodeSurface = mediaCodec.createInputSurface();
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            mediaCodec.start();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public Surface getEncodeSurface()
    {
        return encodeSurface;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void getH264Data()
    {
        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        int outputIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, 0);
        while(outputIndex >= 0)
        {
            Log.i(TAG, "outputIndex = " + outputIndex);
            ByteBuffer buffer = mediaCodec.getOutputBuffer(outputIndex);
            byte[] tmp = new byte[bufferInfo.size];
            buffer.get(tmp);

			try {
				stream.write(tmp);
			}catch (Exception e)
			{
				e.printStackTrace();
			}
            mediaCodec.releaseOutputBuffer(outputIndex, false);
            outputIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, 0);
        }
    }


    private FileOutputStream stream = null;
    private void initFile() throws IOException {
        File f = new File("/sdcard/testandroid-h264.h264");
        if(!f.exists()){
            f.createNewFile();
        }
        stream = new FileOutputStream(f);
    }
    private void close() throws IOException{
        if(stream != null){
            stream.flush();
            stream.close();
        }
    }
}
