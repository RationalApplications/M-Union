package xyz.ratapp.munion;

import android.hardware.Camera;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import xyz.ratapp.munion.R;

/**
 * <p>Date: 09.11.17</p>
 *
 * @author Simon
 */

public class CameraActivity extends AppCompatActivity implements
        SurfaceHolder.Callback,
        View.OnClickListener,
        Camera.PictureCallback,
        Camera.PreviewCallback,
        Camera.AutoFocusCallback {


    private static final String ARGS_TEXT = "camera_args_text";
    private static final String ARGS_WIDTH = "camera_args_width";
    private static final String ARGS_HEIGHT = "camera_args_height";

    private Camera camera;
    private SurfaceHolder surfaceHolder;
    private SurfaceView preview;
    private ImageButton shotBtn;
    private AppCompatTextView cameraText;
    private ImageButton backButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.fragment_camera);

        View view = findViewById(R.id.camera_frame_layout);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (camera != null){
                    camera.autoFocus(null);
                }
            }
        });

        preview = findViewById(R.id.camera_surface);

        surfaceHolder = preview.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        shotBtn = findViewById(R.id.camera_shot);
        cameraText = findViewById(R.id.camera_text);
        backButton = findViewById(R.id.camera_back);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        cameraText.setText("Сделайте фото паспорта");

    }

    @Override
    public void onResume() {
        super.onResume();
        camera = Camera.open();
        camera.autoFocus(null);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (camera != null) {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(holder);
            camera.setPreviewCallback(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        camera.setDisplayOrientation(90);
        camera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    @Override
    public void onClick(View v) {
        if (v == shotBtn) {
            camera.autoFocus(this);
        }
    }

    @Override
    public void onPictureTaken(byte[] paramArrayOfByte, Camera paramCamera) {
        // сохраняем полученные jpg в папке /sdcard/CameraExample/
        // имя файла - System.currentTimeMillis()

//        try {
//            File saveDir = new File("/sdcard/CameraExample/");
//
//            if (!saveDir.exists()) {
//                saveDir.mkdirs();
//            }
//
//            FileOutputStream os = new FileOutputStream(String.format("/sdcard/CameraExample/%d.jpg", System.currentTimeMillis()));
//            os.write(paramArrayOfByte);
//            os.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        // после того, как снимок сделан, показ превью отключается. необходимо включить его
        paramCamera.startPreview();
    }

    @Override
    public void onAutoFocus(boolean paramBoolean, Camera paramCamera) {
        if (paramBoolean) {
            // если удалось сфокусироваться, делаем снимок
            paramCamera.takePicture(null, null, null, this);
        }
    }

    @Override
    public void onPreviewFrame(byte[] paramArrayOfByte, Camera paramCamera) {
        // здесь можно обрабатывать изображение, показываемое в preview
    }
}
