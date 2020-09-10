package com.evisitor.ui.main.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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

        getViewDataBinding().toolbar.imgSearch.setVisibility(View.VISIBLE);
        getViewDataBinding().toolbar.imgSearch.setImageDrawable(getResources().getDrawable(R.drawable.ic_logout_icon));
        getViewDataBinding().toolbar.imgSearch.setOnClickListener(v -> getViewModel().getDataManager().logout(getBaseActivity()));

        getViewModel().getUserDetail().observe(this, userDetail -> {
            getViewDataBinding().tvName.setText(userDetail.getFullName());
            getViewDataBinding().tvUsername.setText(userDetail.getUsername());
            getViewDataBinding().tvEmail.setText(userDetail.getEmail());
            getViewDataBinding().tvGender.setText(userDetail.getGender());
            getViewDataBinding().tvContact.setText(userDetail.getContactNo());
            getViewDataBinding().tvAddress.setText(userDetail.getAddress());

            if (userDetail.getImageUrl().isEmpty()) {
                Glide.with(this)
                        .load(R.drawable.ic_person)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(getViewDataBinding().imgUser);
            } else {
                Glide.with(this)
                        .load(getViewModel().getDataManager().getImageBaseURL().concat(userDetail.getImageUrl()))
                        .centerCrop()
                        .placeholder(R.drawable.ic_person)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(getViewDataBinding().imgUser);
            }
        });
    }
}
