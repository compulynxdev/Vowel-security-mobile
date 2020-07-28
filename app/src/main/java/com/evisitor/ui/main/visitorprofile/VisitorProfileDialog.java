package com.evisitor.ui.main.visitorprofile;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.data.model.VisitorProfileBean;
import com.evisitor.databinding.DialogVisitorProfileBinding;
import com.evisitor.ui.base.BaseDialog;

import java.util.List;

public class VisitorProfileDialog extends BaseDialog<DialogVisitorProfileBinding, VisitorProfileViewModel> implements View.OnClickListener {
    private static final String TAG = "VisitorProfileDialog";

    private VisitorProfileCallback callback;
    private List<VisitorProfileBean> visitorInfoList;
    private String btnLabel = "";

    public static VisitorProfileDialog newInstance(List<VisitorProfileBean> visitorInfoList, VisitorProfileCallback callback) {
        Bundle args = new Bundle();
        VisitorProfileDialog fragment = new VisitorProfileDialog();
        fragment.setArguments(args);
        fragment.setOnClick(callback);
        fragment.setData(visitorInfoList);
        return fragment;
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
        getViewModel().setNavigator(getBaseActivity());
        getViewDataBinding().imgClose.setOnClickListener(this);
        getViewDataBinding().btnOk.setOnClickListener(this);

        if (!btnLabel.isEmpty()) {
            getViewDataBinding().btnOk.setText(btnLabel);
        }

        setUpAdapter();
    }

    private void setUpAdapter() {
        VisitorProfileAdapter infoAdapter = new VisitorProfileAdapter(visitorInfoList);
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
                break;

            case R.id.img_close:
                dismissDialog(TAG);
                break;
        }
    }
}
