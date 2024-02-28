package com.evisitor.ui.image;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.databinding.ActivityImageViewBinding;
import com.evisitor.ui.base.BaseActivity;

import androidx.lifecycle.ViewModelProvider;

public class BitmapImageViewActivity extends BaseActivity<ActivityImageViewBinding, ImageViewModel> {

    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.0f;

    public static Intent newIntent(Context context) {
        return new Intent(context, BitmapImageViewActivity.class);
    }

    @Override
    public int getBindingVariable() {
        return com.evisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_image_view;
    }

    @Override
    public ImageViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstanceM()).get(ImageViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel.setNavigator(this);
        getViewDataBinding().imgBack.setOnClickListener(v -> onBackPressed());
        mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
        updateUI(getIntent());
    }

    private void updateUI(Intent intent) {
        if (intent.hasExtra("IMAGE_VIEW_URL")) {
            byte[] byteArray = getIntent().getByteArrayExtra("IMAGE_VIEW_URL");
            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            getViewDataBinding().imageView.setImageBitmap(bmp);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        mScaleGestureDetector.onTouchEvent(motionEvent);
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        updateUI(intent);
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f,
                    Math.min(mScaleFactor, 10.0f));
            getViewDataBinding().imageView.setScaleX(mScaleFactor);
            getViewDataBinding().imageView.setScaleY(mScaleFactor);
            return true;
        }
    }
}
