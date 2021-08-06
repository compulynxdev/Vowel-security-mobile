package com.evisitor.ui.main.home.rejected.staff;

import android.content.Context;
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
import com.evisitor.data.model.HouseKeepingResponse;
import com.evisitor.ui.base.BaseViewHolder;
import com.evisitor.ui.base.ItemClickCallback;
import com.evisitor.util.CommonUtils;
import com.evisitor.util.pagination.FooterLoader;

import java.util.List;

public class RejectedStaffAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final int VIEWTYPE_ITEM = 1;
    private static final int VIEWTYPE_LOADER = 2;
    private final List<HouseKeepingResponse.ContentBean> list;
    private final ItemClickCallback callback;
    private boolean showLoader;

    RejectedStaffAdapter(List<HouseKeepingResponse.ContentBean> list, ItemClickCallback callback) {
        this.list = list;
        this.callback = callback;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case VIEWTYPE_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hk, parent, false);
                return new RejectedStaffAdapter.ViewHolder(view);

            default:
            case VIEWTYPE_LOADER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pagination_item_loader, parent, false);
                return new FooterLoader(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == list.size() - 1) {
            return showLoader ? VIEWTYPE_LOADER : VIEWTYPE_ITEM;
        }
        return VIEWTYPE_ITEM;
    }

    public void showLoading(boolean status) {
        showLoader = status;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        if (holder instanceof FooterLoader) {
            FooterLoader loaderViewHolder = (FooterLoader) holder;
            if (showLoader) {
                loaderViewHolder.mProgressBar.setVisibility(View.VISIBLE);
            } else {
                loaderViewHolder.mProgressBar.setVisibility(View.GONE);
            }
            return;
        }
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends BaseViewHolder {
        final ImageView imgVisitor;
        final TextView name, identity, profile, time, houseNo, host, reject;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            identity = itemView.findViewById(R.id.tv_identity);
            profile = itemView.findViewById(R.id.tv_profile);
            time = itemView.findViewById(R.id.tv_time);
            houseNo = itemView.findViewById(R.id.tv_house_no);
            host = itemView.findViewById(R.id.tv_host);
            reject = itemView.findViewById(R.id.tv_status);

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
            HouseKeepingResponse.ContentBean bean = list.get(position);
            Context context = name.getContext();
            name.setText(context.getString(R.string.data_name, bean.getFullName()));
            identity.setText(context.getString(R.string.data_identity, bean.getDocumentId().isEmpty() ? "N?A" : CommonUtils.paritalEncodeData(bean.getDocumentId())));
            profile.setText(context.getString(R.string.data_profile, bean.getProfile()));
            reject.setVisibility(bean.getRejectedBy().isEmpty() ? View.GONE : View.VISIBLE);
            reject.setText(context.getString(R.string.rejected_by, bean.getRejectedBy()));

            /*if (bean.getRejectedOn().isEmpty()) {
                time.setVisibility(View.GONE);
            } else {
                time.setVisibility(View.VISIBLE);
                time.setText(time.getContext().getString(R.string.rejected_on, CalenderUtils.formatDate(bean.getRejectedOn(), CalenderUtils.SERVER_DATE_FORMAT, CalenderUtils.TIMESTAMP_FORMAT)));
            }*/

            time.setVisibility(View.GONE);

            if (isCommercial()) {
                host.setVisibility(View.GONE);
                houseNo.setVisibility(View.GONE);
            } else {
                //For Residential System
                if (bean.getPremiseName().isEmpty()) {
                    houseNo.setVisibility(View.GONE);
                    host.setText(context.getString(R.string.data_host, bean.getCreatedBy()));
                } else {
                    houseNo.setVisibility(View.VISIBLE);
                    houseNo.setText(context.getString(R.string.data_dynamic_premise, getPremiseLastLevel(), bean.getPremiseName()));
                    host.setText(context.getString(R.string.data_host, bean.getResidentName()));
                }
            }

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
