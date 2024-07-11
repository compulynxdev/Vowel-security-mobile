package com.evisitor.ui.dialog;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.evisitor.BuildConfig;
import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.databinding.DialogImagePickerBinding;
import com.evisitor.ui.base.BaseBottomSheetDialog;
import com.evisitor.util.AppConstants;
import com.evisitor.util.AppLogger;
import com.evisitor.util.AppUtils;
import com.evisitor.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;


/**
 * Created by hemant
 * Date: 25/5/18
 * Time: 1:10 PM
 */

public class ImagePickBottomSheetDialog extends BaseBottomSheetDialog<DialogImagePickerBinding, ImagePickViewModel> implements View.OnClickListener {

    private static final String TAG = "ImagePickBottomSheetDialog";
    private static String imageData;

    private ImagePickCallback callback;
    private String mCurrentPhotoPath = "";

    public static ImagePickBottomSheetDialog newInstance(ImagePickCallback callback, String image) {
        ImagePickBottomSheetDialog fragment = new ImagePickBottomSheetDialog();
        fragment.setCallback(callback);
        Bundle bundle = new Bundle();
        imageData = image;
        fragment.setArguments(bundle);
        return fragment;
    }

    private void setCallback(ImagePickCallback callback) {
        this.callback = callback;
    }

    @Override
    public int getBindingVariable() {
        return com.evisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_image_picker;
    }

    @Override
    public ImagePickViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstanceM()).get(ImagePickViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getViewDataBinding().tvCancel.setOnClickListener(this);
        getViewDataBinding().tvGallery.setOnClickListener(this);
        getViewDataBinding().tvCamera.setOnClickListener(this);
        getViewDataBinding().tvView.setOnClickListener(this);
        if (!imageData.isEmpty())
            getViewDataBinding().tvView.setVisibility(View.VISIBLE);
    }

    public void show(FragmentManager fragmentManager) {
        super.show(fragmentManager, TAG);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvCancel) {
            dismiss();
        } else if (v.getId() == R.id.tvGallery) {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(photoPickerIntent, AppConstants.REQUEST_GALLERY);
        } else if (v.getId() == R.id.tvCamera) {
            dispatchTakePictureIntent();
        } else if (v.getId() == R.id.tvView) {
            dismiss();
            if (callback != null) {
                callback.onView();
            }
        }
    }

    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        try {

            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                // Error occurred while creating the File
                AppLogger.w(TAG, e.toString());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getBaseActivity(),
                        BuildConfig.APPLICATION_ID + ".provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, AppConstants.REQUEST_TAKE_PHOTO);
            }
        }catch (Exception e){
            Toast.makeText(requireContext(),R.string.applicaion_not_found,Toast.LENGTH_LONG).show();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        @SuppressLint("SimpleDateFormat")
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getBaseActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case AppConstants.REQUEST_GALLERY:
                    try {
                        Uri uri = data.getData();
                        if (uri != null) {
                            //Bitmap bitmap = MediaStore.Images.Media.getBitmap(getBaseActivity().getContentResolver(), uri);
                            Bitmap compressedBitmap = new Compressor(getBaseActivity())
                                    .setMaxWidth(400)//640
                                    .setMaxHeight(300)//480
                                    .setQuality(75)
                                    .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                            Environment.DIRECTORY_PICTURES).getAbsolutePath())
                                    .compressToBitmap(new File(Objects.requireNonNull(FileUtils.getPath(getBaseActivity(), uri))));

                            long bitmapSize = AppUtils.getBitmapSizeInMB(compressedBitmap);
                            AppLogger.e(TAG, "" + bitmapSize);
                            if (bitmapSize <= 2) {
                                callback.onImageReceived(compressedBitmap);
                            } else {
                                showToast(getString(R.string.alert_image_size, bitmapSize));
                                //showAlert(R.string.alert, getString(R.string.alert_image_size, bitmapSize));
                            }
                        } else {
                            showToast(getString(R.string.something_went_wrong));
                        }
                    } catch (OutOfMemoryError e) {
                        showToast(getString(R.string.alert_out_of_memory));
                        AppLogger.w(TAG, e.toString());
                    } catch (Exception e) {
                        showToast(getString(R.string.something_went_wrong));
                        AppLogger.w(TAG, e.toString());
                    }
                    break;

                case AppConstants.REQUEST_TAKE_PHOTO:
                    try {
                        if (!mCurrentPhotoPath.isEmpty()) {
                            //Bitmap bitmap = MediaStore.Images.Media.getBitmap(getBaseActivity().getContentResolver(), uri);
                            Bitmap compressedBitmap = new Compressor(getBaseActivity())
                                    .setMaxWidth(400)
                                    .setMaxHeight(300)
                                    .setQuality(75)
                                    .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                            Environment.DIRECTORY_PICTURES).getAbsolutePath())
                                    .compressToBitmap(new File(mCurrentPhotoPath));
                            long bitmapSize = AppUtils.getBitmapSizeInMB(compressedBitmap);
                            if (bitmapSize <= 2) {
                                callback.onImageReceived(compressedBitmap);
                            } else {
                                showToast(getString(R.string.alert_image_size, bitmapSize));
                            }
                        } else {
                            showToast(getString(R.string.something_went_wrong));
                        }

                    } catch (OutOfMemoryError e) {
                        showToast(getString(R.string.alert_out_of_memory));
                        AppLogger.w(TAG, e.toString());
                    } catch (Exception e) {
                        showToast(getString(R.string.something_went_wrong));
                        AppLogger.w(TAG, e.toString());
                    }
                    break;
            }
        }
        dismiss();
    }
}

