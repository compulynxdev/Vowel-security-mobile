package com.evisitor.ui.main.home.guest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.databinding.ActivityExpectedGuestBinding;
import com.evisitor.ui.base.BaseActivity;
import com.evisitor.ui.dialog.AlertDialog;
import com.evisitor.ui.main.home.guest.add.AddGuestActivity;
import com.evisitor.ui.main.home.guest.expected.ExpectedGuestFragment;
import com.evisitor.ui.main.home.scan.ScanIDActivity;
import com.sharma.mrzreader.MrzRecord;

public class GuestActivity extends BaseActivity<ActivityExpectedGuestBinding, GuestViewModel> {

    private final int SCAN_RESULT = 101;
    private ExpectedGuestFragment guestFragment;

    public static Intent getStartIntent(Context context){
        return new Intent(context, GuestActivity.class);
    }
    @Override
    public int getBindingVariable() {
        return com.evisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_expected_guest;
    }

    @Override
    public GuestViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstance()).get(GuestViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getViewModel().setNavigator(this);
        getViewDataBinding().header.tvTitle.setText(R.string.title_expected_guests);
        getViewDataBinding().header.imgBack.setVisibility(View.VISIBLE);
        getViewDataBinding().header.imgBack.setOnClickListener(v -> onBackPressed());
        setUpSearch();
        guestFragment = ExpectedGuestFragment.newInstance();
        replaceFragment(guestFragment, R.id.guest_frame);

        getViewDataBinding().fabAdd.setOnClickListener(v -> AlertDialog.newInstance()
                .setNegativeBtnShow(true)
                .setCloseBtnShow(true)
                .setTitle(getString(R.string.check_in))
                .setMsg(getString(R.string.msg_add_guest_option))
                .setNegativeBtnColor(R.color.colorPrimary)
                .setPositiveBtnLabel(getString(R.string.manually))
                .setNegativeBtnLabel(getString(R.string.scan_id))
                .setOnNegativeClickListener(dialog1 -> {
                    dialog1.dismiss();
                    Intent i = ScanIDActivity.getStartIntent(getContext());
                    startActivityForResult(i, SCAN_RESULT);
                })
                .setOnPositiveClickListener(dialog12 -> {
                    dialog12.dismiss();
                    Intent i = AddGuestActivity.getStartIntent(this);
                    startActivity(i);
                }).show(getSupportFragmentManager()));
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
                if (newText.trim().isEmpty() || newText.trim().length() >= 3) {
                    guestFragment.setSearch(newText);
                }
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SCAN_RESULT && data != null) {
                MrzRecord mrzRecord = (MrzRecord) data.getSerializableExtra("Record");
                Intent intent = AddGuestActivity.getStartIntent(this);
                intent.putExtra("Record", mrzRecord);
                startActivity(intent);
            }
        }
    }
}
