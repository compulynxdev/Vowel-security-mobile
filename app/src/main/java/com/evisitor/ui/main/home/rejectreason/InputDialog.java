package com.evisitor.ui.main.home.rejectreason;

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

    private static final String TAG = "InputDialog";
    private PositiveListener positiveListener;

    //default param
    private String mTitle = "",hint = "";

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
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstanceM()).get(InputDialogViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getViewDataBinding().tvTitle.setText(mTitle);
        if(!hint.isEmpty())
            getViewDataBinding().etInput.setHint(hint);

        getViewDataBinding().btnPositive.setOnClickListener(this);
        getViewDataBinding().btnNegative.setOnClickListener(this);
    }

    public void show(FragmentManager fragmentManager) {
        super.show(fragmentManager);
    }

    @Override
    public void onClick(View v) {
        hideKeyboard();
        int id = v.getId();

        if (id == R.id.btn_negative) {
            dismiss();
        } else if (id == R.id.btn_positive) {
            if (getViewDataBinding().etInput.getText().toString().isEmpty()) {
                if (!hint.isEmpty()) {
                    showToast(getString(R.string.description_not_empty));
                } else {
                    showToast(getString(R.string.reason_cannot_be_empty));
                }
            } else {
                if (positiveListener != null) {
                    positiveListener.onPositiveClick(this, getViewDataBinding().etInput.getText().toString());
                }
            }
        }

    }

    public InputDialog setHint(String s) {
        hint = s;
        return  this;
    }

    public interface PositiveListener {
        void onPositiveClick(InputDialog dialog, String input);
    }
}

