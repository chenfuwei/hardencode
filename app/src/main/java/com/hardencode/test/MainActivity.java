package com.hardencode.test;

import android.Manifest;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.QuickContactBadge;

import com.hardencode.test.permission.PermissionsHelper;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private PreviewGlSurfaceView previewGlSurfaceView;

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
                test();
            }
        });
    }

    private void test()
    {
    }
}
