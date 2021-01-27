package com.evisitor.ui.main.commercial.add.whomtomeet.staff;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.evisitor.R;
import com.evisitor.data.model.SelectCommercialStaffResponse;
import com.evisitor.ui.base.BaseViewHolder;
import com.evisitor.ui.base.ItemClickCallback;

import java.util.List;

public class SelectStaffAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private final List<SelectCommercialStaffResponse> list;
    private final ItemClickCallback callback;

    SelectStaffAdapter(List<SelectCommercialStaffResponse> list, ItemClickCallback callback) {
        this.list = list;
        this.callback = callback;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_commercial_staff, parent, false);
        return new SelectStaffAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends BaseViewHolder {
        final ImageView imgVisitor;
        final TextView name, profile;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            profile = itemView.findViewById(R.id.tv_profile);
            imgVisitor = itemView.findViewById(R.id.img_visitor);

            itemView.setOnClickListener(v -> {
                if (callback != null && getAdapterPosition() != -1)
                    callback.onItemClick(getAdapterPosition());
            });

            imgVisitor.setOnClickListener(v -> {
                if (getAdapterPosition() != -1)
                    showFullImage(list.get(getAdapterPosition()).getImageUrl());
            });
        }

        @Override
        public void onBind(int position) {
            SelectCommercialStaffResponse bean = list.get(position);
            name.setText(name.getContext().getString(R.string.data_name, bean.getFullName()));
            profile.setText(profile.getContext().getString(R.string.data_designation, bean.getProfile()));

            if (bean.getImageUrl().isEmpty()) {
                Glide.with(imgVisitor.getContext())
                        .load(R.drawable.ic_person)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imgVisitor);
            } else {
                Glide.with(imgVisitor.getContext())
                        .load(getImageBaseUrl(bean.getImageUrl()))
                        .centerCrop()
                        .placeholder(R.drawable.ic_person)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imgVisitor);
            }

        }
    }

}
