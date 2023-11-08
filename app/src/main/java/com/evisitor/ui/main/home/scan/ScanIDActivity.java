package com.evisitor.ui.main.home.scan;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.databinding.ActivityScanIdBinding;
import com.evisitor.ui.base.BaseActivity;
import com.evisitor.util.AppLogger;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.sharma.mrzreader.MrzParser;
import com.sharma.mrzreader.MrzRecord;

import java.io.IOException;

public class ScanIDActivity extends BaseActivity<ActivityScanIdBinding, ScanIDViewModel> {

    private static final String TAG = "ScanIDActivity";
    private final int RequestCameraPermissionID = 1001;
    private final Handler handler = new Handler();
    private boolean isDataParsed = false;
    private CameraSource cameraSource;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, ScanIDActivity.class);
    }

    private static String getLastLines(String string, int numLines) {
        StringBuilder mrzCode = new StringBuilder();
        String[] lines = string.split("\n");
        int startPos = Math.max(0, lines.length - numLines);
        if (lines[startPos].contains("<")) {
            for (int i = startPos; i < lines.length; i++) {
                //here replace method remove white space
                mrzCode.append(lines[i].replaceAll("\\s+", "")).append("\n");
            }
        }
        return mrzCode.toString();
    }

    @Override
    public int getBindingVariable() {
        return com.evisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_scan_id;
    }

    @Override
    public ScanIDViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstanceM()).get(ScanIDViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel.setNavigator(this);
        TextView tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(R.string.title_scan_id);
        ImageView imgBack = findViewById(R.id.img_back);
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setOnClickListener(v -> onBackPressed());

        setUp();
    }

    private void setUp() {
        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        if (!textRecognizer.isOperational()) {
            // Note: The first time that an app using a Vision API is installed on a
            // device, GMS will download a native libraries to the device in order to do detection.
            // Usually this completes before the app is run for the first time.  But if that
            // download has not yet completed, then the above call will not detect any text,
            // barcodes, or faces.
            //
            // isOperational() can be used to check if the required native libraries are currently
            // available.  The detectors will automatically become operational once the library
            // downloads complete on device.
            Log.w(TAG, "Detector dependencies are not yet available.");
            showAlert(R.string.alert, R.string.ocr_error);
            // Check for low storage.  If there is low storage, the native library will not be
            // downloaded, so detection will not become operational.
           /* IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
            boolean hasLowStorage = registerReceiver(null, lowstorageFilter) != null;

            if (hasLowStorage) {
                Toast.makeText(this, R.string.low_storage_error, Toast.LENGTH_LONG).show();
                Log.w(TAG, getString(R.string.low_storage_error));
            }*/
        } else {

            cameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setRequestedFps(2.0f)
                    .setAutoFocusEnabled(true)
                    .build();

            getViewDataBinding().surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {

                    try {
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(ScanIDActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    RequestCameraPermissionID);
                            return;
                        }
                        cameraSource.start(getViewDataBinding().surfaceView.getHolder());
                    } catch (IOException e) {
                        AppLogger.w(TAG, e.toString());
                    }
                }

                @Override
                public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
                }

                @Override
                public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
                    cameraSource.stop();
                }
            });

            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {
                }

                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {

                    final SparseArray<TextBlock> items = detections.getDetectedItems();
                    if (items.size() != 0) {
                        handler.post(() -> {
                            StringBuilder stringBuilder = new StringBuilder();
                            int size = items.size();
                            for (int i = 0; i < size; ++i) {
                                TextBlock item = items.valueAt(i);
                                stringBuilder.append(item.getValue());
                                stringBuilder.append("\n");
                            }

                            // get the index of last new line character
                            String mrz = getLastLines(stringBuilder.toString(), 3);
                            if (mrz.isEmpty()) mrz = getLastLines(stringBuilder.toString(), 2);
                            Log.e("MRZ", mrz);
                            parseMrz(mrz);
                        });
                    }
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == RequestCameraPermissionID) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                try {
                    cameraSource.start(getViewDataBinding().surfaceView.getHolder());
                } catch (IOException e) {
                    AppLogger.w(TAG, e.toString());
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isDataParsed = false;
    }

    private void parseMrz(String mrz) {
        try {
            final MrzRecord record = MrzParser.Companion.parse(mrz);
            if (!isDataParsed) {
                isDataParsed = true;

                Intent intent = getIntent();
                intent.putExtra("Record", record);
                setResult(RESULT_OK, intent);
                finish();
            }
        } catch (Exception e) {
            Log.w(TAG, e.toString());
           /* if (ex instanceof MrzParseException) {
                final MrzParseException mpe = (MrzParseException) ex;
                final MrzRange r = mpe.getRange();
                Log.e("MRX", r.toString());
                //mpe..select(toPos(r.column, r.row, m), toPos(r.columnTo, r.row, m));
            }*/
        }
    }

}
