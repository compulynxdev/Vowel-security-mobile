package com.evisitor.ui.main.activity.checkin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evisitor.R;
import com.evisitor.data.model.HouseKeeping;
import com.evisitor.ui.base.BaseViewHolder;
import com.evisitor.util.CalenderUtils;
import com.evisitor.util.pagination.FooterLoader;

import java.util.List;

public class HouseKeepingCheckInAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private List<HouseKeeping> list;
    private static final int VIEWTYPE_ITEM = 1;
    private static final int VIEWTYPE_LOADER =2 ;
    private boolean showLoader;
    private Context context;
    private OnItemClickListener listener;

    public HouseKeepingCheckInAdapter(List<HouseKeeping> list, Context context, OnItemClickListener click) {
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
                return new HouseKeepingCheckInAdapter.ViewHolder(view);

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

            itemView.findViewById(R.id.constraint).setOnClickListener(v -> {
                if (listener!=null)
                    listener.ItemClick(list.get(getAdapterPosition()));
            });

        }

        @Override
        public void onBind(int position) {
            HouseKeeping bean = list.get(position);
            name.setText(context.getString(R.string.data_name,bean.getName()));
            time.setText(context.getString(R.string.data_time_in,CalenderUtils.formatDate(bean.getCheckInTime(),CalenderUtils.SERVER_DATE_FORMAT,CalenderUtils.TIME_FORMAT_AM)));
            if (!bean.getHouseNo().isEmpty()) {
                houseNo.setVisibility(View.VISIBLE);
                houseNo.setText(context.getString(R.string.data_house, bean.getHouseNo()));
                host.setText(context.getString(R.string.data_host,bean.getHost()));

            }
            else{
                houseNo.setVisibility(View.GONE);
                houseNo.setText("");
                host.setText(context.getString(R.string.data_host,bean.getCreatedBy()));

            }
        }
    }

    public interface OnItemClickListener{
        void ItemClick(HouseKeeping houseKeeping);
    }
}

