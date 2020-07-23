package com.evisitor.ui.activity.checkin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evisitor.R;
import com.evisitor.data.model.CheckInOut;
import com.evisitor.ui.base.BaseViewHolder;
import com.evisitor.util.pagination.FooterLoader;

import java.util.List;

public class CheckInAdpter extends RecyclerView.Adapter<BaseViewHolder> {
    private List<CheckInOut> list;
    private static final int VIEWTYPE_ITEM = 1;
    private static final int VIEWTYPE_LOADER =2 ;
    private boolean showLoader;
    private Context context;

    CheckInAdpter(List<CheckInOut> list, Context context) {
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
                return new CheckInAdpter.ViewHolder(view);

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
            CheckInOut bean = list.get(position);
            name.setText(context.getString(R.string.name).concat(" : ").concat(bean.getName()));
            time.setText(context.getString(R.string.time_in).concat(" : ").concat(bean.getTime()));
            houseNo.setText(context.getString(R.string.house_no).concat(" : ").concat(bean.getName()));
            host.setText(context.getString(R.string.host).concat(" : ").concat(bean.getHost()));
            visitorType.setText(context.getString(R.string.visitor_type).concat(" : ").concat(bean.getVisitorType()));

        }
    }
}
