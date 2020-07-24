package com.evisitor.ui.main.settings.content;

import com.evisitor.data.DataManager;
import com.evisitor.ui.base.BaseNavigator;
import com.evisitor.ui.base.BaseViewModel;


/**
 * Created by Hemant Sharma on 15-01-20.
 * Divergent software labs pvt. ltd
 */
public class ContentViewModel extends BaseViewModel<BaseNavigator> {

    public ContentViewModel(DataManager dataManager) {
        super(dataManager);
    }

    /*private MutableLiveData<ContentResponse> responseMutableLiveData = new MutableLiveData<>();
    private Callback<ResponseBody> callback = new Callback<ResponseBody>() {
        @Override
        public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
            getNavigator().hideLoading();
            if (response.code() == 200) {
                try {
                    assert response.body() != null;
                    ContentResponse contentResponse = getDataManager().getGson().fromJson(response.body().string(), ContentResponse.class);
                    if (contentResponse.getError_code() == 200 && contentResponse.getStatus() == 1) {
                        responseMutableLiveData.setValue(contentResponse);
                    } else
                        getNavigator().handleApiError(contentResponse.getError_code(), contentResponse.getMessage());
                } catch (Exception e) {
                    getNavigator().showAlert(R.string.app_name, e.toString());
                }
            } else {
                getNavigator().hideLoading();
                getNavigator().handleApiError(response.errorBody());
            }
        }

        @Override
        public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
            getNavigator().hideLoading();
            getNavigator().handleApiFailure(t);
        }
    };
    MutableLiveData<ContentResponse> getContent(String from) {
        if (getNavigator().isNetworkConnected(true)) {
            getNavigator().showLoading();
            switch (from != null ? from : "") {
                case AppConstants.ACTIVITY_ABOUT_US:
                    getDataManager().doGetAboutUs(getDataManager().getHeader(false)).enqueue(callback);
                    break;

                case AppConstants.ACTIVITY_PRIVACY:
                    getDataManager().doGetPrivacyPolicy(getDataManager().getHeader(false)).enqueue(callback);
                    break;

                default:
                    getNavigator().hideLoading();
                    break;
            }
        }
        return responseMutableLiveData;
    }*/
}
