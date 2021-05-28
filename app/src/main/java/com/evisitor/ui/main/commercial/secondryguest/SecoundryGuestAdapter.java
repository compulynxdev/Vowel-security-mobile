package com.evisitor.ui.main.commercial.secondryguest;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.evisitor.EVisitor;
import com.evisitor.R;
import com.evisitor.data.model.GuestConfigurationResponse;
import com.evisitor.data.model.IdentityBean;
import com.evisitor.data.model.SecoundryGuest;
import com.evisitor.ui.base.BaseActivity;
import com.evisitor.ui.base.BaseViewHolder;
import com.evisitor.ui.dialog.selection.SelectionBottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public class SecoundryGuestAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private final List<SecoundryGuest> list;
    private final AdapterCallback callback;
    private boolean isAdd;
    String idType;
    FragmentManager fragmentManager;
    List<IdentityBean> identityTypeList = new ArrayList<>();

    List getIdentityTypeList() {
        if (identityTypeList.isEmpty()) {
            identityTypeList.add(new IdentityBean("National ID", "nationalId"));
            identityTypeList.add(new IdentityBean("Driving Licence", "dl"));
            identityTypeList.add(new IdentityBean("Passport", "passport"));
        }
        return identityTypeList;
    }

    SecoundryGuestAdapter(List<SecoundryGuest> list, AdapterCallback callback, FragmentManager fragmentManager) {
        this.list = list;
        this.callback = callback;
        this.fragmentManager = fragmentManager;
    }

    void setIsAdd(boolean isAdd) {
        this.isAdd = isAdd;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.family_member_layout, parent, false);
        return new SecoundryGuestAdapter.ViewHolder(view);
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
        void onChangeList(List<SecoundryGuest> deviceList);

        void onRemove(int position);
    }

    public class ViewHolder extends BaseViewHolder {

        final TextView tv_title, etFullname, etDocumentId, etContactNo, etAddress, tvIdentityType;
        View view1;
        final ImageView img_close;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_fullname);
            etFullname = itemView.findViewById(R.id.et_fullname);
            etDocumentId = itemView.findViewById(R.id.et_document_id);
            etContactNo = itemView.findViewById(R.id.et_contact_no);
            etAddress = itemView.findViewById(R.id.et_address);
            img_close = itemView.findViewById(R.id.img_close);
            tvIdentityType = itemView.findViewById(R.id.tv_identity);
            view1 = itemView.findViewById(R.id.view1);

            GuestConfigurationResponse configurationResponse = EVisitor.getInstance().getDataManager().getGuestConfiguration();

            etFullname.setVisibility(configurationResponse.getSecGuestField().isSecFullname() ? View.VISIBLE : View.GONE);
            etDocumentId.setVisibility(configurationResponse.getSecGuestField().isSecDocumentID() ? View.VISIBLE : View.GONE);
            tvIdentityType.setVisibility(configurationResponse.getSecGuestField().isSecDocumentID() ? View.VISIBLE : View.GONE);
            view1.setVisibility(configurationResponse.getSecGuestField().isSecDocumentID() ? View.VISIBLE : View.GONE);
            etContactNo.setVisibility(configurationResponse.getSecGuestField().isSecContactNo() ? View.VISIBLE : View.GONE);
            etAddress.setVisibility(configurationResponse.getSecGuestField().isSecAddress() ? View.VISIBLE : View.GONE);

            tvIdentityType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SelectionBottomSheetDialog.newInstance(etContactNo.getContext().getString(R.string.select_identity_type), getIdentityTypeList()).setItemSelectedListener(pos -> {
                        IdentityBean bean = (IdentityBean) getIdentityTypeList().get(pos);
                        tvIdentityType.setText(bean.getTitle());
                        idType = bean.getKey();
                        list.get(getAdapterPosition()).setDocumentType(idType);
                        callback.onChangeList(list);
                    }).show(fragmentManager);
                }
            });


            etFullname.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (getAdapterPosition() != -1) {
                        list.get(getAdapterPosition()).setFullName(s.toString());
                        callback.onChangeList(list);
                    }
                }
            });

            etDocumentId.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (getAdapterPosition() != -1) {
                        list.get(getAdapterPosition()).setDocumentId(s.toString());
                        callback.onChangeList(list);
                    }
                }
            });

            etContactNo.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (getAdapterPosition() != -1) {
                        list.get(getAdapterPosition()).setContactNo(s.toString());
                        callback.onChangeList(list);
                    }
                }
            });

            etAddress.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (getAdapterPosition() != -1) {
                        list.get(getAdapterPosition()).setAddress(s.toString());
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
            SecoundryGuest bean = list.get(position);

            //tv_title.setText(bean.getCount() != null && bean.getCount().isEmpty() ? tv_title.getContext().getString(R.string.device).concat(" ").concat(String.valueOf(position + 1)) : bean.getCount());
            etFullname.setText(bean.getFullName().isEmpty() ? "" : bean.getFullName());
            etDocumentId.setText(bean.getDocumentId().isEmpty() ? "" : bean.getDocumentId());
            etContactNo.setText(bean.getContactNo().isEmpty() ? "" : bean.getContactNo());
            etAddress.setText(bean.getAddress().isEmpty() ? "" : bean.getAddress());
            if (bean.getDocumentType() != null && !bean.getDocumentType().isEmpty()) {
                for (int i = 0; i < getIdentityTypeList().size(); i++) {
                    IdentityBean identityBean = identityTypeList.get(i);
                    if (identityBean.getKey().equalsIgnoreCase(bean.getDocumentType()) || identityBean.getTitle().equalsIgnoreCase(bean.getDocumentType())) {
                        tvIdentityType.setText(identityBean.getTitle());
                    }

                }

            }

            if (isAdd) {
                etFullname.setEnabled(true);
                etDocumentId.setEnabled(true);
                etContactNo.setEnabled(true);
                etAddress.setEnabled(true);
                img_close.setVisibility(View.VISIBLE);
            } else {
                etFullname.setEnabled(false);
                etDocumentId.setEnabled(false);
                etContactNo.setEnabled(false);
                etAddress.setEnabled(false);
                img_close.setVisibility(View.GONE);
            }
        }
    }
}
