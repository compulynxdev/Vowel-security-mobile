package com.evisitor.ui.settings;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.databinding.FragmentSettingsBinding;
import com.evisitor.ui.base.BaseFragment;
import com.evisitor.ui.base.BaseNavigator;
import com.evisitor.ui.settings.language.LanguageDialog;

public class SettingsFragment extends BaseFragment<FragmentSettingsBinding, SettingsViewModel> implements BaseNavigator, View.OnClickListener {

   public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
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
        return R.layout.fragment_settings;
    }

    @Override
    public SettingsViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstance()).get(SettingsViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getViewModel().setNavigator(this);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText(R.string.title_settings);

        setOnClickListener(getViewDataBinding().infoConstraint, getViewDataBinding().languageConstraint, getViewDataBinding().aboutusConstraint
                , getViewDataBinding().privacyConstraint, getViewDataBinding().logoutConstraint);
    }

    private void setOnClickListener(View... views) {
        for (View view : views)
            view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.info_constraint:
                break;

            case R.id.language_constraint:
                LanguageDialog.newInstance(language -> {
                    //Todo set current lang to preference
                    //getViewModel().getDataManager().
                    getViewDataBinding().tvLang.setText(language.getLocalisationTitle());
                }).show(getChildFragmentManager());
                break;

            case R.id.aboutus_constraint:
                break;

            case R.id.privacy_constraint:
                break;

            case R.id.logout_constraint:
                break;
        }
    }
}
