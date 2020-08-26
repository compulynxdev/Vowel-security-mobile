package com.evisitor.ui.main.home.guest.expected;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.data.model.Guests;
import com.evisitor.data.model.VisitorProfileBean;
import com.evisitor.databinding.ActivityExpectedGuestBinding;
import com.evisitor.ui.base.BaseActivity;
import com.evisitor.ui.dialog.AlertDialog;
import com.evisitor.ui.main.home.guest.add.AddGuestActivity;
import com.evisitor.ui.main.home.scan.ScanIDActivity;
import com.evisitor.ui.main.visitorprofile.VisitorProfileDialog;
import com.evisitor.util.pagination.RecyclerViewScrollListener;
import com.sharma.mrzreader.MrzRecord;

import java.util.ArrayList;
import java.util.List;

public class ExpectedGuestActivity extends BaseActivity<ActivityExpectedGuestBinding,GuestViewModel> implements GuestNavigator {

    private List<Guests> guestsList;
    private RecyclerViewScrollListener scrollListener;
    private GuestAdapter adapter;
    private int page = 0;
    private final int SCAN_RESULT = 101;

    public static Intent getStartIntent(Context context){
        return new Intent(context,ExpectedGuestActivity.class);
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
        TextView tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(R.string.title_expected_guests );
        setUpAdapter();
        setUpSearch();

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
                    Intent i = ScanIDActivity.getStartIntent(this);
                    startActivityForResult(i, SCAN_RESULT);
                })
                .setOnPositiveClickListener(dialog12 -> {
                    dialog12.dismiss();
                    Intent i = AddGuestActivity.getStartIntent(this);
                    startActivity(i);
                }).show(getSupportFragmentManager()));
    }

    private void setUpAdapter() {
        guestsList = new ArrayList<>();
        adapter = new GuestAdapter(guestsList, this, pos -> {
            Guests guest = guestsList.get(pos);
            List<VisitorProfileBean> visitorProfileBeanList = getViewModel().setClickVisitorDetail(guest);
            VisitorProfileDialog.newInstance(visitorProfileBeanList, visitorProfileDialog -> {
                visitorProfileDialog.dismiss();
                showCheckinOptions();
            }).setBtnLabel(getString(R.string.check_in)).show(getSupportFragmentManager());
        });
        adapter.setHasStableIds(true);
        getViewDataBinding().recyclerView.setAdapter(adapter);

        scrollListener = new RecyclerViewScrollListener() {
            @Override
            public void onLoadMore() {
                adapter.showLoading(true);
                adapter.notifyDataSetChanged();
                page++;

                getViewModel().getGuestListData(page, getViewDataBinding().etSearch.getText().toString().trim());
            }
        };
        getViewDataBinding().recyclerView.addOnScrollListener(scrollListener);

        getViewModel().getGuestListData().observe(this, guests -> {
            adapter.showLoading(false);
            adapter.notifyDataSetChanged();

            if (page == 0) guestsList.clear();

            guestsList.addAll(guests);
            adapter.notifyDataSetChanged();
        });

        getViewDataBinding().swipeToRefresh.setOnRefreshListener(this::updateUI);
        getViewDataBinding().swipeToRefresh.setColorSchemeResources(R.color.colorPrimary);
        updateUI();
    }

    private void updateUI() {
        getViewDataBinding().swipeToRefresh.setRefreshing(true);
        doSearch(getViewDataBinding().etSearch.getText().toString());
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
                    doSearch(s.toString());
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

    private void doSearch(String search) {
        scrollListener.onDataCleared();
        guestsList.clear();
        this.page = 0;
        getViewModel().getGuestListData(page, search.trim());
    }

    private void showCheckinOptions() {
        AlertDialog.newInstance()
                .setNegativeBtnShow(true)
                .setCloseBtnShow(true)
                .setTitle(getString(R.string.check_in))
                .setMsg(getString(R.string.msg_check_in_option))
                .setNegativeBtnColor(R.color.colorPrimary)
                .setPositiveBtnLabel(getString(R.string.approve_by_call))
                .setNegativeBtnLabel(getString(R.string.send_notification))
                .setOnNegativeClickListener(dialog1 -> {
                    dialog1.dismiss();
                    getViewModel().sendNotification();
                })
                .setOnPositiveClickListener(dialog12 -> {
                    dialog12.dismiss();
                    showCallDialog();
                }).show(getSupportFragmentManager());

    }

    private void showCallDialog() {
        AlertDialog.newInstance()
                .setNegativeBtnShow(true)
                .setCloseBtnShow(true)
                .setTitle(getString(R.string.check_in))
                .setMsg(getString(R.string.msg_check_in_call))
                .setPositiveBtnLabel(getString(R.string.approve))
                .setNegativeBtnLabel(getString(R.string.reject))
                .setOnNegativeClickListener(dialog1 -> {
                    dialog1.dismiss();
                    showAlert(R.string.alert,R.string.check_in_rejected);
                })
                .setOnPositiveClickListener(dialog12 -> {
                    dialog12.dismiss();
                    getViewModel().approveByCall();
                }).show(getSupportFragmentManager());
    }

    @Override
    public void hideSwipeToRefresh() {
        getViewDataBinding().swipeToRefresh.setRefreshing(false);
    }

    @Override
    public void refreshList() {
        doSearch(getViewDataBinding().etSearch.getText().toString());
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
