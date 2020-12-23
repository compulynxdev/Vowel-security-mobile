package com.evisitor.ui.main.home.sp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.databinding.ActivitySpBinding;
import com.evisitor.ui.base.BaseActivity;
import com.evisitor.ui.dialog.AlertDialog;
import com.evisitor.ui.main.home.scan.ScanIDActivity;
import com.evisitor.ui.main.home.visitor.AddVisitorActivity;
import com.evisitor.ui.main.home.visitor.commercialvisitor.CommercialAddVisitorActivity;
import com.evisitor.util.AppConstants;
import com.sharma.mrzreader.MrzRecord;

public class SPActivity extends BaseActivity<ActivitySpBinding, SPViewModel> {

    private ExpectedSPFragment fragment;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, SPActivity.class);
    }

    @Override
    public int getBindingVariable() {
        return com.evisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_sp;
    }

    @Override
    public SPViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstance()).get(SPViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getViewModel().setNavigator(this);
        getViewDataBinding().header.tvTitle.setText(R.string.title_service_provider);
        getViewDataBinding().header.imgBack.setVisibility(View.VISIBLE);
        getViewDataBinding().header.imgBack.setOnClickListener(v -> onBackPressed());
        getViewDataBinding().fabAdd.setOnClickListener(v -> AlertDialog.newInstance()
                .setNegativeBtnShow(true)
                .setCloseBtnShow(true)
                .setTitle(getString(R.string.check_in))
                .setMsg(getString(R.string.msg_add_option))
                .setNegativeBtnColor(R.color.colorPrimary)
                .setPositiveBtnLabel(getString(R.string.manually))
                .setNegativeBtnLabel(getString(R.string.scan_id))
                .setOnNegativeClickListener(dialog1 -> {
                    dialog1.dismiss();
                    Intent i = ScanIDActivity.getStartIntent(getContext());
                    startActivityForResult(i, AppConstants.SCAN_RESULT);
                })
                .setOnPositiveClickListener(dialog12 -> {
                    dialog12.dismiss();
                    Intent i = getViewModel().getDataManager().isCommercial() ? CommercialAddVisitorActivity.getStartIntent(this) : AddVisitorActivity.getStartIntent(this);
                    i.putExtra(AppConstants.FROM, AppConstants.CONTROLLER_SP);
                    startActivity(i);
                }).show(getSupportFragmentManager()));
        setUpSearch();

        fragment = ExpectedSPFragment.newInstance();
        replaceFragment(fragment, R.id.frame_layout);
    }

    private void setUpSearch() {
        getViewDataBinding().header.imgSearch.setVisibility(View.VISIBLE);
        getViewDataBinding().header.imgSearch.setOnClickListener(v -> {
            hideKeyboard();
            getViewDataBinding().customSearchView.llSearchBar.setVisibility(getViewDataBinding().customSearchView.llSearchBar.getVisibility() == View.GONE
                    ? View.VISIBLE : View.GONE);

            getViewDataBinding().customSearchView.searchView.setQuery("", false);
        });

        setupSearchSetting(getViewDataBinding().customSearchView.searchView);
        getViewDataBinding().customSearchView.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String txt = newText.trim();

                if (txt.isEmpty() || txt.length() >= 3) {
                    fragment.setSearch(txt);
                }
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == AppConstants.SCAN_RESULT && data != null) {
                MrzRecord mrzRecord = (MrzRecord) data.getSerializableExtra("Record");
                Intent intent = getViewModel().getDataManager().isCommercial() ? CommercialAddVisitorActivity.getStartIntent(this) : AddVisitorActivity.getStartIntent(this);
                intent.putExtra("Record", mrzRecord);
                intent.putExtra(AppConstants.FROM, AppConstants.CONTROLLER_SP);
                startActivity(intent);
            }
        }
    }
}
