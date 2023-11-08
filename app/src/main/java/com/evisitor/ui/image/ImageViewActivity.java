package com.evisitor.ui.image;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.databinding.ActivityImageViewBinding;
import com.evisitor.ui.base.BaseActivity;

import java.util.Objects;

public class ImageViewActivity extends BaseActivity<ActivityImageViewBinding, ImageViewModel> {

    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.0f;

    public static Intent newIntent(Context context) {
        return new Intent(context, ImageViewActivity.class);
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
            String imageUrl = Objects.requireNonNull(intent.getStringExtra("IMAGE_VIEW_URL"));
            if (imageUrl.isEmpty()) {
                Glide.with(this)
                        .load(R.drawable.ic_person)
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(getViewDataBinding().imageView);
            } else {
                Glide.with(this)
                        .load(mViewModel.getDataManager().getImageBaseURL().concat(imageUrl))
                        .fitCenter()
                        .placeholder(R.drawable.ic_person)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(getViewDataBinding().imageView);
            }
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
