package com.evisitor.ui.main.home.guest.add.dialogs;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.databinding.DialogGenderPickerBinding;
import com.evisitor.ui.base.BaseBottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class GenderPickerBottomSheetDialog extends BaseBottomSheetDialog<DialogGenderPickerBinding, GenderPickerViewModel> {

    private static final String TAG = "GenderPickerBottomSheet";
    private OnItemSelectedListener itemSelectedListener;

    public static GenderPickerBottomSheetDialog newInstance() {

        Bundle args = new Bundle();

        GenderPickerBottomSheetDialog fragment = new GenderPickerBottomSheetDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public GenderPickerBottomSheetDialog setItemSelectedListener(OnItemSelectedListener itemSelectedListener) {
        this.itemSelectedListener = itemSelectedListener;
        return this;
    }

    @Override
    public int getBindingVariable() {
        return com.evisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_gender_picker;
    }

    @Override
    public GenderPickerViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstance()).get(GenderPickerViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getViewDataBinding().loopWheelView.setOnItemSelectedListener((picker, data, position) -> {
            if (itemSelectedListener != null) {
                itemSelectedListener.setOnItemSelectedListener(data.toString());
            }
        });
        List<String> list = new ArrayList<>();
        list.add("Male");
        list.add("Female");
        getViewDataBinding().loopWheelView.setData(list);
    }

    public void show(FragmentManager fragmentManager) {
        super.show(fragmentManager, TAG);
    }
}


