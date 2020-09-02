package com.evisitor.ui.main.home.housekeeping.expected;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evisitor.R;
import com.evisitor.data.model.RegisteredHKResponse;
import com.evisitor.ui.base.BaseViewHolder;
import com.evisitor.ui.base.ItemClickCallback;
import com.evisitor.util.CalenderUtils;
import com.evisitor.util.pagination.FooterLoader;

import java.util.List;

public class ExpectedHKAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final int VIEWTYPE_ITEM = 1;
    private static final int VIEWTYPE_LOADER = 2;
    private List<RegisteredHKResponse.ContentBean> list;
    private boolean showLoader;
    private ItemClickCallback listener;

    ExpectedHKAdapter(List<RegisteredHKResponse.ContentBean> list, ItemClickCallback callback) {
        this.list = list;
        this.listener = callback;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case VIEWTYPE_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hk, parent, false);
                return new ExpectedHKAdapter.ViewHolder(view);

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
        ImageView imgVisitor;
        TextView name, identity, profile, time, houseNo, host;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            identity = itemView.findViewById(R.id.tv_identity);
            profile = itemView.findViewById(R.id.tv_profile);
            time = itemView.findViewById(R.id.tv_time);
            houseNo = itemView.findViewById(R.id.tv_house_no);
            host = itemView.findViewById(R.id.tv_host);
            imgVisitor = itemView.findViewById(R.id.img_visitor);
            itemView.findViewById(R.id.constraint).setOnClickListener(v -> {
                if (listener != null)
                    listener.onItemClick(getAdapterPosition());
            });
        }

        @Override
        public void onBind(int position) {
            RegisteredHKResponse.ContentBean bean = list.get(position);
            Context context = name.getContext();
            name.setText(context.getString(R.string.data_name, bean.getFullName()));
            identity.setText(context.getString(R.string.data_identity, bean.getDocumentId().isEmpty() ? "N?A" : bean.getDocumentId()));
            profile.setText(context.getString(R.string.data_profile, bean.getProfile()));
            if (bean.getFlatNo().isEmpty()) {
                houseNo.setVisibility(View.GONE);
                host.setText(context.getString(R.string.data_host, bean.getCreatedBy()));
            } else {
                houseNo.setVisibility(View.VISIBLE);
                houseNo.setText(context.getString(R.string.data_house, bean.getFlatNo()));
                host.setText(context.getString(R.string.data_host, bean.getResidentName()));
            }
            time.setText(context.getString(R.string.data_time_slot, CalenderUtils.formatDate(bean.getTimeIn(), CalenderUtils.TIME_FORMAT, CalenderUtils.TIME_FORMAT_AM), CalenderUtils.formatDate(bean.getTimeOut(), CalenderUtils.TIME_FORMAT, CalenderUtils.TIME_FORMAT_AM)));
        }
    }

}
