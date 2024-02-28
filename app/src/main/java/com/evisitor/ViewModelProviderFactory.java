package com.evisitor;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.evisitor.data.DataManager;
import com.evisitor.ui.dialog.AlertViewModel;
import com.evisitor.ui.dialog.ImagePickViewModel;
import com.evisitor.ui.dialog.country.CountrySelectionDialogViewModel;
import com.evisitor.ui.dialog.selection.SelectionViewModel;
import com.evisitor.ui.image.ImageViewModel;
import com.evisitor.ui.login.LoginViewModel;
import com.evisitor.ui.login.forgotpassword.ForgotPasswordViewModel;
import com.evisitor.ui.main.MainViewModel;
import com.evisitor.ui.main.activity.ActivityViewModel;
import com.evisitor.ui.main.activity.checkin.CheckInViewModel;
import com.evisitor.ui.main.activity.checkout.CheckOutViewModel;
import com.evisitor.ui.main.commercial.add.CommercialAddVisitorViewModel;
import com.evisitor.ui.main.commercial.add.whomtomeet.WhomToMeetViewModel;
import com.evisitor.ui.main.commercial.add.whomtomeet.level.SelectLastLevelViewModel;
import com.evisitor.ui.main.commercial.add.whomtomeet.staff.SelectStaffViewModel;
import com.evisitor.ui.main.commercial.gadgets.GadgetsInputViewModel;
import com.evisitor.ui.main.commercial.secondryguest.SecoundryGuestInputViewModel;
import com.evisitor.ui.main.commercial.staff.CommercialStaffViewModel;
import com.evisitor.ui.main.commercial.staff.expected.ExpectedCommercialStaffViewModel;
import com.evisitor.ui.main.commercial.staff.registered.RegisteredCommercialStaffViewModel;
import com.evisitor.ui.main.commercial.visitor.VisitorViewModel;
import com.evisitor.ui.main.commercial.visitor.expected.ExpectedCommercialGuestViewModel;
import com.evisitor.ui.main.home.HomeViewModel;
import com.evisitor.ui.main.home.blacklist.BlackListViewModel;
import com.evisitor.ui.main.home.flag.FlagVisitorViewModel;
import com.evisitor.ui.main.home.idverification.IdVerificationViewModel;
import com.evisitor.ui.main.home.recurrentvisitor.FilterRecurrentVisitorViewModel;
import com.evisitor.ui.main.home.rejected.RejectedVisitorViewModel;
import com.evisitor.ui.main.home.rejected.guest.RejectedGuestViewModel;
import com.evisitor.ui.main.home.rejected.sp.RejectedSPViewModel;
import com.evisitor.ui.main.home.rejected.staff.RejectedStaffViewModel;
import com.evisitor.ui.main.home.rejectreason.InputDialogViewModel;
import com.evisitor.ui.main.home.scan.BarcodeScanViewModel;
import com.evisitor.ui.main.home.customCamera.CameraActivityViewModel;
import com.evisitor.ui.main.home.scan.ScanIDViewModel;
import com.evisitor.ui.main.home.total.TotalVisitorsViewModel;
import com.evisitor.ui.main.home.trespasser.TrespasserViewModel;
import com.evisitor.ui.main.home.trespasser.guests.TrespasserGuestViewModel;
import com.evisitor.ui.main.home.trespasser.services.TrespasserSPViewModel;
import com.evisitor.ui.main.home.tutorial.TutorialScanViewModel;
import com.evisitor.ui.main.home.visitorprofile.VisitorProfileViewModel;
import com.evisitor.ui.main.notifications.NotificationsViewModel;
import com.evisitor.ui.main.profile.UserProfileViewModel;
import com.evisitor.ui.main.residential.add.AddVisitorViewModel;
import com.evisitor.ui.main.residential.guest.GuestViewModel;
import com.evisitor.ui.main.residential.guest.expected.ExpectedGuestViewModel;
import com.evisitor.ui.main.residential.residentprofile.ResidentProfileViewModel;
import com.evisitor.ui.main.residential.residentprofile.ResidentVehicleViewModel;
import com.evisitor.ui.main.residential.sp.ExpectedSpViewModel;
import com.evisitor.ui.main.residential.sp.SPViewModel;
import com.evisitor.ui.main.residential.staff.HKViewModel;
import com.evisitor.ui.main.residential.staff.expected.ExpectedHKViewModel;
import com.evisitor.ui.main.residential.staff.registered.RegisteredHKViewModel;
import com.evisitor.ui.main.settings.SettingsViewModel;
import com.evisitor.ui.main.settings.content.ContentViewModel;
import com.evisitor.ui.main.settings.info.DeviceInfoViewModel;
import com.evisitor.ui.main.settings.language.LanguageDialogViewModel;
import com.evisitor.ui.main.settings.propertyinfo.PropertyInfoViewModel;
import com.evisitor.ui.splash.SplashViewModel;

