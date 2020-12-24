package com.evisitor.ui.main.home.visitor.gadgetsdialog;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evisitor.R;
import com.evisitor.data.model.DeviceBean;
import com.evisitor.ui.base.BaseViewHolder;

import java.util.List;

public class GadgetsAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private List<DeviceBean> list;
    private AdapterCallback callback;

    GadgetsAdapter(List<DeviceBean> list, AdapterCallback callback) {
        this.list = list;
        this.callback = callback;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gadgets, parent, false);
        return new GadgetsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public interface AdapterCallback {
        void onChangeList(List<DeviceBean> deviceList);
    }

    public class ViewHolder extends BaseViewHolder {

        TextView tv_title, etDeviceName, etDeviceType, etDeviceTag, etDeviceSerial, etDeviceManufacturer;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_serial_no);
            etDeviceName = itemView.findViewById(R.id.et_dev_name);
            etDeviceType = itemView.findViewById(R.id.et_dev_type);
            etDeviceTag = itemView.findViewById(R.id.et_tag_no);
            etDeviceSerial = itemView.findViewById(R.id.et_serial_no);
            etDeviceManufacturer = itemView.findViewById(R.id.et_manufacturer);
        }

        @Override
        public void onBind(int position) {
            DeviceBean bean = list.get(position);
            tv_title.setText(bean.getsNo().isEmpty() ? tv_title.getContext().getString(R.string.device).concat(" ").concat(String.valueOf(position + 1)) : bean.getsNo());
            etDeviceName.setText(bean.getDeviceName().isEmpty() ? "" : bean.getDeviceName());
            etDeviceType.setText(bean.getType().isEmpty() ? "" : bean.getType());
            etDeviceTag.setText(bean.getTagNo().isEmpty() ? "" : bean.getTagNo());
            etDeviceSerial.setText(bean.getSerialNo().isEmpty() ? "" : bean.getSerialNo());
            etDeviceManufacturer.setText(bean.getManufacturer().isEmpty() ? "" : bean.getManufacturer());
            etDeviceName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    bean.setDeviceName(s.toString());
                    list.set(position, bean);
                    callback.onChangeList(list);
                }
            });

            etDeviceType.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    bean.setType(s.toString());
                    list.set(position, bean);
                    callback.onChangeList(list);
                }
            });

            etDeviceSerial.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    bean.setSerialNo(s.toString());
                    list.set(position, bean);
                    callback.onChangeList(list);
                }
            });

            etDeviceTag.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    bean.setTagNo(s.toString());
                    list.set(position, bean);
                    callback.onChangeList(list);
                }
            });
            etDeviceManufacturer.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    bean.setManufacturer(s.toString());
                    list.set(position, bean);
                    callback.onChangeList(list);
                }
            });

        }
    }
}
