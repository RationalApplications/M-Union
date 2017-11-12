package xyz.ratapp.munion;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.List;

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

    private static final String PHOTO_RESULT = "xyz.ratapp.muinion.camera.result";

    private static final String ARGS_TEXT = "camera_args_text";
    private static final String ARGS_WIDTH = "camera_args_width";
    private static final String ARGS_HEIGHT = "camera_args_height";

    private String argsText;
    private Integer argsWidth;
    private Integer argsHeight;

    private Camera camera;
    private SurfaceHolder surfaceHolder;
    private SurfaceView preview;
    private ImageButton shotBtn;
    private AppCompatTextView cameraText;
    private ImageButton backButton;
    private View cameraDocumentFrame;
    private View loading;


    private Button acceptPhoto;
    private Button cancelPhoto;

    public static Intent newIntent(Context context, String text, int width, int height) {
        Intent i = new Intent(context, CameraActivity.class);

        i.putExtra(ARGS_TEXT, text);
        i.putExtra(ARGS_WIDTH, width);
        i.putExtra(ARGS_HEIGHT, height);

        return i;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.fragment_camera);

        argsText = getIntent().getStringExtra(ARGS_TEXT);
        argsWidth = getIntent().getIntExtra(ARGS_WIDTH, 200);
        argsHeight = getIntent().getIntExtra(ARGS_HEIGHT, 300);

        View view = findViewById(R.id.camera_frame_layout);
        view.setOnClickListener(this);

        acceptPhoto = findViewById(R.id.camera_accept);
        cancelPhoto = findViewById(R.id.camera_cancel);

        preview = findViewById(R.id.camera_surface);

        loading = findViewById(R.id.camera_loading);

        surfaceHolder = preview.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        shotBtn = findViewById(R.id.camera_shot);
        shotBtn.setOnClickListener(this);

        cameraText = findViewById(R.id.camera_text);
        backButton = findViewById(R.id.camera_back);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        cameraText.setText(argsText);

        cameraDocumentFrame = findViewById(R.id.camera_document_frame);
        resizeView(cameraDocumentFrame, argsWidth, argsHeight);
    }

    @Override
    public void onResume() {
        super.onResume();
        camera = Camera.open();
        Camera.Parameters parameters = camera.getParameters();
        List<Camera.Size> sizes = parameters.getSupportedPictureSizes();
        parameters.setPictureSize(sizes.get(0).width, sizes.get(0).height);
        parameters.set("orientation", "portrait");
        parameters.setRotation(90);
        camera.setDisplayOrientation(90);
        camera.setParameters(parameters);
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
        if (v.getId() == R.id.camera_shot) {
            camera.autoFocus(this);
        } else if (v.getId() == R.id.camera_frame_layout && camera != null) {
            camera.autoFocus(null);
        }
    }

    @Override
    public void onPictureTaken(final byte[] paramArrayOfByte, final Camera paramCamera) {
        // сохраняем полученные jpg в папке /sdcard/CameraExample/
        // имя файла - System.currentTimeMillis()

        camera.stopPreview();

        acceptPhoto.setVisibility(View.VISIBLE);
        cancelPhoto.setVisibility(View.VISIBLE);

        backButton.setVisibility(View.GONE);
        cameraText.setVisibility(View.GONE);
        cameraDocumentFrame.setVisibility(View.GONE);
        shotBtn.setVisibility(View.GONE);


        acceptPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = savePhoto(paramArrayOfByte);
                setResultUri(uri);
                finish();
            }
        });

        cancelPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptPhoto.setVisibility(View.GONE);
                cancelPhoto.setVisibility(View.GONE);

                backButton.setVisibility(View.VISIBLE);
                cameraText.setVisibility(View.VISIBLE);
                cameraDocumentFrame.setVisibility(View.VISIBLE);
                shotBtn.setVisibility(View.VISIBLE);

                paramCamera.startPreview();
            }
        });

    }

    private void setResultUri(Uri uri){
        Intent data = new Intent();
        data.putExtra(PHOTO_RESULT, uri);
        setResult(RESULT_OK, data);
    }

    private Uri savePhoto(byte[] data) {
        loading.setVisibility(View.VISIBLE);
        acceptPhoto.setVisibility(View.GONE);
        cancelPhoto.setVisibility(View.GONE);

        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        Uri uri = null;

        if (bitmap != null) {

            File file = new File(Environment.getExternalStorageDirectory() + "/MUnion");
            if (!file.isDirectory()) {
                file.mkdir();
            }

            file = new File(Environment.getExternalStorageDirectory() + "/MUnion", System.currentTimeMillis() + ".png");

            uri = Uri.fromFile(file);

            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 1, fileOutputStream);

                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception exception) {
                exception.printStackTrace();
            }

        }

        return uri;
    }

    public static Uri getPhotoUri(Intent result) {
        return result.getParcelableExtra(PHOTO_RESULT);
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


    private void resizeView(View view, int newWidth, int newHeight) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        layoutParams.width = newWidth;
        layoutParams.height = newHeight;
        view.setLayoutParams(layoutParams);
    }
}
