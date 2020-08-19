package com.evisitor.ui.main.home.guest.add.dialogs;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.databinding.DialogPickerBinding;
import com.evisitor.ui.base.BaseBottomSheetDialog;

import java.util.List;

public class PickerBottomSheetDialog extends BaseBottomSheetDialog<DialogPickerBinding, PickerViewModel> {

    private static final String TAG = "PickerBottomSheetDialog";
    private OnItemSelectedListener itemSelectedListener;
    private List list;

    public static PickerBottomSheetDialog newInstance(List list) {

        Bundle args = new Bundle();

        PickerBottomSheetDialog fragment = new PickerBottomSheetDialog();
        fragment.setArguments(args);
        fragment.setData(list);
        return fragment;
    }

    private void setData(List list) {
        this.list = list;
    }

    public PickerBottomSheetDialog setItemSelectedListener(OnItemSelectedListener itemSelectedListener) {
        this.itemSelectedListener = itemSelectedListener;
        return this;
    }

    @Override
    public int getBindingVariable() {
        return com.evisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_picker;
    }

    @Override
    public PickerViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstance()).get(PickerViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getViewDataBinding().loopWheelView.setOnItemSelectedListener((picker, data, position) -> {
            if (itemSelectedListener != null) {
                itemSelectedListener.setOnItemSelectedListener(position);
            }
        });

        getViewDataBinding().loopWheelView.setData(list);
    }

    public void show(FragmentManager fragmentManager) {
        super.show(fragmentManager, TAG);
    }
}


