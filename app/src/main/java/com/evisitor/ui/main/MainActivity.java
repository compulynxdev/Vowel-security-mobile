package com.evisitor.ui.main;

import android.os.Bundle;
import androidx.lifecycle.ViewModelProvider;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.databinding.ActivityMainBinding;
import com.evisitor.ui.base.BaseActivity;
import com.evisitor.ui.base.BaseNavigator;
import com.evisitor.ui.profile.UserProfileFrament;
import com.evisitor.ui.settings.SettingsFragment;


public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> implements BaseNavigator {

    private final static int ID_HOME = 1;
    private final static int ID_EXPLORE = 2;
    private final static int ID_SETTINGS = 3;
    private final static int ID_PROFILE = 4;

    @Override
    public int getBindingVariable() {
        return com.evisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public MainViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstance()).get(MainViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getViewModel().setNavigator(this);

        getViewDataBinding().bottomNavigation.add(new MeowBottomNavigation.Model(ID_HOME, R.drawable.ic_home));
        getViewDataBinding().bottomNavigation.add(new MeowBottomNavigation.Model(ID_EXPLORE, R.drawable.ic_explore));
        getViewDataBinding().bottomNavigation.add(new MeowBottomNavigation.Model(ID_SETTINGS, R.drawable.ic_settings));
        getViewDataBinding().bottomNavigation.add(new MeowBottomNavigation.Model(ID_PROFILE, R.drawable.ic_profile));

        getViewDataBinding().bottomNavigation.setOnClickMenuListener(item -> {
        });
        getViewDataBinding().bottomNavigation.setOnReselectListener(item -> {
        });
        getViewDataBinding().bottomNavigation.setOnShowListener(item -> {

            String name = "";
            switch (item.getId()) {
                case ID_HOME:
                    name = "Home";
                    break;
                case ID_EXPLORE:
                    name = "Explore";
                    break;
                case ID_SETTINGS:
                    replaceFragment(SettingsFragment.newInstance(),R.id.frame_layout);
                    break;
                case ID_PROFILE:
                    replaceFragment(UserProfileFrament.newInstance(),R.id.frame_layout);
                    break;
            }
        });
        getViewDataBinding().bottomNavigation.show(ID_HOME, true);
    }
}
