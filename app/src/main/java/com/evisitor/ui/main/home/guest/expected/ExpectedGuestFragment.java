package com.evisitor.ui.main.home.guest.expected;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.data.model.Guests;
import com.evisitor.data.model.VisitorProfileBean;
import com.evisitor.databinding.FragmentExpectedGuestBinding;
import com.evisitor.ui.base.BaseFragment;
import com.evisitor.ui.dialog.AlertDialog;
import com.evisitor.ui.main.home.scan.ScanIDActivity;
import com.evisitor.ui.main.idverification.IdVerificationCallback;
import com.evisitor.ui.main.idverification.IdVerificationDialog;
import com.evisitor.ui.main.visitorprofile.VisitorProfileDialog;
import com.evisitor.util.pagination.RecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;

public class ExpectedGuestFragment extends BaseFragment<FragmentExpectedGuestBinding, ExpectedGuestViewModel> implements ExpectedGuestNavigator {
    private List<Guests> guestsList;
    private RecyclerViewScrollListener scrollListener;
    private ExpectedGuestAdapter adapter;
    private EditText etSearch;
    private int page = 0;

    public static ExpectedGuestFragment newInstance(EditText editText) {
        ExpectedGuestFragment fragment = new ExpectedGuestFragment();
        Bundle args = new Bundle();
        fragment.setEditText(editText);
        fragment.setArguments(args);
        return fragment;
    }

    private void setEditText(EditText editText) {
        etSearch=editText;
    }

    @Override
    public int getBindingVariable() {
        return com.evisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_expected_guest;
    }

    @Override
    public ExpectedGuestViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstance()).get(ExpectedGuestViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getViewModel().setCheckInOutNavigator(this);
        setUpAdapter();
        setUpSearch();
    }

    private void setUpAdapter() {
        guestsList = new ArrayList<>();
        adapter = new ExpectedGuestAdapter(guestsList, getContext(), pos -> {
            List<VisitorProfileBean> visitorProfileBeanList = getViewModel().setClickVisitorDetail(guestsList.get(pos));
            VisitorProfileDialog.newInstance(visitorProfileBeanList, visitorProfileDialog -> {
                visitorProfileDialog.dismiss();

                Guests tmpBean = getViewModel().getDataManager().getGuestDetail();
                if (!getViewModel().getDataManager().isIdentifyFeature() || tmpBean.getIdentityNo().isEmpty()) {
                    showCheckinOptions();
                } else {
                    IdVerificationDialog.newInstance(new IdVerificationCallback() {
                        @Override
                        public void onScanClick(IdVerificationDialog dialog) {
                            dialog.dismiss();
                            Intent i = ScanIDActivity.getStartIntent(getContext());
                            startActivityForResult(i, 101);
                        }

                        @Override
                        public void onSubmitClick(IdVerificationDialog dialog, String id) {
                            dialog.dismiss();

                            if (tmpBean.getIdentityNo().equals(id))
                                showCheckinOptions();
                            else {
                                showToast(R.string.alert_id);
                            }
                        }
                    }).show(getFragmentManager());
                }
            }).setBtnLabel(getString(R.string.check_in)).show(getFragmentManager());
        });
        adapter.setHasStableIds(true);
        getViewDataBinding().recyclerView.setAdapter(adapter);

        scrollListener = new RecyclerViewScrollListener() {
            @Override
            public void onLoadMore() {
                adapter.showLoading(true);
                adapter.notifyDataSetChanged();
                page++;

                getViewModel().getGuestListData(page, etSearch.getText().toString().trim());
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
        doSearch(etSearch.getText().toString());
    }

    private void setUpSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
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

        etSearch.setOnEditorActionListener((v, actionId, event) -> {
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
        AlertDialog alert = AlertDialog.newInstance()
                .setCloseBtnShow(true)
                .setTitle(getString(R.string.check_in))
                .setMsg(getString(R.string.msg_check_in_option))
                .setPositiveBtnLabel(getString(R.string.approve_by_call))
                .setOnPositiveClickListener(dialog12 -> {
                    dialog12.dismiss();
                    showCallDialog();
                });


        Guests bean = getViewModel().getDataManager().getGuestDetail();
        if (!bean.isNotificationStatus() || bean.getHouseNo().isEmpty()) {
            alert.setNegativeBtnShow(false).show(getFragmentManager());
        } else {
            alert.setNegativeBtnColor(R.color.colorPrimary)
                    .setNegativeBtnShow(true)
                    .setNegativeBtnLabel(getString(R.string.send_notification))
                    .setOnNegativeClickListener(dialog1 -> {
                        dialog1.dismiss();
                        getViewModel().sendNotification();
                    })
                    .show(getFragmentManager());
        }
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
                }).show(getFragmentManager());
    }

    @Override
    public void hideSwipeToRefresh() {
        getViewDataBinding().swipeToRefresh.setRefreshing(false);
    }

    @Override
    public void refreshList() {
        doSearch(etSearch.getText().toString());
    }
}
