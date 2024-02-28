package com.evisitor.ui.main.profile;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
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

    private String imageUrl = "";

    public static Fragment newInstance() {
        UserProfileFragment fragment = new UserProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel.setNavigator(this);
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
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstanceM()).get(UserProfileViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getViewDataBinding().toolbar.tvTitle.setText(R.string.title_profile);

        getViewDataBinding().toolbar.imgSearch.setVisibility(View.VISIBLE);
        getViewDataBinding().toolbar.imgSearch.setPadding(3, 3, 3, 3);
        getViewDataBinding().toolbar.imgSearch.setImageDrawable(ContextCompat.getDrawable(getBaseActivity(), R.drawable.ic_logout_2));
        getViewDataBinding().imgUser.setOnClickListener(v -> showFullImage(imageUrl));

        getViewDataBinding().toolbar.imgSearch.setOnClickListener(v -> showAlert(R.string.logout, R.string.logout_msg)
                .setNegativeBtnShow(true)
                .setPositiveBtnLabel(getString(R.string.yes))
                .setNegativeBtnLabel(getString(R.string.no))
                .setOnPositiveClickListener(dialog -> {
                    dialog.dismiss();
                    openActivityOnTokenExpire();
                }).setOnNegativeClickListener(DialogFragment::dismiss));

        mViewModel.getUserDetail().observe(getViewLifecycleOwner(), userDetail -> {
            getViewDataBinding().tvName.setText(userDetail.getFullName().isEmpty() ? getString(R.string.na) : userDetail.getFullName());
            getViewDataBinding().tvUsername.setText(userDetail.getUsername().isEmpty() ? getString(R.string.na) : userDetail.getUsername());
            getViewDataBinding().tvEmail.setText(userDetail.getEmail().isEmpty() ? getString(R.string.na) : userDetail.getEmail());
            getViewDataBinding().tvGender.setText(userDetail.getGender().isEmpty() ? getString(R.string.na) : userDetail.getGender());
            getViewDataBinding().tvContact.setText(userDetail.getContactNo().isEmpty() ? getString(R.string.na) : "+".concat(userDetail.getDialingCode()).concat(" ").concat(userDetail.getContactNo()));
            getViewDataBinding().tvAddress.setText(userDetail.getAddress().isEmpty() ? getString(R.string.na) : userDetail.getAddress());
            getViewDataBinding().tvGroup.setText(userDetail.getGroupName().isEmpty() ? getString(R.string.na) : userDetail.getGroupName());


            imageUrl = userDetail.getImageUrl();
            if (userDetail.getImageUrl().isEmpty()) {
                Glide.with(this)
                        .load(R.drawable.ic_person)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(getViewDataBinding().imgUser);
            } else {
                Glide.with(this)
                        .load(mViewModel.getDataManager().getImageBaseURL().concat(userDetail.getImageUrl()))
                        .centerCrop()
                        .placeholder(R.drawable.ic_person)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(getViewDataBinding().imgUser);
            }
        });

        mViewModel.getAccountName().observe(this, s -> getViewDataBinding().tvProperty.setText(s.isEmpty() ? getString(R.string.na) : s));
    }
}
