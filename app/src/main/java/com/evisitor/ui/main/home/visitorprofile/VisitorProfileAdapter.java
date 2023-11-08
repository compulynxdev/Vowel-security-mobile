package com.evisitor.ui.main.home.visitorprofile;

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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.evisitor.EVisitor;
import com.evisitor.R;
import com.evisitor.data.DataManager;
import com.evisitor.data.model.CommercialStaffResponse;
import com.evisitor.data.model.CommercialVisitorResponse;
import com.evisitor.data.model.Guests;
import com.evisitor.data.model.HouseKeepingResponse;
import com.evisitor.data.model.ServiceProvider;
import com.evisitor.data.model.VisitorProfileBean;
import com.evisitor.ui.base.BaseViewHolder;
import com.evisitor.util.AppUtils;

import java.util.List;

public class VisitorProfileAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private final List<VisitorProfileBean> list;
    private final Activity activity;
    private EditText et_data;

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

    public void setNumberPlate(String s) {
        et_data.setText(s.toUpperCase());
        setNumberPlateData(s);
    }

    public class ItemViewHolder extends BaseViewHolder {

        final TextView tv_name;

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

        final TextView tv_title;
        EditableViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            et_data = itemView.findViewById(R.id.et_data);
        }

        @Override
        public void onBind(int position) {
            VisitorProfileBean bean = list.get(position);

           // DataManager dataManager = EVisitor.getInstanceM().getDataManager();
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
                   setNumberPlateData(s.toString());
                }
            });
        }
    }

    private void setNumberPlateData(String s) {
        DataManager dataManager= EVisitor.getInstance().getDataManager();
        if (dataManager.isCommercial()) {
            CommercialVisitorResponse.CommercialGuest guests = dataManager.getCommercialVisitorDetail();
            if (guests != null) {
                guests.setEnteredVehicleNo(et_data.getText().toString());
                dataManager.setCommercialVisitorDetail(guests);
            }

            CommercialStaffResponse.ContentBean staff = dataManager.getCommercialStaff();
            if (staff != null) {
                staff.setEnteredVehicleNo(et_data.getText().toString());
                dataManager.setCommercialStaff(staff);
            }

        } else {
            Guests guests = dataManager.getGuestDetail();
            if (guests != null) {
                guests.setEnteredVehicleNo(et_data.getText().toString());
                dataManager.setGuestDetail(guests);
            }

            HouseKeepingResponse.ContentBean hkBean = dataManager.getHouseKeeping();
            if (hkBean != null) {
                hkBean.setEnteredVehicleNo(et_data.getText().toString());
                dataManager.setHouseKeeping(hkBean);
            }
        }

        ServiceProvider spBean = dataManager.getSpDetail();
        if (spBean != null) {
            spBean.setEnteredVehicleNo(et_data.getText().toString());
            dataManager.setSPDetail(spBean);
        }
    }

    public class DaysViewHolder extends BaseViewHolder {

        final TextView tv_name;
        final LinearLayout ll_days;
        final ConstraintLayout constraint_main;

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
                tv_days.setBackground(ContextCompat.getDrawable(activity, day.equals("sun") || day.equals("sat") ?
                        R.drawable.bg_circle_red : R.drawable.bg_circle_primary));
                ll_days.addView(tv_days);
            }
        }
    }
}
