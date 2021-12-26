package com.evisitor.ui.main.home.customCamera;

import androidx.annotation.NonNull;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.data.model.MrzResponse;
import com.evisitor.databinding.ActivityCameraBinding;
import com.evisitor.ui.base.BaseActivity;
import com.evisitor.util.AppLogger;
import com.evisitor.util.AppUtils;
import com.evisitor.util.PermissionUtils;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import okhttp3.ResponseBody;

public class CameraActivity extends BaseActivity<ActivityCameraBinding, CameraActivityViewModel> implements CameraActivityNavigator {
    private PreviewView previewView;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private ImageCapture imageCapture;
    private ProcessCameraProvider cameraProvider;
    private Bitmap imageCaptured;
    private String filename;

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
        setStatusBarColor(CameraActivity.this, R.color.black);

        PermissionUtils.checkCameraPermission(CameraActivity.this);

        previewView = getViewDataBinding().previewView;
        getViewDataBinding().buttonca.setOnClickListener(view -> captureImage());
        getViewDataBinding().requestBtn.setOnClickListener(view -> makeNetworkRequest(imageCaptured, filename));
        getViewDataBinding().retakeBtn.setOnClickListener(view -> retakeImage());

        initCameraX();
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

    private Bitmap getBitmap(ImageProxy image) {
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        buffer.rewind();
        byte[] bytes = new byte[buffer.capacity()];
        buffer.get(bytes);
        byte[] clonedBytes = bytes.clone();
        return BitmapFactory.decodeByteArray(clonedBytes, 0, clonedBytes.length);
    }

    private void captureImage() {

        if (!PermissionUtils.haveStoragePermission(CameraActivity.this)) {
            PermissionUtils.requestStoragePermission(CameraActivity.this);
        }

        filename = "e-visitor" + System.currentTimeMillis() + ".jpg";
        File file_dir = new File(getContext().getFilesDir(), filename);


        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(file_dir).build();

        imageCapture.takePicture(outputFileOptions, cameraExecutor(),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {

                        imageCaptured = AppUtils.readBitmapFileFromDir(file_dir);

                        getViewDataBinding().frameCamerax.setVisibility(View.GONE);
                        getViewDataBinding().overlayCamerax.setVisibility(View.VISIBLE);
                        getViewDataBinding().imageviewCamerax.setImageBitmap(imageCaptured);

                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException error) {
                        showAlert("camerax", error.toString());
                    }
                }
        );
    }



    private void makeNetworkRequest(Bitmap bitmap, String file_name) {
        getViewDataBinding().requestBtn.setClickable(false);
        mViewModel.documentMrzExtraction(bitmap, file_name);
    }

    private void retakeImage() {
        getViewDataBinding().frameCamerax.setVisibility(View.VISIBLE);
        getViewDataBinding().overlayCamerax.setVisibility(View.GONE);
    }

    @Override
    public void onMrzSuccess(MrzResponse mrzResponse) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result",new Gson().toJson(mrzResponse));
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    @Override
    public void OnError(ResponseBody responseBody) {
        retakeImage();
        showAlert(R.string.alert, responseBody.toString());
        getViewDataBinding().requestBtn.setClickable(true);
    }
}
