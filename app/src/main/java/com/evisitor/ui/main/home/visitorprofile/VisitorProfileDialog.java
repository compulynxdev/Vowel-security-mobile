package com.evisitor.ui.main.home.visitorprofile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.data.model.CommercialVisitorResponse;
import com.evisitor.data.model.DeviceBean;
import com.evisitor.data.model.VisitorProfileBean;
import com.evisitor.databinding.DialogVisitorProfileBinding;
import com.evisitor.ui.base.BaseDialog;
import com.evisitor.ui.main.commercial.gadgets.GadgetsInputActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

import static com.evisitor.util.AppConstants.SCAN_RESULT;

public class VisitorProfileDialog extends BaseDialog<DialogVisitorProfileBinding, VisitorProfileViewModel> implements View.OnClickListener {
    private static final String TAG = "IdVerificationDialog";
    private List<DeviceBean> deviceBeanList;
    private VisitorProfileCallback callback;
    private List<VisitorProfileBean> visitorInfoList;
    private String btnLabel = "";
    private String image = "", documentImage = "";
    private boolean isBtnVisible = true;
    private boolean isCommercialGuest = false;

    public static VisitorProfileDialog newInstance(List<VisitorProfileBean> visitorInfoList, VisitorProfileCallback callback) {
        Bundle args = new Bundle();
        VisitorProfileDialog fragment = new VisitorProfileDialog();
        fragment.setArguments(args);
        fragment.setOnClick(callback);
        fragment.setData(visitorInfoList);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel.setNavigator(getBaseActivity());
    }

    private void setData(List<VisitorProfileBean> visitorInfoList) {
        this.visitorInfoList = visitorInfoList;
    }

    @Override
    public int getBindingVariable() {
        return com.evisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_visitor_profile;
    }

    public VisitorProfileDialog setBtnLabel(String btnLabel) {
        this.btnLabel = btnLabel;
        return this;
    }

    public VisitorProfileDialog setBtnVisible(boolean btnVisible) {
        isBtnVisible = btnVisible;
        return this;
    }

    public VisitorProfileDialog setImage(String image) {
        this.image = image;
        return this;
    }

    public VisitorProfileDialog setDocumentImage(String documentImage) {
        this.documentImage = documentImage;
        return this;
    }
    public VisitorProfileDialog setIsCommercialGuest(boolean isTrue) {
        this.isCommercialGuest = isTrue;
        return this;
    }

    @Override
    public VisitorProfileViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstance()).get(VisitorProfileViewModel.class);
    }

    private void setOnClick(VisitorProfileCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getViewDataBinding().imgClose.setOnClickListener(this);
        getViewDataBinding().btnOk.setVisibility(isBtnVisible ? View.VISIBLE : View.GONE);
        getViewDataBinding().btnOk.setOnClickListener(this);
        getViewDataBinding().imgProfile.setOnClickListener(this);
        getViewDataBinding().tvDocumentImage.setOnClickListener(this);
        if (!btnLabel.isEmpty()) {
            getViewDataBinding().btnOk.setText(btnLabel);
        }

        if (image.isEmpty()) {
            Glide.with(this)
                    .load(R.drawable.ic_person)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(getViewDataBinding().imgProfile);
        } else {
            Glide.with(this)
                    .load(mViewModel.getDataManager().getImageBaseURL().concat(image))
                    .centerCrop()
                    .placeholder(R.drawable.ic_person)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(getViewDataBinding().imgProfile);
        }
        setUpAdapter();

        if (isCommercialGuest && mViewModel.getDataManager().isCommercial()) {
            CommercialVisitorResponse.CommercialGuest guest = mViewModel.getDataManager().getCommercialVisitorDetail();
            if (guest != null) {
                deviceBeanList = guest.getDeviceBeanList();
                if (deviceBeanList != null && !deviceBeanList.isEmpty()) {
                    getViewDataBinding().tvGadgetsInfo.setVisibility(View.VISIBLE);
                    getViewDataBinding().tvGadgetsInfo.setText(getString(R.string.view_gadgets_info).concat(" : ").concat(String.valueOf(deviceBeanList.size())));
                    getViewDataBinding().tvGadgetsInfo.setOnClickListener(this);
                } else if (btnLabel.equalsIgnoreCase(getString(R.string.check_in))) {
                    getViewDataBinding().tvGadgetsInfo.setVisibility(View.VISIBLE);
                    getViewDataBinding().tvGadgetsInfo.setText(getString(R.string.add_gadgets_info));
                    getViewDataBinding().tvGadgetsInfo.setOnClickListener(this);
                } else {
                    getViewDataBinding().tvGadgetsInfo.setVisibility(View.GONE);
                }
            } else {
                getViewDataBinding().tvGadgetsInfo.setVisibility(View.GONE);
            }
        } else {
            getViewDataBinding().tvGadgetsInfo.setVisibility(View.GONE);
        }

        if (!documentImage.isEmpty()) {
            getViewDataBinding().tvDocumentImage.setVisibility(View.VISIBLE);
        } else getViewDataBinding().tvDocumentImage.setVisibility(View.GONE);

    }

    private void setUpAdapter() {
        VisitorProfileAdapter infoAdapter = new VisitorProfileAdapter(getBaseActivity(), visitorInfoList);
        getViewDataBinding().recyclerView.setAdapter(infoAdapter);
    }

    public void show(FragmentManager fragmentManager) {
        super.show(fragmentManager);
    }

    @Override
    public void onClick(View v) {
        hideKeyboard();
        switch (v.getId()) {
            case R.id.btn_ok:
                if (callback != null) callback.onOkayClick(this);
                else dismiss();
                break;

            case R.id.img_close:
                dismiss();
                break;

            case R.id.img_profile:
                showFullImage(image);
                break;


            case R.id.tv_document_image:
                showFullImage(documentImage);
                break;

            case R.id.tv_gadgets_info:
                Intent i = GadgetsInputActivity.getStartIntent(getContext());
                if (!deviceBeanList.isEmpty())
                    i.putExtra("list", new Gson().toJson(deviceBeanList));
                i.putExtra("add", btnLabel.equalsIgnoreCase(getString(R.string.check_in)));
                startActivityForResult(i, SCAN_RESULT);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SCAN_RESULT && data != null) {
            Type listType = new TypeToken<List<DeviceBean>>() {
            }.getType();
            deviceBeanList.clear();
            deviceBeanList.addAll(Objects.requireNonNull(mViewModel.getDataManager().getGson().fromJson(data.getStringExtra("data"), listType)));
            CommercialVisitorResponse.CommercialGuest guest = mViewModel.getDataManager().getCommercialVisitorDetail();
            guest.setDeviceBeanList(deviceBeanList);
            mViewModel.getDataManager().setCommercialVisitorDetail(guest);
            getViewDataBinding().tvGadgetsInfo.setText(getString(R.string.view_gadgets_info).concat(" : ").concat(String.valueOf(deviceBeanList.size())));
        }
    }
}
