package xyz.ratapp.munion.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;

import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dewarder.camerabutton.CameraButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
        CameraButton.OnTapEventListener,
        Camera.PictureCallback,
        Camera.PreviewCallback,
        Camera.AutoFocusCallback {

    private static final String PHOTO_RESULT = "xyz.ratapp.muinion.camera.result";

    private static final String ARGS_TEXT = "camera_args_text";
    private static final String ARGS_IMAGE_MASK = "camera_args_image";
    private static final String ARGS_WIDTH = "camera_args_width";
    private static final String ARGS_HEIGHT = "camera_args_height";

    private Boolean supportAutofocus;

    private Camera camera;
    private SurfaceHolder surfaceHolder;
    private SurfaceView preview;
    private CameraButton shotBtn;
    private TextView cameraText;
    private ImageButton backButton;
    private ImageView cameraDocumentFrame;
    private View loading;


    private Button acceptPhoto;
    private Button cancelPhoto;
    private int srcImageWidth;
    private int srcImageHeight;

    public static Intent newIntent(Context context, String text,
                                   @DrawableRes int mask, int width, int height) {
        Intent i = new Intent(context, CameraActivity.class);

        i.putExtra(ARGS_TEXT, text);
        i.putExtra(ARGS_IMAGE_MASK, mask);
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

        String argsText = getIntent().getStringExtra(ARGS_TEXT);

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
        shotBtn.setMode(CameraButton.Mode.TAP);
        shotBtn.setOnTapEventListener(this);

        cameraText = findViewById(R.id.camera_text);
        backButton = findViewById(R.id.camera_back);

        backButton.setOnClickListener(v -> onBackPressed());

        cameraText.setText(argsText);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onResume() {
        super.onResume();
        camera = Camera.open();
        Camera.Parameters parameters = camera.getParameters();
        Camera.Size size = parameters.getPictureSize();
        //===
        //Даже не спрашивайте, я не знаю почему они так приходят о.о
        srcImageHeight = size.width;
        srcImageWidth = size.height;
        //===
        parameters.setPictureSize(size.width, size.height);
        parameters.set("orientation", "portrait");
        parameters.setRotation(90);
        camera.setDisplayOrientation(90);
        camera.setParameters(parameters);

        List<String> focusModes = parameters.getSupportedFocusModes();
        supportAutofocus = focusModes != null &&
                focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO);

        if(supportAutofocus) {
            camera.autoFocus(null);
        }

        int argsMask = getIntent().getIntExtra(ARGS_IMAGE_MASK, -1);
        Integer argsWidth = getIntent().getIntExtra(ARGS_WIDTH, 200);
        Integer argsHeight = getIntent().getIntExtra(ARGS_HEIGHT, 300);

        cameraDocumentFrame = findViewById(R.id.camera_document_frame);
        resizeView(cameraDocumentFrame, size, argsWidth, argsHeight);
        cameraDocumentFrame.setImageResource(argsMask);
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
    public void surfaceChanged(SurfaceHolder holder,
                               int format, int width, int height) {

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
    public void onTap() {
        if(supportAutofocus) {
            camera.autoFocus(this);
        }
        else {
            camera.takePicture(null, null, null, this);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.camera_frame_layout &&
                camera != null) {
            if (supportAutofocus) {
                camera.autoFocus(null);
            }
        }
    }

    @Override
    public void onPictureTaken(final byte[] paramArrayOfByte,
                               final Camera paramCamera) {
        camera.stopPreview();

        acceptPhoto.setVisibility(View.VISIBLE);
        cancelPhoto.setVisibility(View.VISIBLE);

        backButton.setVisibility(View.GONE);
        cameraText.setVisibility(View.GONE);
        //cameraDocumentFrame.setVisibility(View.GONE);
        cameraDocumentFrame.setImageDrawable(null);
        cameraDocumentFrame.setBackground(null);
        shotBtn.setVisibility(View.GONE);

        findViewById(R.id.v_bottom_decor).setAlpha(1);
        findViewById(R.id.v_top_decor).setAlpha(1);
        findViewById(R.id.v_left_decor).setAlpha(1);
        findViewById(R.id.v_right_decor).setAlpha(1);


        acceptPhoto.setOnClickListener(view -> {
            savePhoto(paramArrayOfByte);
        });

        cancelPhoto.setOnClickListener(view -> {
            acceptPhoto.setVisibility(View.GONE);
            cancelPhoto.setVisibility(View.GONE);

            backButton.setVisibility(View.VISIBLE);
            cameraText.setVisibility(View.VISIBLE);
            cameraDocumentFrame.setVisibility(View.VISIBLE);
            shotBtn.setVisibility(View.VISIBLE);

            findViewById(R.id.v_bottom_decor).setAlpha(0.3f);
            findViewById(R.id.v_top_decor).setAlpha(0.3f);
            findViewById(R.id.v_left_decor).setAlpha(0.3f);
            findViewById(R.id.v_right_decor).setAlpha(0.3f);

            paramCamera.startPreview();
        });

    }

    private void setResultUri(Uri uri){
        Intent data = new Intent();
        data.putExtra(PHOTO_RESULT, uri);
        setResult(RESULT_OK, data);
    }

    private void savePhoto(byte[] data) {
        final Bitmap bitmap = getCroppedBitmap(data);
        loading.setVisibility(View.VISIBLE);
        acceptPhoto.setVisibility(View.GONE);
        cancelPhoto.setVisibility(View.GONE);

        new Thread(() -> {
            Uri uri;

            if (bitmap != null) {

                File file = new File(Environment.getExternalStorageDirectory() + "/MUnion");
                if (!file.isDirectory()) {
                    file.mkdir();
                }

                int argsMask = getIntent().getIntExtra(ARGS_IMAGE_MASK, -1);
                String fileName = CameraActivity.this.getResources().getResourceName(argsMask);
                fileName = fileName.substring(fileName.lastIndexOf('/'));
                file = new File(Environment.getExternalStorageDirectory() + "/MUnion", fileName + ".png");

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

                setResultUri(uri);
                finish();
            }
            else {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }

        }).start();
    }

    private Bitmap getCroppedBitmap(byte[] data) {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int sWidth = size.x; //s means screen
        int sHeight = size.y;

        int x = (int) ((cameraDocumentFrame.getLeft() / (float) sWidth) * srcImageWidth);
        int y = ((int) ((cameraDocumentFrame.getTop() / (float) sHeight) * srcImageHeight));
        int width = ((int) ((cameraDocumentFrame.getWidth() / (float) sWidth) * srcImageWidth));
        int height = ((int) ((cameraDocumentFrame.getHeight() / (float) sHeight) * srcImageHeight));

        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        bitmap = Bitmap.createBitmap(bitmap, y, x, height, width);

        return bitmap;
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


    private void resizeView(View view, Camera.Size cSize, int newWidth, int newHeight) {
        int padding = getResources().getDimensionPixelOffset(R.dimen.camera_document_offset);

        Display display = getWindowManager().getDefaultDisplay();
        Point sSize = new Point();
        display.getSize(sSize);
        int sWidth = sSize.x - padding; //s means screen
        int sHeight = sSize.y - padding;

        int cWidth = srcImageWidth; //c means camera
        int cHeight = srcImageHeight;
        float widthDesity = sWidth / ((float) cWidth);
        float heightDesity = sHeight / ((float) cHeight);
        int multiplier = Math.min(sWidth / newWidth,
                sHeight / newHeight);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        layoutParams.width = ((int) (newWidth * multiplier * widthDesity));
        layoutParams.height = ((int) (newHeight * multiplier * heightDesity));
        view.setLayoutParams(layoutParams);
    }
}
