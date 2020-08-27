package com.evisitor.ui.main.activity.checkout.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evisitor.R;
import com.evisitor.data.model.ServiceProvider;
import com.evisitor.ui.base.BaseViewHolder;
import com.evisitor.util.CalenderUtils;
import com.evisitor.util.pagination.FooterLoader;

import java.util.List;

public class ServiceProviderCheckOutAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private List<ServiceProvider> list;
    private static final int VIEWTYPE_ITEM = 1;
    private static final int VIEWTYPE_LOADER =2 ;
    private boolean showLoader;
    private Context context;

    public ServiceProviderCheckOutAdapter(List<ServiceProvider> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case VIEWTYPE_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_check_in_out, parent, false);
                return new ServiceProviderCheckOutAdapter.ViewHolder(view);

            default:
            case VIEWTYPE_LOADER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pagination_item_loader, parent, false);
                return new FooterLoader(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == list.size()-1) {
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
        TextView name,time,houseNo,host,visitorType;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            time = itemView.findViewById(R.id.tv_time);
            houseNo = itemView.findViewById(R.id.tv_house_no);
            host = itemView.findViewById(R.id.tv_host);
            visitorType = itemView.findViewById(R.id.tv_type);
            imgVisitor = itemView.findViewById(R.id.img_visitor);
        }

        @Override
        public void onBind(int position) {
            ServiceProvider bean = list.get(position);
            name.setText(context.getString(R.string.data_name,bean.getName()));
            time.setText(context.getString(R.string.data_time_out,CalenderUtils.formatDate(bean.getCheckOutTime(),CalenderUtils.SERVER_DATE_FORMAT,CalenderUtils.TIME_FORMAT)));
            if (!bean.getHouseNo().isEmpty())
                houseNo.setText(context.getString(R.string.data_house,bean.getName()));
            else houseNo.setText("");
            host.setText(context.getString(R.string.data_host,bean.getHost().isEmpty() ? bean.getCreatedBy() : bean.getHost()));
        }
    }
}

