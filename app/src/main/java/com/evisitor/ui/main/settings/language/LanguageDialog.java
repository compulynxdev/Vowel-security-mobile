package com.evisitor.ui.main.settings.language;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.data.model.LanguageBean;
import com.evisitor.databinding.DialogLanguageBinding;
import com.evisitor.ui.base.BaseDialog;

import java.util.List;

public class LanguageDialog extends BaseDialog<DialogLanguageBinding, LanguageDialogViewModel> implements View.OnClickListener {
    private static final String TAG = "LanguageBean Dialog";
    private LanguageBean language;
    private List<LanguageBean> langList;
    private LanguageCallback callback;


    public static LanguageDialog newInstance(LanguageCallback callback) {
        Bundle args = new Bundle();
        LanguageDialog fragment = new LanguageDialog();
        fragment.setArguments(args);
        fragment.setOnClick(callback);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel.setNavigator(getBaseActivity());
    }

    @Override
    public int getBindingVariable() {
        return com.evisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_language;
    }

    @Override
    public LanguageDialogViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstanceM()).get(LanguageDialogViewModel.class);
    }

    private void setOnClick(LanguageCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getViewDataBinding().btnCancel.setOnClickListener(this);
        getViewDataBinding().btnOk.setOnClickListener(this);
        langList = mViewModel.getLangList();
        LanguageAdapter adapter = new LanguageAdapter(langList, pos -> language = langList.get(pos));
        language = mViewModel.getSelectedLang();
        for (int i = 0; i < langList.size(); i++) {
            if (language.getLangName().equalsIgnoreCase(langList.get(i).getLangName())) {
                adapter.lastPos = i;
                langList.get(i).setSelected(true);
                break;
            }
        }
        getViewDataBinding().recyclerView.setAdapter(adapter);

    }

    public void show(FragmentManager fragmentManager) {
        super.show(fragmentManager);
    }

    @Override
    public void onClick(View v) {
        hideKeyboard();
        int id = v.getId();

        if (id == R.id.btn_ok) {
            if (verifyInput()) {
                dismiss();
                callback.onDoneClick(language);
            } else {
                dismiss();
            }
        } else if (id == R.id.btn_cancel) {
            dismiss();
        }

    }

    private boolean verifyInput() {
        return !language.getLangName().equals(mViewModel.getSelectedLang().getLangName());
    }


}
