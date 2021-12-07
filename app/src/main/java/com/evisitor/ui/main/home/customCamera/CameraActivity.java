package com.evisitor.ui.main.home.customCamera;

import androidx.annotation.NonNull;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.databinding.ActivityCameraBinding;
import com.evisitor.ui.base.BaseActivity;
import com.evisitor.util.PermissionUtils;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

public class CameraActivity extends BaseActivity<ActivityCameraBinding, CameraActivityViewModel> implements CameraActivityNavigator {
    private PreviewView previewView;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private ImageCapture imageCapture;
    private Button button;
    private ProcessCameraProvider cameraProvider;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, CameraActivity.class);
    }

    @Override
    public int getBindingVariable() {
        return com.evisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_camera;
    }

    @Override
    public CameraActivityViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstance()).get(CameraActivityViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel.setNavigator(this);

        previewView = getViewDataBinding().previewView;

        button = getViewDataBinding().buttonca;

        initCameraX();

        button.setOnClickListener(view -> captureImage());
    }

    private void initCameraX() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();
                startCamerax();
            } catch (ExecutionException | InterruptedException e) {
                // No errors need to be handled for this Future.
                // This should never be reached.
            }
        }, cameraExecutor());
    }

    private Executor cameraExecutor() {
        return ContextCompat.getMainExecutor(this);
    }


    private void startCamerax() {
        cameraProvider.unbindAll();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK).build();

        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .setTargetAspectRatio(AspectRatio.RATIO_16_9)
                .build();


        cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);
    }


    private void captureImage() {
        if (!PermissionUtils.haveStoragePermission(CameraActivity.this)) {
            PermissionUtils.requestStoragePermission(CameraActivity.this);
        }
        String nameFile = "e-visitor" + System.currentTimeMillis() + ".jpg";
        File file_dir = new File(getContext().getFilesDir(), nameFile);


        ImageCapture.OutputFileOptions outputFileOptions =
                new ImageCapture.OutputFileOptions.Builder(file_dir).build();

        imageCapture.takePicture(outputFileOptions, cameraExecutor(),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        Toast.makeText(CameraActivity.this, "saved", Toast.LENGTH_LONG).show();
                        read_Image(file_dir, nameFile);
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException error) {
                        // insert your code here.
                        Toast.makeText(CameraActivity.this, error.getCause().toString(), Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    private void read_Image(File file_dir, String file_name) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(file_dir.getAbsolutePath(), options);

        mViewModel.documentMrzExtraction(bitmap, file_name, new MRZCallback() {
            @Override
            public void onMrzSuccess() {

            }

            @Override
            public void OnError() {

            }
        });
    }

}
