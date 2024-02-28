package com.evisitor.ui.dialog.country;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.data.model.CountryResponse;
import com.evisitor.databinding.DialogCountrySelectionBinding;
import com.evisitor.ui.base.BaseDialog;

import java.util.ArrayList;
import java.util.List;

public class CountrySelectionDialog extends BaseDialog<DialogCountrySelectionBinding, CountrySelectionDialogViewModel> implements View.OnClickListener {
    private CountrySelectionCallback callback;

    private CountrySelectionAdapter adapter;
    private List<CountryResponse> adapterList;
    private List<CountryResponse> backupList;
    private boolean isShowDialCode;

    public static CountrySelectionDialog newInstance(boolean isShowDialCode, CountrySelectionCallback callback) {
        Bundle args = new Bundle();
        CountrySelectionDialog fragment = new CountrySelectionDialog();
        fragment.setArguments(args);
        fragment.setOnClick(isShowDialCode, callback);
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
        return R.layout.dialog_country_selection;
    }

    @Override
    public CountrySelectionDialogViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstanceM()).get(CountrySelectionDialogViewModel.class);
    }

    private void setOnClick(boolean isShowDialCode, CountrySelectionCallback callback) {
        this.isShowDialCode = isShowDialCode;
        this.callback = callback;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getViewDataBinding().imgClose.setOnClickListener(this);

        adapterList = new ArrayList<>();
        backupList = new ArrayList<>();
        adapter = new CountrySelectionAdapter(isShowDialCode, adapterList, pos -> {
            dismiss();
            if (callback != null) {
                callback.onSelect(adapterList.get(pos));
            }
        });
        getViewDataBinding().recyclerView.setAdapter(adapter);
        mViewModel.getResponseCountryLiveData().observe(this, countryResponseList -> {
            backupList.clear();
            backupList.addAll(countryResponseList);

            adapterList.clear();
            adapterList.addAll(countryResponseList);
            adapter.notifyDataSetChanged();
        });

        handleSearchEvent();
    }

    private void handleSearchEvent() {
        getViewDataBinding().etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String searchInput = getViewDataBinding().etSearch.getText().toString().trim();
                hideKeyboard();
                if (!searchInput.isEmpty()) {
                    performSearchOperation(searchInput);
                }
                return true;
            }
            return false;
        });

        getViewDataBinding().etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                performSearchOperation(s.toString());
            }
        });
    }

    private void performSearchOperation(String input) {
        if (adapter == null) return;
        adapterList.clear();
        if (input.isEmpty()) {
            adapterList.addAll(backupList);
        } else {
            for (CountryResponse bean : backupList) {
                if (bean.getName().toLowerCase().contains(input.toLowerCase().trim())) {
                    adapterList.add(bean);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void show(FragmentManager fragmentManager) {
        super.show(fragmentManager);
    }

    @Override
    public void onClick(View v) {
        hideKeyboard();
        if (v.getId() == R.id.img_close) {
            dismiss();
        }
    }


}
