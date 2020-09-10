package com.evisitor.ui.main.home.trespasser;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evisitor.R;
import com.evisitor.data.model.TrespasserResponse;
import com.evisitor.ui.base.BaseViewHolder;
import com.evisitor.ui.base.ItemClickCallback;
import com.evisitor.util.CalenderUtils;
import com.evisitor.util.pagination.FooterLoader;

import java.util.List;

public class TrespasserAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private List<TrespasserResponse.ContentBean> list;
    private static final int VIEWTYPE_ITEM = 1;
    private static final int VIEWTYPE_LOADER =2 ;
    private ItemClickCallback callback;
    private boolean showLoader;

    public TrespasserAdapter(List<TrespasserResponse.ContentBean> list, ItemClickCallback callback) {
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
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trespasser_list, parent, false);
                return new TrespasserAdapter.ViewHolder(view);

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
        TextView name,docId, flat,contact, vehicleNo, checkInTime;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            docId = itemView.findViewById(R.id.tv_doc_id);
            flat = itemView.findViewById(R.id.tv_flat);
            contact = itemView.findViewById(R.id.tv_contact);
            vehicleNo = itemView.findViewById(R.id.tv_vehicle);
            checkInTime = itemView.findViewById(R.id.tv_check);
            imgVisitor = itemView.findViewById(R.id.img_visitor);

            itemView.setOnClickListener(v -> {
                if (callback != null && getAdapterPosition() != -1) {
                    callback.onItemClick(getAdapterPosition());
                }
            });
        }

        @Override
        public void onBind(int position) {
            TrespasserResponse.ContentBean bean = list.get(position);
            name.setText(name.getContext().getString(R.string.data_name,bean.getFullName()));
            if (bean.getDocumentId()!=null && !bean.getDocumentId().isEmpty()){
                docId.setVisibility(View.VISIBLE);
                docId.setText(docId.getContext().getString(R.string.data_identity,bean.getDocumentId()));
            }else docId.setVisibility(View.GONE);

            if (bean.getFlatNo()!=null && !bean.getFlatNo().isEmpty()){
                flat.setVisibility(View.VISIBLE);
                flat.setText(flat.getContext().getString(R.string.data_house,bean.getFlatNo()));
            }else flat.setVisibility(View.GONE);

            if (bean.getContactNo()!=null && !bean.getContactNo().isEmpty()){
                contact.setVisibility(View.VISIBLE);
                contact.setText(contact.getContext().getString(R.string.data_mobile,bean.getContactNo()));
            }else contact.setVisibility(View.GONE);

           /* if (bean.getEnteredVehicleNo()!=null && !bean.getEnteredVehicleNo().isEmpty()){
                vehicleNo.setVisibility(View.VISIBLE);
                vehicleNo.setText(vehicleNo.getContext().getString(R.string.data_vehicle,bean.getEnteredVehicleNo()));
            }else vehicleNo.setVisibility(View.GONE);*/

            if (bean.getCheckInTime()!=null && !bean.getCheckInTime().isEmpty()){
                checkInTime.setVisibility(View.VISIBLE);
                checkInTime.setText(checkInTime.getContext().getString(R.string.data_time_in, CalenderUtils.formatDate(bean.getCheckInTime(),CalenderUtils.SERVER_DATE_FORMAT,CalenderUtils.TIMESTAMP_FORMAT)));
            }else checkInTime.setVisibility(View.GONE);

        }
    }

}
