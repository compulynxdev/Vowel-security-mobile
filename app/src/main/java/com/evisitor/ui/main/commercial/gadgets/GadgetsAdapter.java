package com.evisitor.ui.main.commercial.gadgets;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evisitor.R;
import com.evisitor.data.model.DeviceBean;
import com.evisitor.ui.base.BaseViewHolder;

import java.util.List;

public class GadgetsAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private final List<DeviceBean> list;
    private final AdapterCallback callback;
    private boolean isAdd;

    GadgetsAdapter(List<DeviceBean> list, AdapterCallback callback) {
        this.list = list;
        this.callback = callback;
    }

    void setIsAdd(boolean isAdd) {
        this.isAdd = isAdd;
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

        void onRemove(int position);
    }

    public class ViewHolder extends BaseViewHolder {

        final TextView tv_title, etDeviceName, etDeviceType, etDeviceTag, etDeviceSerial, etDeviceManufacturer;
        final ImageView img_close;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_serial_no);
            etDeviceName = itemView.findViewById(R.id.et_dev_name);
            etDeviceType = itemView.findViewById(R.id.et_dev_type);
            etDeviceTag = itemView.findViewById(R.id.et_tag_no);
            etDeviceSerial = itemView.findViewById(R.id.et_serial_no);
            etDeviceManufacturer = itemView.findViewById(R.id.et_manufacturer);
            img_close = itemView.findViewById(R.id.img_close);

            etDeviceName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (getAdapterPosition() != -1) {
                        list.get(getAdapterPosition()).setDeviceName(s.toString());
                        callback.onChangeList(list);
                    }
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
                    if (getAdapterPosition() != -1) {
                        list.get(getAdapterPosition()).setType(s.toString());
                        callback.onChangeList(list);
                    }
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
                    if (getAdapterPosition() != -1) {
                        list.get(getAdapterPosition()).setSerialNo(s.toString());
                        callback.onChangeList(list);
                    }
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
                    if (getAdapterPosition() != -1) {
                        list.get(getAdapterPosition()).setTagNo(s.toString());
                        callback.onChangeList(list);
                    }
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
                    if (getAdapterPosition() != -1) {
                        list.get(getAdapterPosition()).setManufacturer(s.toString());
                        callback.onChangeList(list);
                    }
                }
            });

            img_close.setOnClickListener(v -> {
                if (getAdapterPosition() != -1) {
                    callback.onRemove(getAdapterPosition());
                }
            });
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

            if (isAdd) {
                etDeviceManufacturer.setEnabled(true);
                etDeviceName.setEnabled(true);
                etDeviceType.setEnabled(true);
                etDeviceTag.setEnabled(true);
                etDeviceSerial.setEnabled(true);
                img_close.setVisibility(View.VISIBLE);
            } else {
                etDeviceManufacturer.setEnabled(false);
                etDeviceName.setEnabled(false);
                etDeviceType.setEnabled(false);
                etDeviceTag.setEnabled(false);
                etDeviceSerial.setEnabled(false);
                img_close.setVisibility(View.GONE);
            }
        }
    }
}
