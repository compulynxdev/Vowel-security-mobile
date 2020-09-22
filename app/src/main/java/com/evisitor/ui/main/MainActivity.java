package com.evisitor.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.databinding.ActivityMainBinding;
import com.evisitor.ui.base.BaseActivity;
import com.evisitor.ui.base.BaseNavigator;
import com.evisitor.ui.main.activity.ActivityFragment;
import com.evisitor.ui.main.home.HomeFragment;
import com.evisitor.ui.main.notifications.NotificationsFragment;
import com.evisitor.ui.main.profile.UserProfileFragment;
import com.evisitor.ui.main.settings.SettingsFragment;


public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> implements BaseNavigator {

    private final static int ID_HOME = 1;
    private final static int ID_ACTIVITY = 2;
    private final static int ID_NOTIFICATION = 3;
    private final static int ID_SETTINGS = 4;
    private final static int ID_PROFILE = 5;

    public static Intent newIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

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
        getViewDataBinding().bottomNavigation.add(new MeowBottomNavigation.Model(ID_ACTIVITY, R.drawable.ic_clock));
        getViewDataBinding().bottomNavigation.add(new MeowBottomNavigation.Model(ID_NOTIFICATION, R.drawable.ic_notification));
        getViewDataBinding().bottomNavigation.add(new MeowBottomNavigation.Model(ID_SETTINGS, R.drawable.ic_settings));
        getViewDataBinding().bottomNavigation.add(new MeowBottomNavigation.Model(ID_PROFILE, R.drawable.ic_profile));

        getViewDataBinding().bottomNavigation.setOnClickMenuListener(item -> {
        });
        getViewDataBinding().bottomNavigation.setOnReselectListener(item -> {
        });

        //HomeFragment Initialise one time because setOnShowListener run many time during initialization
        HomeFragment homeFragment = HomeFragment.newInstance();
        getViewDataBinding().bottomNavigation.setOnShowListener(item -> {
            switch (item.getId()) {
                case ID_HOME:
                    replaceFragment(homeFragment, R.id.frame_layout);
                    break;

                case ID_ACTIVITY:
                    replaceFragment(ActivityFragment.newInstance(), R.id.frame_layout);
                    break;

                case ID_NOTIFICATION:
                    replaceFragment(NotificationsFragment.newInstance(), R.id.frame_layout);
                    break;

                case ID_SETTINGS:
                    replaceFragment(SettingsFragment.newInstance(),R.id.frame_layout);
                    break;

                case ID_PROFILE:
                    replaceFragment(UserProfileFragment.newInstance(), R.id.frame_layout);
                    break;
            }
        });
        homeFragment.setInteraction(count -> {
            if (count > 0) {
                getViewDataBinding().bottomNavigation.setCount(ID_NOTIFICATION, String.valueOf(count));
            } else getViewDataBinding().bottomNavigation.clearCount(ID_NOTIFICATION);
        });
        getViewDataBinding().bottomNavigation.show(ID_HOME, true);
    }

}
