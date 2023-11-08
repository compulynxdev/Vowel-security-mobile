package com.evisitor.ui.dialog.selection;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.databinding.DialogSelectionBinding;
import com.evisitor.ui.base.BaseBottomSheetDialog;

import java.util.List;

public class SelectionBottomSheetDialog extends BaseBottomSheetDialog<DialogSelectionBinding, SelectionViewModel> {

    private static final String TAG = "SelectionBottomSheetDialog";
    private OnItemSelectedListener itemSelectedListener;
    private List list;
    private String title = "Select";

    public static SelectionBottomSheetDialog newInstance(String title, List list) {

        Bundle args = new Bundle();

        SelectionBottomSheetDialog fragment = new SelectionBottomSheetDialog();
        fragment.setArguments(args);
        fragment.setData(title, list);
        return fragment;
    }

    private void setData(String title, List list) {
        this.title = title;
        this.list = list;
    }

    public SelectionBottomSheetDialog setItemSelectedListener(OnItemSelectedListener itemSelectedListener) {
        this.itemSelectedListener = itemSelectedListener;
        return this;
    }

    @Override
    public int getBindingVariable() {
        return com.evisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_selection;
    }

    @Override
    public SelectionViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstanceM()).get(SelectionViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getViewDataBinding().tvTitle.setText(title);
        if (list == null) return;
        SelectionAdapter selectionAdapter = new SelectionAdapter(list, pos -> {
            if (itemSelectedListener != null) {
                dismiss();
                itemSelectedListener.setOnItemSelectedListener(pos);
            }
        });
        getViewDataBinding().recyclerView.setAdapter(selectionAdapter);
    }

    public void show(FragmentManager fragmentManager) {
        super.show(fragmentManager, TAG);
    }
}


