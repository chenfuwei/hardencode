package com.hardencode.test;

import android.Manifest;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.QuickContactBadge;
import android.widget.SeekBar;

import com.hardencode.test.permission.PermissionsHelper;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private PreviewGlSurfaceView previewGlSurfaceView;

    private SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionsHelper.with(this).requestCode(1000).requestPermission(
                Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        ).request();
        previewGlSurfaceView = (PreviewGlSurfaceView)findViewById(R.id.glsurfaceview);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // test();
            }
        });


        seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                processSeekBarValue(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        previewGlSurfaceView.onDestroy();
        super.onDestroy();
    }

    private void processSeekBarValue(int progress)
    {
        float setProgress = (float)(progress / 100.0);
        previewGlSurfaceView.setProgressValue(setProgress);
        Log.i("progress","setProgress = " + setProgress);
    }
}
