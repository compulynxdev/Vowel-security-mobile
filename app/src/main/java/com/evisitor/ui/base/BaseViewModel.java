package com.evisitor.ui.base;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.evisitor.R;
import com.evisitor.data.DataManager;
import com.evisitor.data.model.Configurations;
import com.evisitor.data.model.GuestConfigurationResponse;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseViewModel<N extends BaseNavigator> extends ViewModel {
    private final DataManager dataManager;
    private WeakReference<N> mNavigator;
    private HashMap<String, String> identityTypeList;
    private HashMap<String, String> visitorModeList;


    public BaseViewModel(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public N getNavigator() {
        return mNavigator.get();
    }

    public void setNavigator(N navigator) {
        this.mNavigator = new WeakReference<>(navigator);
    }

    public String getIdentityType(String key) {
        if (identityTypeList == null) {
            identityTypeList = new HashMap<>();
            identityTypeList.put("nationalId", "National ID");
            identityTypeList.put("dl", "Driving Licence");
            identityTypeList.put("passport", "Passport");
            identityTypeList.put("National ID", "National ID");
        }

        return identityTypeList.get(key);
    }

    public String getVisitorMode(String key) {
        if (visitorModeList == null) {
            visitorModeList = new HashMap<>();
            visitorModeList.put("Walk-In", "Walk-In");
            visitorModeList.put("walk-in", "Walk-In");
            visitorModeList.put("drive-in", "Drive-In");
        }

        return visitorModeList.get(key.toLowerCase());
    }

    public void getConfigurations(){
        try {
            if (getNavigator().isNetworkConnected()) {
                Map<String, String> map = new HashMap<>();
                map.put("accountId", getDataManager().getAccountId());
              getDataManager().doGetConfigurations(getDataManager().getHeader(), map).enqueue(new Callback<ResponseBody>() {
                  @Override
                  public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                      if (response.isSuccessful()){
                          try {
                               if (response.body() != null) {

                                   Configurations[] configurations = getDataManager().getGson().fromJson(response.body().string(), Configurations[].class);
                                   for (Configurations configuration : configurations) {
                                       if (configuration.getFeatureCode().equalsIgnoreCase("vhclmodel")) {
                                           getDataManager().setCaptureVehicleModel(configuration.getValue());
                                       }
                                   }
                               }
                          } catch (IOException e) {
                              e.printStackTrace();
                              //throw new RuntimeException(e);
                          }
                      }
                  }

                  @Override
                  public void onFailure(Call<ResponseBody> call, Throwable t) {
                      t.printStackTrace();
                      getNavigator().handleApiFailure(t);
                  }
              });
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void doGetGuestConfiguration(GuestConfigurationCallback callback) {
        boolean isShowMsg = true;
        if (callback == null) isShowMsg = false;

        if (getNavigator().isNetworkConnected(isShowMsg)) {
            Map<String, String> map = new HashMap<>();
            map.put("accountId", getDataManager().getAccountId());
            boolean finalIsShowMsg = isShowMsg;
            getDataManager().doGetGuestConfiguration(getDataManager().getHeader(), map).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if (response.code() == 200) {
                        try {
                            assert response.body() != null;
                            GuestConfigurationResponse configurationResponse = getDataManager().getGson().fromJson(response.body().string(), GuestConfigurationResponse.class);
                            configurationResponse.isDataUpdated = true;
                            getDataManager().setGuestConfiguration(configurationResponse);
                            if (callback != null) callback.onSuccess(configurationResponse);
                        } catch (Exception e) {
                            if (finalIsShowMsg) {
                                getNavigator().showAlert(R.string.alert, R.string.alert_error);
                            }
                        }
                        getConfigurations();
                    } else {
                        if (finalIsShowMsg) {
                            getNavigator().handleApiError(response.errorBody());
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    if (finalIsShowMsg) {
                        getNavigator().handleApiFailure(t);
                    }
                }
            });
        }
    }


    public interface GuestConfigurationCallback {
        void onSuccess(GuestConfigurationResponse configurationResponse);
    }

}
