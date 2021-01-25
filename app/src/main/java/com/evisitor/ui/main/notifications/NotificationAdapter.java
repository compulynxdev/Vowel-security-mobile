package com.evisitor.ui.main.notifications;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evisitor.EVisitor;
import com.evisitor.R;
import com.evisitor.data.model.NotificationResponse;
import com.evisitor.ui.base.BaseViewHolder;
import com.evisitor.util.AppConstants;
import com.evisitor.util.AppUtils;
import com.evisitor.util.CalenderUtils;
import com.evisitor.util.CommonUtils;
import com.evisitor.util.pagination.FooterLoader;

import java.util.Date;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final int VIEWTYPE_ITEM = 1;
    private static final int VIEWTYPE_LOADER = 2;
    private final List<NotificationResponse.ContentBean> notificationList;
    private boolean showLoader;

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

    private void updateCompanyUI(TextView tvCompany, NotificationResponse.ContentBean notification) {
        switch (notification.getType().toUpperCase()) {
            case AppConstants.SERVICE_PROVIDER:
            case AppConstants.STAFF:
                tvCompany.setVisibility(View.VISIBLE);
                tvCompany.setText(tvCompany.getContext().getString(R.string.data_comp_name, notification.getCompanyName().isEmpty() ? tvCompany.getContext().getString(R.string.na) : notification.getCompanyName()));
                break;

            default:
                tvCompany.setVisibility(View.GONE);
                break;
        }
    }

    public class ViewHolder extends BaseViewHolder {
        final TextView tvTitle, tvTime, tvMsg, tvType, tvCompany;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvType = itemView.findViewById(R.id.tv_type);
            tvMsg = itemView.findViewById(R.id.tv_msg);
            tvCompany = itemView.findViewById(R.id.tv_company);
            if (EVisitor.getInstance().getDataManager().isCommercial())
                tvType.setVisibility(View.GONE);
            else tvType.setVisibility(View.VISIBLE);
        }

        @Override
        public void onBind(int position) {
            NotificationResponse.ContentBean notification = notificationList.get(position);
            tvTitle.setText(notification.getFullName());
            Date date = CalenderUtils.getDateFormat(notification.getCreatedDate(), CalenderUtils.SERVER_DATE_FORMAT2);
            tvTime.setText(DateUtils.getRelativeTimeSpanString(date == null ? new Date().getTime() : date.getTime()));
            if (EVisitor.getInstance().getDataManager().isCommercial())
                tvMsg.setText(tvMsg.getContext().getString(R.string.msg_notification, AppUtils.capitaliseFirstLetter(notification.getNotificationStatus()), notification.getStaffName()));
            else
                tvMsg.setText(tvMsg.getContext().getString(R.string.msg_notification, AppUtils.capitaliseFirstLetter(notification.getNotificationStatus()), notification.getResidentName()));
            tvType.setText(tvType.getContext().getString(R.string.data_type, CommonUtils.getVisitorType(notification.getType())));
            updateCompanyUI(tvCompany, notification);
        }
    }
}
