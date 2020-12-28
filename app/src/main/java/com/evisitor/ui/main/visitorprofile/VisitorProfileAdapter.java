package com.evisitor.ui.main.visitorprofile;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.evisitor.EVisitor;
import com.evisitor.R;
import com.evisitor.data.DataManager;
import com.evisitor.data.model.CommercialGuestResponse;
import com.evisitor.data.model.Guests;
import com.evisitor.data.model.HouseKeepingResponse;
import com.evisitor.data.model.ServiceProvider;
import com.evisitor.data.model.VisitorProfileBean;
import com.evisitor.ui.base.BaseViewHolder;
import com.evisitor.util.AppUtils;

import java.util.List;

public class VisitorProfileAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private List<VisitorProfileBean> list;
    private Activity activity;

    VisitorProfileAdapter(Activity activity, List<VisitorProfileBean> list) {
        this.list = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VisitorProfileBean.VIEW_TYPE_DAYS:
                return new DaysViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_days, parent, false));

            case VisitorProfileBean.VIEW_TYPE_EDITABLE:
                return new EditableViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_editable, parent, false));

            case VisitorProfileBean.VIEW_TYPE_ITEM:
            default:
                return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_info, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getView_type();
    }

    public class ItemViewHolder extends BaseViewHolder {

        TextView tv_name;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
        }

        @Override
        public void onBind(int position) {
            VisitorProfileBean bean = list.get(position);

            tv_name.setText(bean.getTitle());
        }
    }

    public class EditableViewHolder extends BaseViewHolder {

        TextView tv_title;
        EditText et_data;

        EditableViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            et_data = itemView.findViewById(R.id.et_data);
        }

        @Override
        public void onBind(int position) {
            VisitorProfileBean bean = list.get(position);

            DataManager dataManager = EVisitor.getInstance().getDataManager();
            tv_title.setText(bean.getTitle());
            et_data.setText(bean.getValue());
            et_data.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (dataManager.isCommercial()) {
                        CommercialGuestResponse.CommercialGuest guests = dataManager.getCommercialGuestDetail();
                        if (guests != null) {
                            guests.setEnteredVehicleNo(et_data.getText().toString());
                            dataManager.setCommercialGuestDetail(guests);
                            return;
                        }
                    } else {
                        Guests guests = dataManager.getGuestDetail();
                        if (guests != null) {
                            guests.setEnteredVehicleNo(et_data.getText().toString());
                            dataManager.setGuestDetail(guests);
                            return;
                        }
                    }

                    ServiceProvider spBean = dataManager.getSpDetail();
                    if (spBean != null) {
                        spBean.setEnteredVehicleNo(et_data.getText().toString());
                        dataManager.setSPDetail(spBean);
                        return;
                    }

                    HouseKeepingResponse.ContentBean hkBean = dataManager.getHouseKeeping();
                    if (hkBean != null) {
                        hkBean.setEnteredVehicleNo(et_data.getText().toString());
                        dataManager.setHouseKeeping(hkBean);
                    }
                }
            });
        }
    }

    public class DaysViewHolder extends BaseViewHolder {

        TextView tv_name;
        LinearLayout ll_days;
        ConstraintLayout constraint_main;

        DaysViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            ll_days = itemView.findViewById(R.id.ll_days);
            constraint_main = itemView.findViewById(R.id.constraint_main);
        }

        @Override
        public void onBind(int position) {
            VisitorProfileBean bean = list.get(position);
            tv_name.setText(bean.getTitle());
            for (String day : bean.getDataList()) {
                TextView tv_days = (TextView) activity.getLayoutInflater().inflate(R.layout.item_day, constraint_main, false);
                tv_days.setText(AppUtils.capitaliseFirstLetter(day));
                tv_days.setBackground(activity.getResources().getDrawable(day.equals("sun") || day.equals("sat") ?
                        R.drawable.bg_circle_red : R.drawable.bg_circle_primary));
                ll_days.addView(tv_days);
            }
        }
    }
}
