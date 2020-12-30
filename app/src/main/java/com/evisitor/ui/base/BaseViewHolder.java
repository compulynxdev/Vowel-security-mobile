package com.evisitor.ui.base;

import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evisitor.EVisitor;
import com.evisitor.ui.image.ImageViewActivity;
import com.evisitor.util.AppConstants;

public abstract class BaseViewHolder extends RecyclerView.ViewHolder {
    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public String getImageBaseUrl(String image) {
        return EVisitor.getInstance().getDataManager().getImageBaseURL().concat(image);
    }

    public void showFullImage(String imageUrl) {
        Intent intent = ImageViewActivity.newIntent(itemView.getContext());
        intent.putExtra(AppConstants.IMAGE_VIEW_URL, imageUrl);
        itemView.getContext().startActivity(intent);
    }

    public boolean isCommercial() {
        return EVisitor.getInstance().getDataManager().isCommercial();
    }

    protected String getPremiseLastLevel() {
        return EVisitor.getInstance().getDataManager().getLevelName();
    }

    public abstract void onBind(int position);
}
