package com.evisitor.ui.main.notifications;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evisitor.R;
import com.evisitor.data.model.NotificationResponse;
import com.evisitor.ui.base.BaseViewHolder;
import com.evisitor.util.AppUtils;
import com.evisitor.util.CalenderUtils;
import com.evisitor.util.pagination.FooterLoader;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final int VIEWTYPE_ITEM = 1;
    private static final int VIEWTYPE_LOADER =2 ;
    private boolean showLoader;
    private List<NotificationResponse.ContentBean> notificationList;

    NotificationAdapter(List<NotificationResponse.ContentBean> notificationList) {
        this.notificationList = notificationList;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            default:
            case VIEWTYPE_ITEM:
                return new NotificationAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false));

            case VIEWTYPE_LOADER:
                return new FooterLoader(LayoutInflater.from(parent.getContext()).inflate(R.layout.pagination_item_loader, parent, false));
        }
    }

    @Override
    public long getItemId(int position) {
        return notificationList.get(position).getId();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == notificationList.size() - 1) {
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
        return notificationList.size();
    }

    public class ViewHolder extends BaseViewHolder {
        TextView tvTitle, tvTime, tvMsg;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvMsg = itemView.findViewById(R.id.tv_msg);
        }

        @Override
        public void onBind(int position) {
            NotificationResponse.ContentBean notification = notificationList.get(position);
            tvTitle.setText(notification.getFullName().concat(" (").concat(notification.getType()).concat(")"));
            tvTime.setText(CalenderUtils.formatDate(notification.getCreatedDate(), CalenderUtils.SERVER_DATE_FORMAT2, CalenderUtils.TIME_FORMAT_AM));
            tvMsg.setText(tvMsg.getContext().getString(R.string.msg_notification, AppUtils.capitaliseFirstLetter(notification.getNotificationStatus()), notification.getCreatedBy()));
        }
    }
}
