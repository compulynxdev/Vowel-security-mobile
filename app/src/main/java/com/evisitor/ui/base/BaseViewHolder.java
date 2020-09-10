package com.evisitor.ui.base;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evisitor.EVisitor;

public abstract class BaseViewHolder extends RecyclerView.ViewHolder {
    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public String getImageUrl(String image) {
        return EVisitor.getInstance().getDataManager().getImageBaseURL().concat(image);
    }

    public abstract void onBind(int position);
}
