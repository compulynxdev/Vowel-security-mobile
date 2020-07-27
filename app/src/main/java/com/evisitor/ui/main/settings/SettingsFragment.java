package com.evisitor.ui.main.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.databinding.FragmentSettingsBinding;
import com.evisitor.ui.base.BaseFragment;
import com.evisitor.ui.base.BaseNavigator;
import com.evisitor.ui.main.settings.content.ContentActivity;
import com.evisitor.ui.main.settings.info.DeviceInfoDialog;
import com.evisitor.ui.main.settings.language.LanguageDialog;
import com.evisitor.util.AppConstants;

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
        getViewDataBinding().tvLang.setText(getViewModel().getDataManager().getLanguage());

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
                DeviceInfoDialog.newInstance().show(getChildFragmentManager());
                break;

            case R.id.language_constraint:
                LanguageDialog.newInstance(language -> {
                    getViewModel().getDataManager().setLanguage(language.getLangName());
                    getViewDataBinding().tvLang.setText(language.getLocalisationTitle());
                }).show(getChildFragmentManager());
                break;

            case R.id.aboutus_constraint:
                Intent intent = ContentActivity.newIntent(getBaseActivity());
                intent.putExtra("From", AppConstants.ACTIVITY_ABOUT_US);
                startActivity(intent);
                break;

            case R.id.privacy_constraint:
                intent = ContentActivity.newIntent(getBaseActivity());
                intent.putExtra("From", AppConstants.ACTIVITY_PRIVACY);
                startActivity(intent);
                break;

            case R.id.logout_constraint:
                showAlert(R.string.logout, R.string.logout_msg)
                        .setCancelViewVisible()
                        .setLabelOkBtn(getString(R.string.yes))
                        .setLabelCancelBtn(getString(R.string.no))
                        .setOnOKClickListener(dialog -> {
                            dialog.dismiss();
                            openActivityOnTokenExpire();
                        }).setOnCancelClickListener(DialogFragment::dismiss);
                break;
        }
    }
}
