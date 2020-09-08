package com.evisitor;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.evisitor.data.DataManager;
import com.evisitor.ui.dialog.AlertViewModel;
import com.evisitor.ui.dialog.ImagePickViewModel;
import com.evisitor.ui.login.LoginViewModel;
import com.evisitor.ui.main.MainViewModel;
import com.evisitor.ui.main.activity.ActivityViewModel;
import com.evisitor.ui.main.activity.checkin.CheckInViewModel;
import com.evisitor.ui.main.activity.checkout.CheckOutViewModel;
import com.evisitor.ui.main.home.HomeViewModel;
import com.evisitor.ui.main.home.blacklist.BlackListViewModel;
import com.evisitor.ui.main.home.guest.GuestViewModel;
import com.evisitor.ui.main.home.guest.add.AddGuestViewModel;
import com.evisitor.ui.main.home.guest.add.dialogs.HostPickerViewModel;
import com.evisitor.ui.main.home.guest.add.dialogs.PickerViewModel;
import com.evisitor.ui.main.home.guest.expected.ExpectedGuestViewModel;
import com.evisitor.ui.main.home.housekeeping.HKViewModel;
import com.evisitor.ui.main.home.housekeeping.expected.ExpectedHKViewModel;
import com.evisitor.ui.main.home.housekeeping.registered.RegisteredHKViewModel;
import com.evisitor.ui.main.home.scan.ScanIDViewModel;
import com.evisitor.ui.main.home.sp.ExpectedSpViewModel;
import com.evisitor.ui.main.home.sp.SPViewModel;
import com.evisitor.ui.main.home.total.TotalVisitorsViewModel;
import com.evisitor.ui.main.home.trespasser.TrespasserViewModel;
import com.evisitor.ui.main.home.trespasser.guests.TrespasserGuestViewModel;
import com.evisitor.ui.main.home.trespasser.services.TrespasserSPViewModel;
import com.evisitor.ui.main.idverification.IdVerificationViewModel;
import com.evisitor.ui.main.notifications.NotificationsFragmentViewModel;
import com.evisitor.ui.main.profile.UserProfileViewModel;
import com.evisitor.ui.main.settings.SettingsViewModel;
import com.evisitor.ui.main.settings.content.ContentViewModel;
import com.evisitor.ui.main.settings.info.DeviceInfoViewModel;
import com.evisitor.ui.main.settings.language.LanguageDialogViewModel;
import com.evisitor.ui.main.visitorprofile.VisitorProfileViewModel;
import com.evisitor.ui.splash.SplashViewModel;

/**
 * Created by Priyanka Joshi on 14-07-2020.
 * Divergent software labs pvt. ltd
 */
public class ViewModelProviderFactory extends ViewModelProvider.NewInstanceFactory {
    private static ViewModelProviderFactory instance;
    private DataManager dataManager;

    private ViewModelProviderFactory(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public static synchronized ViewModelProviderFactory getInstance(){
        if (instance == null) {
            instance = new ViewModelProviderFactory(EVisitor.getInstance().getDataManager());
        }
        return instance;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SplashViewModel.class)) {
            //noinspection unchecked
            return (T) new SplashViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            //noinspection unchecked
            return (T) new LoginViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(MainViewModel.class)) {
            //noinspection unchecked
            return (T) new MainViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(HomeViewModel.class)) {
            //noinspection unchecked
            return (T) new HomeViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(ActivityViewModel.class)) {
            //noinspection unchecked
            return (T) new ActivityViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(NotificationsFragmentViewModel.class)) {
            //noinspection unchecked
            return (T) new NotificationsFragmentViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(SettingsViewModel.class)) {
            //noinspection unchecked
            return (T) new SettingsViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(UserProfileViewModel.class)) {
            //noinspection unchecked
            return (T) new UserProfileViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(CheckInViewModel.class)) {
            //noinspection unchecked
            return (T) new CheckInViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(CheckOutViewModel.class)) {
            //noinspection unchecked
            return (T) new CheckOutViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(LanguageDialogViewModel.class)) {
            //noinspection unchecked
            return (T) new LanguageDialogViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(DeviceInfoViewModel.class)) {
            //noinspection unchecked
            return (T) new DeviceInfoViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(ContentViewModel.class)) {
            //noinspection unchecked
            return (T) new ContentViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(AlertViewModel.class)) {
            //noinspection unchecked
            return (T) new AlertViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(VisitorProfileViewModel.class)) {
            //noinspection unchecked
            return (T) new VisitorProfileViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(GuestViewModel.class)) {
            //noinspection unchecked
            return (T) new GuestViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(AddGuestViewModel.class)) {
            //noinspection unchecked
            return (T) new AddGuestViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(PickerViewModel.class)) {
            //noinspection unchecked
            return (T) new PickerViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(ImagePickViewModel.class)) {
            //noinspection unchecked
            return (T) new ImagePickViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(HostPickerViewModel.class)) {
            //noinspection unchecked
            return (T) new HostPickerViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(ScanIDViewModel.class)) {
            //noinspection unchecked
            return (T) new ScanIDViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(SPViewModel.class)) {
            //noinspection unchecked
            return (T) new SPViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(IdVerificationViewModel.class)) {
            //noinspection unchecked
            return (T) new IdVerificationViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(ExpectedGuestViewModel.class)) {
            //noinspection unchecked
            return (T) new ExpectedGuestViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(ExpectedSpViewModel.class)) {
            //noinspection unchecked
            return (T) new ExpectedSpViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(ExpectedHKViewModel.class)) {
            //noinspection unchecked
            return (T) new ExpectedHKViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(HKViewModel.class)) {
            //noinspection unchecked
            return (T) new HKViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(RegisteredHKViewModel.class)) {
            //noinspection unchecked
            return (T) new RegisteredHKViewModel(dataManager);
        }else if (modelClass.isAssignableFrom(TotalVisitorsViewModel.class)) {
            //noinspection unchecked
            return (T) new TotalVisitorsViewModel(dataManager);
        }else if (modelClass.isAssignableFrom(BlackListViewModel.class)) {
            //noinspection unchecked
            return (T) new BlackListViewModel(dataManager);
        }else if (modelClass.isAssignableFrom(TrespasserViewModel.class)) {
            //noinspection unchecked
            return (T) new TrespasserViewModel(dataManager);
        }else if (modelClass.isAssignableFrom(TrespasserGuestViewModel.class)) {
            //noinspection unchecked
            return (T) new TrespasserGuestViewModel(dataManager);
        }else if (modelClass.isAssignableFrom(TrespasserSPViewModel.class)) {
            //noinspection unchecked
            return (T) new TrespasserSPViewModel(dataManager);
        }

        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
