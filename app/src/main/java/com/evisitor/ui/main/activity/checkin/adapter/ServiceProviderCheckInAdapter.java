package com.evisitor.ui.main.activity.checkin.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.evisitor.EVisitor;
import com.evisitor.R;
import com.evisitor.data.model.ServiceProvider;
import com.evisitor.ui.base.BaseViewHolder;
import com.evisitor.util.CalenderUtils;
import com.evisitor.util.pagination.FooterLoader;

import java.util.List;

public class ServiceProviderCheckInAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final int VIEWTYPE_ITEM = 1;
    private static final int VIEWTYPE_LOADER = 2;
    private final List<ServiceProvider> list;
    private final Context context;
    private final OnItemClickListener listener;
    private boolean showLoader;

    public ServiceProviderCheckInAdapter(List<ServiceProvider> list, Context context, OnItemClickListener click) {
        this.list = list;
        this.listener = click;
        this.context = context;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case VIEWTYPE_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_check_in_out, parent, false);
                return new ServiceProviderCheckInAdapter.ViewHolder(view);

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


    public interface OnItemClickListener {
        void ItemClick(ServiceProvider serviceProvider);

        void printClick(ServiceProvider serviceProvider);
    }

    public class ViewHolder extends BaseViewHolder {

        final ImageView imgVisitor;
        final TextView name, time, houseNo, host, visitorType;
        final Button btn_print;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            time = itemView.findViewById(R.id.tv_time_in);
            houseNo = itemView.findViewById(R.id.tv_house_no);
            host = itemView.findViewById(R.id.tv_host);
            visitorType = itemView.findViewById(R.id.tv_type);
            imgVisitor = itemView.findViewById(R.id.img_visitor);
            btn_print = itemView.findViewById(R.id.btn_ok);

            if(EVisitor.getInstance().getDataManager().isCommercial() && EVisitor.getInstance().getDataManager().isPrintLabel()) {
                btn_print.setVisibility(View.VISIBLE);
            }

            itemView.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != -1)
                    listener.ItemClick(list.get(getAdapterPosition()));
            });

            imgVisitor.setOnClickListener(v -> {
                if (getAdapterPosition() != -1)
                    showFullImage(list.get(getAdapterPosition()).getImageUrl());
            });

            btn_print.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != -1)
                    listener.printClick(list.get(getAdapterPosition()));
            });

        }

        @Override
        public void onBind(int position) {
            ServiceProvider bean = list.get(position);
            name.setText(context.getString(R.string.data_name, bean.getName()));
            time.setText(context.getString(R.string.data_time_in, CalenderUtils.formatDate(bean.getCheckInTime(), CalenderUtils.SERVER_DATE_FORMAT, CalenderUtils.TIMESTAMP_FORMAT)));

            if (isCommercial()) {
                host.setVisibility(View.GONE);
                /*if (!bean.getPremiseName().isEmpty()) {
                    houseNo.setVisibility(View.VISIBLE);
                    houseNo.setText(context.getString(R.string.data_dynamic_premise, getPremiseLastLevel(), bean.getPremiseName()));
                } else*/
                houseNo.setVisibility(View.GONE);
            } else {
                if (!bean.getHouseNo().isEmpty()) {
                    houseNo.setVisibility(View.VISIBLE);
                    houseNo.setText(context.getString(R.string.data_dynamic_premise, getPremiseLastLevel(), bean.getPremiseName()));
                    host.setText(context.getString(R.string.data_host, bean.getHost()));
                } else {
                    houseNo.setVisibility(View.GONE);
                    houseNo.setText("");
                    host.setText(context.getString(R.string.data_host, bean.getCreatedBy()));
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

