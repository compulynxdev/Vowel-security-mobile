package com.evisitor.ui.main.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.data.model.VisitorProfileBean;
import com.evisitor.databinding.FragmentHomeBinding;
import com.evisitor.ui.base.BaseFragment;
import com.evisitor.ui.base.BaseNavigator;
import com.evisitor.ui.dialog.AlertDialog;
import com.evisitor.ui.main.visitorprofile.VisitorProfileDialog;

import java.util.ArrayList;
import java.util.List;
import com.evisitor.ui.main.home.guest.expected.ExpectedGuestActivity;

public class HomeFragment extends BaseFragment<FragmentHomeBinding, HomeViewModel> implements BaseNavigator {

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getBindingVariable() {
        return com.evisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public HomeViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstance()).get(HomeViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getViewModel().setNavigator(this);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText(R.string.title_home);

        setupAdapter();
    }

    private void setupAdapter() {
        getViewModel().getHomeListData().observe(this, homeBeansList -> {
            HomeAdapter homeAdapter = new HomeAdapter(homeBeansList, pos -> {
                switch (pos) {
                    //Guests
                    case 0:
                        Intent i = ExpectedGuestActivity.getStartIntent(getContext());
                        startActivity(i);
                        /*VisitorProfileDialog.newInstance(getDummyData(), (dialog) -> {
                            dialog.dismiss();
                            showCheckInDialog();
                        }).setBtnLabel(getString(R.string.check_in)).show(getChildFragmentManager());*/
                        break;

                    //House-Keeping
                    case 1:
                        break;

                    //Service-Provider
                    case 2:
                        break;

                    //Total Expected Visitor
                    case 3:
                        break;

                    //Blacklisted Visitor
                    case 4:
                        break;

                    //Trespasser Visitor
                    case 5:
                        break;
                }
            });
            getViewDataBinding().recyclerView.setAdapter(homeAdapter);
        });
    }

    private void showCheckInDialog() {
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
                })
                .setOnPositiveClickListener(dialog12 -> {
                    dialog12.dismiss();
                    showCallDialog();
                }).show(getChildFragmentManager());
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
                })
                .setOnPositiveClickListener(dialog12 -> {
                    dialog12.dismiss();
                }).show(getChildFragmentManager());
    }

    private List<VisitorProfileBean> getDummyData() {
        List<VisitorProfileBean> list = new ArrayList<>();
        list.add(new VisitorProfileBean(getString(R.string.data_name, "Hemant Sharma")));
        list.add(new VisitorProfileBean(getString(R.string.vehicle_col), "MP09 HS0001", true));
        list.add(new VisitorProfileBean(getString(R.string.data_house, "A101")));
        list.add(new VisitorProfileBean(getString(R.string.data_host, "Shivanshu")));
        list.add(new VisitorProfileBean(getString(R.string.data_time, "28/07/2020 11:00 AM")));
        list.add(new VisitorProfileBean(getString(R.string.data_identity, "N/A")));
        list.add(new VisitorProfileBean(getString(R.string.data_mobile, "7566661234")));
        return list;
    }
}
