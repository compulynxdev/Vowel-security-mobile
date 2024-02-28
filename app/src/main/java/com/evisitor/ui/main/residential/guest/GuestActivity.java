package com.evisitor.ui.main.residential.guest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.databinding.ActivityExpectedGuestOrVisitorBinding;
import com.evisitor.ui.base.BaseActivity;
import com.evisitor.ui.dialog.AlertDialog;
import com.evisitor.ui.main.residential.add.AddVisitorActivity;
import com.evisitor.ui.main.residential.guest.expected.ExpectedGuestFragment;
import com.evisitor.util.AppConstants;
import com.smartengines.ScanSmartActivity;

import static com.evisitor.util.AppConstants.SCAN_RESULT;

public class GuestActivity extends BaseActivity<ActivityExpectedGuestOrVisitorBinding, GuestViewModel> {

    private ExpectedGuestFragment guestFragment;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, GuestActivity.class);
    }

    @Override
    public int getBindingVariable() {
        return com.evisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_expected_guest_or_visitor;
    }

    @Override
    public GuestViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstanceM()).get(GuestViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel.setNavigator(this);
        getViewDataBinding().header.tvTitle.setText(R.string.title_expected_guests);
        getViewDataBinding().header.imgBack.setVisibility(View.VISIBLE);
        getViewDataBinding().header.imgBack.setOnClickListener(v -> onBackPressed());
        setUpSearch();
        guestFragment = ExpectedGuestFragment.newInstance();
        replaceFragment(guestFragment, R.id.frame_view);

        getViewDataBinding().fabAdd.setOnClickListener(v -> {
            if (mViewModel.getDataManager().isIdentifyFeature()) {
                AlertDialog.newInstance()
                        .setNegativeBtnShow(true)
                        .setCloseBtnShow(true)
                        .setTitle(getString(R.string.check_in))
                        .setMsg(getString(R.string.msg_add_option))
                        .setNegativeBtnColor(R.color.colorPrimary)
                        .setPositiveBtnLabel(getString(R.string.manually))
                        .setNegativeBtnLabel(getString(R.string.scan_id))
                        .setOnNegativeClickListener(dialog1 -> {
                            dialog1.dismiss();
                            //Intent i = ScanIDActivity.getStartIntent(getContext());
                            Intent i = ScanSmartActivity.getStartIntent(getContext());
                            startActivityForResult(i, SCAN_RESULT);
                        })
                        .setOnPositiveClickListener(dialog12 -> {
                            dialog12.dismiss();
                            Intent i = AddVisitorActivity.getStartIntent(this);
                            i.putExtra(AppConstants.FROM, AppConstants.CONTROLLER_GUEST);
                            startActivity(i);
                        }).show(getSupportFragmentManager());
            } else {
                Intent i = AddVisitorActivity.getStartIntent(this);
                i.putExtra(AppConstants.FROM, AppConstants.CONTROLLER_GUEST);
                startActivity(i);
            }
        });
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
                    guestFragment.setSearch(txt);
                }
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SCAN_RESULT /*&& data != null*/) {
                //MrzRecord mrzRecord = (MrzRecord) data.getSerializableExtra("Record");
                Intent intent = AddVisitorActivity.getStartIntent(this);
                //intent.putExtra("Record", mrzRecord);
                intent.putExtra("Record", "");
                intent.putExtra(AppConstants.FROM, AppConstants.CONTROLLER_GUEST);
                startActivity(intent);
            }
        }
    }
}
