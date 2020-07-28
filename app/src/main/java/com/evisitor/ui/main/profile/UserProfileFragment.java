package com.evisitor.ui.main.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.databinding.FragmentUserProfileBinding;
import com.evisitor.ui.base.BaseFragment;
import com.evisitor.ui.base.BaseNavigator;

public class UserProfileFragment extends BaseFragment<FragmentUserProfileBinding, UserProfileViewModel> implements BaseNavigator {


    public static Fragment newInstance() {
        UserProfileFragment fragment = new UserProfileFragment();
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
        return R.layout.fragment_user_profile;
    }

    @Override
    public UserProfileViewModel getViewModel() {
        return new ViewModelProvider(this,ViewModelProviderFactory.getInstance()).get(UserProfileViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getViewModel().setNavigator(this);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText(R.string.title_profile);

        getViewModel().getUserDetail().observe(this, userDetail -> {
            getViewDataBinding().tvName.setText(userDetail.getFullName());
            getViewDataBinding().tvUsername.setText(userDetail.getUsername());
            getViewDataBinding().tvEmail.setText(userDetail.getEmail());
            getViewDataBinding().tvGender.setText(userDetail.getGender());
            getViewDataBinding().tvContact.setText(userDetail.getGender());
            getViewDataBinding().tvAddress.setText(userDetail.getAddress());
        });
    }
}
