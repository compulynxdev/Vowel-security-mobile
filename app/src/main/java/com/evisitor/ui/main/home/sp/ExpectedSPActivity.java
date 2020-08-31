package com.evisitor.ui.main.home.sp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.lifecycle.ViewModelProvider;
import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.databinding.ActivityExpectedSpBinding;
import com.evisitor.ui.base.BaseActivity;

public class ExpectedSPActivity extends BaseActivity<ActivityExpectedSpBinding, SPViewModel> implements SPNavigator {

    private ExpectedSPFragment fragment;
    public static Intent getStartIntent(Context context) {
        return new Intent(context, ExpectedSPActivity.class);
    }

    @Override
    public int getBindingVariable() {
        return com.evisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_expected_sp;
    }

    @Override
    public SPViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstance()).get(SPViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getViewModel().setNavigator(this);
        TextView tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(R.string.title_service_provider);
        setUpSearch();

        fragment = ExpectedSPFragment.newInstance();
        addFragment(fragment,R.id.frame_layout,false);
    }

    private void setUpSearch() {
        ImageView imgSearch = findViewById(R.id.img_search);
        imgSearch.setVisibility(View.VISIBLE);
        ImageView imgBack = findViewById(R.id.img_back);
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setOnClickListener(v -> onBackPressed());
        imgSearch.setOnClickListener(v -> {
            hideKeyboard();
            getViewDataBinding().searchBar.setVisibility(getViewDataBinding().searchBar.getVisibility() == View.GONE
                    ? View.VISIBLE : View.GONE);

            if (!getViewDataBinding().etSearch.getText().toString().trim().isEmpty()) {
                getViewDataBinding().etSearch.setText("");
            }
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
                if (s.toString().isEmpty() || s.toString().length() >= 2) {
                    fragment.setSearch(s.toString());
                }
            }
        });

        getViewDataBinding().etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard();
                return true;
            }
            return false;
        });
    }
    @Override
    public void hideSwipeToRefresh() {
    }

    @Override
    public void refreshList() {
    }
}
