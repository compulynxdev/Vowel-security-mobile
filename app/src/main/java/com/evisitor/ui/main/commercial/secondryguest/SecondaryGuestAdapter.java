package com.evisitor.ui.main.commercial.secondryguest;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.evisitor.EVisitor;
import com.evisitor.R;
import com.evisitor.data.model.CommercialStaffResponse;
import com.evisitor.data.model.CommercialVisitorResponse;
import com.evisitor.data.model.GuestConfigurationResponse;
import com.evisitor.data.model.Guests;
import com.evisitor.data.model.HouseKeepingResponse;
import com.evisitor.data.model.IdentityBean;
import com.evisitor.data.model.SecondaryGuest;
import com.evisitor.data.model.ServiceProvider;
import com.evisitor.ui.base.BaseViewHolder;
import com.evisitor.ui.dialog.country.CountrySelectionDialog;
import com.evisitor.ui.dialog.selection.SelectionBottomSheetDialog;
import com.evisitor.util.AppLogger;
import com.evisitor.util.CommonUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

//add guest
//check in
//list display
public class SecondaryGuestAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private final List<SecondaryGuest> list;
    private final AdapterCallback callback;
    private boolean isAdd;
    private boolean isCheckIn;
    private boolean checkInApproved;
    String idType;
    private final FragmentManager fragmentManager;
    List<IdentityBean> identityTypeList = new ArrayList<>();

    private List<IdentityBean> getIdentityTypeList() {
        if (identityTypeList.isEmpty()) {
            identityTypeList.add(new IdentityBean("National ID", "nationalId"));
            identityTypeList.add(new IdentityBean("Driving Licence", "dl"));
            identityTypeList.add(new IdentityBean("Passport", "passport"));
        }
        return identityTypeList;
    }

    SecondaryGuestAdapter(List<SecondaryGuest> list, AdapterCallback callback, FragmentManager fragmentManager) {
        this.list = list;
        this.callback = callback;
        this.fragmentManager = fragmentManager;
    }

    void setIsAdd(boolean isAdd) {
        this.isAdd = isAdd;
    }

    void setIsCheckIn(boolean isCheckIn) {
        this.isCheckIn = isCheckIn;
    }

    void checkInApproved(boolean approved) {
        this.checkInApproved = approved;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.family_member_layout, parent, false);
        return new SecondaryGuestAdapter.ViewHolder(view);
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
        void onChangeList(List<SecondaryGuest> deviceList);

        void onRemove(int position);

        void onCheckInSelected(int guestIds);

        void onCheckInDeselected(int index);

        void onCheckInTemperature(int index, String temperature);
    }

    public class ViewHolder extends BaseViewHolder {

        EditText etFullname, etDocumentId, etContactNo, etAddress, etTemperature;
        TextView tvIdentityType, tvDialingCode;
        View view1;
        CheckBox cb_minor, cb_checkIn;
        final ImageView img_close;
        LinearLayout llNumber;
        RelativeLayout rlCode;
        View line;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            etFullname = itemView.findViewById(R.id.et_fullname);
            etDocumentId = itemView.findViewById(R.id.et_document_id);
            etContactNo = itemView.findViewById(R.id.et_contact);
            etAddress = itemView.findViewById(R.id.et_address);
            img_close = itemView.findViewById(R.id.img_close);
            tvIdentityType = itemView.findViewById(R.id.tv_identity);
            view1 = itemView.findViewById(R.id.view1);
            cb_minor = itemView.findViewById(R.id.minor_checkbox);
            llNumber = itemView.findViewById(R.id.ll_number);
            cb_checkIn = itemView.findViewById(R.id.checkin_checkbox);
            tvDialingCode = itemView.findViewById(R.id.tv_code);
            rlCode = itemView.findViewById(R.id.rl_code);
            line = itemView.findViewById(R.id.view2);
            etTemperature = itemView.findViewById(R.id.et_temperature);



            updateUi();

            tvIdentityType.setOnClickListener(v -> SelectionBottomSheetDialog.newInstance(tvIdentityType.getContext().getString(R.string.select_identity_type), getIdentityTypeList()).setItemSelectedListener(pos -> {
                IdentityBean bean = getIdentityTypeList().get(pos);
                tvIdentityType.setText(bean.getTitle());
                idType = bean.getKey();
                list.get(getAdapterPosition()).setDocumentType(idType);
                callback.onChangeList(list);
            }).show(fragmentManager));

            rlCode.setOnClickListener(v -> CountrySelectionDialog.newInstance(true, countryResponse -> {
                String countryCode = countryResponse.getDial_code();
                tvDialingCode.setText("+ ".concat(countryCode));
                list.get(getAdapterPosition()).setDialingCode(countryCode);
                callback.onChangeList(list);
            }).show(fragmentManager));

            cb_checkIn.setVisibility(isCheckIn ? View.VISIBLE : View.GONE);

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


            cb_minor.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (getAdapterPosition() != -1) {
                    if (cb_minor.isChecked()) {
                        etDocumentId.setVisibility(View.GONE);
                        tvIdentityType.setVisibility(View.GONE);
                        view1.setVisibility(View.GONE);
                        llNumber.setVisibility(View.GONE);
                        etAddress.setVisibility(View.GONE);
                        list.get(getAdapterPosition()).setDocumentId("");
                        list.get(getAdapterPosition()).setDocumentType("");
                        list.get(getAdapterPosition()).setDialingCode("");
                        list.get(getAdapterPosition()).setContactNo("");
                        list.get(getAdapterPosition()).setAddress("");
                    } else {
                       updateUi();
                    }
                    list.get(getAdapterPosition()).setMinor(isChecked);
                    callback.onChangeList(list);
                }
            });

            cb_checkIn.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    if (getAdapterPosition() != -1)
                        callback.onCheckInSelected(list.get(getAdapterPosition()).getId());
                } else {
                    if (getAdapterPosition() != -1) {
                        callback.onCheckInDeselected(list.get(getAdapterPosition()).getId());
                    }
                }
            });

            etTemperature.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (getAdapterPosition() != -1) {
                        if (!s.toString().isEmpty() && s.toString().length() > 1) {
                            if (Double.parseDouble(s.toString()) >= 34 && Double.parseDouble(s.toString()) <= 40) {
                                if (isCheckIn)
                                    callback.onCheckInTemperature(list.get(getAdapterPosition()).getId(), s.toString().trim());

                                else {
                                    list.get(getAdapterPosition()).setBodyTemperature(s.toString());
                                    callback.onChangeList(list);
                                }
                            } else {
                                etTemperature.setText("");
                                Toast.makeText(etTemperature.getContext(), R.string.temperature_should_be_30, Toast.LENGTH_LONG).show();
                            }

                        }

                    }
                }
            });
        }

        /**using configuration the ui will show of hide the fields**/
        private void updateUi() {
            GuestConfigurationResponse configurationResponse = EVisitor.getInstance().getDataManager().getGuestConfiguration();

            etFullname.setVisibility(configurationResponse.getSecGuestField().isSecFullname() ? View.VISIBLE : View.GONE);
            etDocumentId.setVisibility(configurationResponse.getSecGuestField().isSecDocumentID() ? View.VISIBLE : View.GONE);
            tvIdentityType.setVisibility(configurationResponse.getSecGuestField().isSecDocumentID() ? View.VISIBLE : View.GONE);
            view1.setVisibility(configurationResponse.getSecGuestField().isSecDocumentID() ? View.VISIBLE : View.GONE);
            llNumber.setVisibility(configurationResponse.getSecGuestField().isSecContactNo() ? View.VISIBLE : View.GONE);
            etAddress.setVisibility(configurationResponse.getSecGuestField().isSecAddress() ? View.VISIBLE : View.GONE);
        }

        @Override
        public void onBind(int position) {
            SecondaryGuest bean = list.get(position);
            if (isAdd) {
                etFullname.setText(bean.getFullName().isEmpty() ? "" : bean.getFullName());
                etDocumentId.setText(bean.getDocumentId().isEmpty() ? "" : bean.getDocumentId());
                etContactNo.setText(bean.getContactNo().isEmpty() ? "" : bean.getContactNo());
                tvDialingCode.setText(bean.getDialingCode().isEmpty() ? "" : "+ " + bean.getDialingCode());
                etAddress.setText(bean.getAddress().isEmpty() ? "" : bean.getAddress());
                etTemperature.setText(bean.getBodyTemperature().isEmpty() ? "" : bean.getBodyTemperature());
                if (bean.getDocumentType() != null && !bean.getDocumentType().isEmpty()) {
                    for (int i = 0; i < getIdentityTypeList().size(); i++) {
                        IdentityBean identityBean = identityTypeList.get(i);
                        if (identityBean.getKey().equalsIgnoreCase(bean.getDocumentType()) || identityBean.getTitle().equalsIgnoreCase(bean.getDocumentType())) {
                            tvIdentityType.setText(identityBean.getTitle());
                        }

                    }
                }
            } else {
                etFullname.setText(etFullname.getContext().getString(R.string.data_name, bean.getFullName().isEmpty() ? "N/A" : bean.getFullName()));
                if (!bean.getDocumentId().isEmpty()) {
                    etDocumentId.setVisibility(View.VISIBLE);
                    etDocumentId.setText(etDocumentId.getContext().getString(R.string.data_identity, bean.getDocumentId().isEmpty() ? "N/A" : CommonUtils.paritalEncodeData(bean.getDocumentId())));
                } else etDocumentId.setVisibility(View.GONE);
                if (!bean.getContactNo().isEmpty()) {
                    llNumber.setVisibility(View.VISIBLE);
                    etContactNo.setText(bean.getContactNo().isEmpty() ? "N/A" : CommonUtils.paritalEncodeData(bean.getContactNo()));
                } else llNumber.setVisibility(View.GONE);
                tvDialingCode.setText(bean.getDialingCode().isEmpty() ? "" : "+ " + bean.getDialingCode());
                if (!bean.getAddress().isEmpty()) {
                    etAddress.setVisibility(View.VISIBLE);
                    etAddress.setText(etFullname.getContext().getString(R.string.address).concat(" : ").concat(bean.getAddress().isEmpty() ? "N/A" : bean.getAddress()));
                } else etAddress.setVisibility(View.GONE);
                if (bean.getDocumentType() != null && !bean.getDocumentType().isEmpty()) {
                    tvIdentityType.setVisibility(View.VISIBLE);
                    view1.setVisibility(View.VISIBLE);
                    for (int i = 0; i < getIdentityTypeList().size(); i++) {
                        IdentityBean identityBean = identityTypeList.get(i);
                        if (identityBean.getKey().equalsIgnoreCase(bean.getDocumentType()) || identityBean.getTitle().equalsIgnoreCase(bean.getDocumentType())) {
                            tvIdentityType.setText(etFullname.getContext().getString(R.string.data_identity_type, bean.getDocumentType().isEmpty() ? "N/A" : identityBean.getTitle()));
                        }
                    }
                } else {
                    tvIdentityType.setVisibility(View.GONE);
                    view1.setVisibility(View.GONE);
                }
                try {
                    if (checkInApproved && bean.getBodyTemperature().isEmpty()) {
                        etTemperature.setVisibility(View.GONE);
                    } else {
                        etTemperature.setText(bean.getBodyTemperature());
                    }

                } catch (Exception e) {
                    AppLogger.d("temperature : ", e.toString());
                }

            }
            cb_minor.setChecked(bean.isMinor());

            if (isAdd) {
                etFullname.setEnabled(true);
                etDocumentId.setEnabled(true);
                etContactNo.setEnabled(true);
                rlCode.setEnabled(true);
                etAddress.setEnabled(true);
                img_close.setVisibility(View.VISIBLE);
                cb_minor.setEnabled(true);
                tvIdentityType.setText("");
            } else {
                etFullname.setEnabled(false);
                etDocumentId.setEnabled(false);
                etContactNo.setEnabled(false);
                rlCode.setEnabled(false);
                etAddress.setEnabled(false);
                img_close.setVisibility(View.GONE);
                cb_minor.setEnabled(false);
            }

            etTemperature.setEnabled(isAdd || isCheckIn);
            if (checkInApproved) {
                etTemperature.setEnabled(false);
                etTemperature.setCursorVisible(false);
            }
        }
    }
}
