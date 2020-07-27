package com.evisitor.ui.main.notifications;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evisitor.R;
import com.evisitor.data.model.Notifications;
import com.evisitor.ui.base.BaseViewHolder;
import com.evisitor.util.pagination.FooterLoader;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final int VIEWTYPE_ITEM = 1;
    private static final int VIEWTYPE_LOADER =2 ;
    private boolean showLoader;
    private List<Notifications> notifications;

    NotificationAdapter(List<Notifications> text) {
        this.notifications = text;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            default:
            case VIEWTYPE_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
                return new NotificationAdapter.ViewHolder(view);

            case VIEWTYPE_LOADER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pagination_item_loader, parent, false);
                return new FooterLoader(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == notifications.size()-1) {
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
        return notifications.size();
    }

    public class ViewHolder extends BaseViewHolder {
        TextView tvTitle,tvTime,tvMsg;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvMsg = itemView.findViewById(R.id.tv_msg);
        }

        @Override
        public void onBind(int position) {
            Notifications notification = notifications.get(position);
            tvTitle.setText(notification.getTitle());
            tvTime.setText(notification.getTime());
            tvMsg.setText(notification.getMsg());
        }
    }
}
