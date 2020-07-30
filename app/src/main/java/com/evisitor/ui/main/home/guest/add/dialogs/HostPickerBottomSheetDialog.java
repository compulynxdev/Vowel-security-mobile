package com.evisitor.ui.main.home.guest.add.dialogs;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.data.model.HostDetailBean;
import com.evisitor.databinding.DialogHostPickerBinding;
import com.evisitor.ui.base.BaseBottomSheetDialog;

import java.util.List;

public class HostPickerBottomSheetDialog extends BaseBottomSheetDialog<DialogHostPickerBinding, HostPickerViewModel> {

    private static final String TAG = "HostPickerBottomSheetDi";

    private HostPickerCallback hostPickerCallback;
    private List<HostDetailBean> list;

    public static HostPickerBottomSheetDialog newInstance(List<HostDetailBean> list, HostPickerCallback hostPickerCallback) {
        Bundle args = new Bundle();

        HostPickerBottomSheetDialog fragment = new HostPickerBottomSheetDialog();
        fragment.setArguments(args);
        fragment.setDataListAndCallback(list, hostPickerCallback);
        return fragment;
    }

    private void setDataListAndCallback(List<HostDetailBean> list, HostPickerCallback hostPickerCallback) {
        this.list = list;
        this.hostPickerCallback = hostPickerCallback;
    }

    @Override
    public int getBindingVariable() {
        return com.evisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_host_picker;
    }

    @Override
    public HostPickerViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstance()).get(HostPickerViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getViewModel().setNavigator(getBaseActivity());

        if (list != null) {
            getViewDataBinding().loopWheelView.setData(list);
            getViewDataBinding().loopWheelView.setOnItemSelectedListener((picker, data, position) -> {
                if (hostPickerCallback != null) hostPickerCallback.onHostSelect(list.get(position));
            });
        }
    }

    public void show(FragmentManager fragmentManager) {
        super.show(fragmentManager, TAG);
    }

    public interface HostPickerCallback {
        void onHostSelect(HostDetailBean bean);
    }
}


