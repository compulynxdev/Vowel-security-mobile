package com.evisitor.ui.main.rejectreason;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.databinding.DialogInputBinding;
import com.evisitor.ui.base.BaseDialog;

public class InputDialog extends BaseDialog<DialogInputBinding, InputDialogViewModel> implements View.OnClickListener {

    private PositiveListener positiveListener;

    //default param
    private String mTitle = "";

    public static InputDialog newInstance() {
        Bundle args = new Bundle();
        InputDialog fragment = new InputDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public InputDialog setTitle(String mTitle) {
        this.mTitle = mTitle;
        return this;
    }

    public InputDialog setOnPositiveClickListener(PositiveListener positiveListener) {
        this.positiveListener = positiveListener;
        return this;
    }

    @Override
    public int getBindingVariable() {
        return com.evisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_input;
    }

    @Override
    public InputDialogViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstance()).get(InputDialogViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getViewDataBinding().tvTitle.setText(mTitle);

        getViewDataBinding().btnPositive.setOnClickListener(this);
        getViewDataBinding().btnNegative.setOnClickListener(this);
    }

    public void show(FragmentManager fragmentManager) {
        super.show(fragmentManager);
    }

    @Override
    public void onClick(View v) {
        hideKeyboard();
        switch (v.getId()) {
            case R.id.btn_negative:
                dismiss();
                break;

            case R.id.btn_positive:
                if (getViewDataBinding().etInput.getText().toString().isEmpty()) {
                    getBaseActivity().showToast(getString(R.string.reason_cannot_be_empty));
                } else {
                    if (positiveListener != null) {
                        positiveListener.onPositiveClick(this, getViewDataBinding().etInput.getText().toString());
                    }
                }
                break;
        }
    }

    public interface PositiveListener {
        void onPositiveClick(InputDialog dialog, String input);
    }
}