/**
 * Created by Priyanka Joshi on 14-07-2020.
 * Divergent software labs pvt. ltd
 */
public class ViewModelProviderFactory extends ViewModelProvider.NewInstanceFactory {
    private static ViewModelProviderFactory instance;
    private final DataManager dataManager;

    private ViewModelProviderFactory(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public static synchronized ViewModelProviderFactory getInstanceM() {
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
        } else if (modelClass.isAssignableFrom(NotificationsViewModel.class)) {
            //noinspection unchecked
            return (T) new NotificationsViewModel(dataManager);
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
        } else if (modelClass.isAssignableFrom(VisitorViewModel.class)) {
            //noinspection unchecked
            return (T) new VisitorViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(AddVisitorViewModel.class)) {
            //noinspection unchecked
            return (T) new AddVisitorViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(SelectionViewModel.class)) {
            //noinspection unchecked
            return (T) new SelectionViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(ImagePickViewModel.class)) {
            //noinspection unchecked
            return (T) new ImagePickViewModel(dataManager);
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
        } else if (modelClass.isAssignableFrom(TotalVisitorsViewModel.class)) {
            //noinspection unchecked
            return (T) new TotalVisitorsViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(BlackListViewModel.class)) {
            //noinspection unchecked
            return (T) new BlackListViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(TrespasserViewModel.class)) {
            //noinspection unchecked
            return (T) new TrespasserViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(TrespasserGuestViewModel.class)) {
            //noinspection unchecked
            return (T) new TrespasserGuestViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(TrespasserSPViewModel.class)) {
            //noinspection unchecked
            return (T) new TrespasserSPViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(FlagVisitorViewModel.class)) {
            //noinspection unchecked
            return (T) new FlagVisitorViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(ImageViewModel.class)) {
            //noinspection unchecked
            return (T) new ImageViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(PropertyInfoViewModel.class)) {
            //noinspection unchecked
            return (T) new PropertyInfoViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(CountrySelectionDialogViewModel.class)) {
            //noinspection unchecked
            return (T) new CountrySelectionDialogViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(RejectedVisitorViewModel.class)) {
            //noinspection unchecked
            return (T) new RejectedVisitorViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(RejectedGuestViewModel.class)) {
            //noinspection unchecked
            return (T) new RejectedGuestViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(RejectedSPViewModel.class)) {
            //noinspection unchecked
            return (T) new RejectedSPViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(RejectedStaffViewModel.class)) {
            //noinspection unchecked
            return (T) new RejectedStaffViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(InputDialogViewModel.class)) {
            //noinspection unchecked
            return (T) new InputDialogViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(TutorialScanViewModel.class)) {
            //noinspection unchecked
            return (T) new TutorialScanViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(CommercialAddVisitorViewModel.class)) {
            //noinspection unchecked
            return (T) new CommercialAddVisitorViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(GadgetsInputViewModel.class)) {
            //noinspection unchecked
            return (T) new GadgetsInputViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(ExpectedCommercialGuestViewModel.class)) {
            //noinspection unchecked
            return (T) new ExpectedCommercialGuestViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(WhomToMeetViewModel.class)) {
            //noinspection unchecked
            return (T) new WhomToMeetViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(SelectLastLevelViewModel.class)) {
            //noinspection unchecked
            return (T) new SelectLastLevelViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(SelectStaffViewModel.class)) {
            //noinspection unchecked
            return (T) new SelectStaffViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(CommercialStaffViewModel.class)) {
            //noinspection unchecked
            return (T) new CommercialStaffViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(ExpectedCommercialStaffViewModel.class)) {
            //noinspection unchecked
            return (T) new ExpectedCommercialStaffViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(RegisteredCommercialStaffViewModel.class)) {
            //noinspection unchecked
            return (T) new RegisteredCommercialStaffViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(BarcodeScanViewModel.class)) {
            //noinspection unchecked
            return (T) new BarcodeScanViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(ResidentProfileViewModel.class)) {
            //noinspection unchecked
            return (T) new ResidentProfileViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(ForgotPasswordViewModel.class)) {
            //noinspection unchecked
            return (T) new ForgotPasswordViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(FilterRecurrentVisitorViewModel.class)) {
            //noinspection unchecked
            return (T) new FilterRecurrentVisitorViewModel(dataManager);
        } else if (modelClass.isAssignableFrom(SecoundryGuestInputViewModel.class)) {
            //noinspection unchecked
            return (T) new SecoundryGuestInputViewModel(dataManager);
        }else if (modelClass.isAssignableFrom(ResidentVehicleViewModel.class)) {
            //noinspection unchecked
            return (T) new ResidentVehicleViewModel(dataManager);
        }else if (modelClass.isAssignableFrom(CameraActivityViewModel.class)) {
            //noinspection unchecked
            return (T) new CameraActivityViewModel(dataManager);
        }

        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
