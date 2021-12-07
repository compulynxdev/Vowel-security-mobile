package com.evisitor.ui.main.residential.staff.registered;

import androidx.annotation.NonNull;

import com.evisitor.R;
import com.evisitor.data.DataManager;
import com.evisitor.data.model.HouseKeepingResponse;
import com.evisitor.data.model.VisitorProfileBean;
import com.evisitor.ui.base.BaseViewModel;
import com.evisitor.util.AppConstants;
import com.evisitor.util.AppLogger;
import com.evisitor.util.AppUtils;
import com.evisitor.util.CalenderUtils;
import com.evisitor.util.CommonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisteredHKViewModel extends BaseViewModel<RegisteredHKNavigator> {

    public RegisteredHKViewModel(DataManager dataManager) {
        super(dataManager);
    }

    void getRegisteredHKListData(int page, String search) {
        if (getNavigator().isNetworkConnected()) {
            Map<String, String> map = new HashMap<>();
            map.put("accountId", getDataManager().getAccountId());
            if (!search.isEmpty())
                map.put("search", search);
            map.put("page", "" + page);
            map.put("size", String.valueOf(AppConstants.LIMIT));
            AppLogger.d("Searching : Registered", page + " : " + search);

            getDataManager().doGetRegisteredHKList(getDataManager().getHeader(), map).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    getNavigator().hideLoading();
                    getNavigator().hideSwipeToRefresh();
                    try {
                        if (response.code() == 200) {
                            assert response.body() != null;
                            HouseKeepingResponse registeredHKResponse = getDataManager().getGson().fromJson(response.body().string(), HouseKeepingResponse.class);
                            if (registeredHKResponse.getContent() != null) {
                                AppLogger.d("Searching : Size", "" + registeredHKResponse.getContent().size());
                                getNavigator().onRegisteredHKSuccess(registeredHKResponse.getContent());
                            }
                        } else if (response.code() == 401) {
                            getNavigator().openActivityOnTokenExpire();
                        } else getNavigator().handleApiError(response.errorBody());
                    } catch (Exception e) {
                        getNavigator().showAlert(R.string.alert, R.string.alert_error);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    getNavigator().hideSwipeToRefresh();
                    getNavigator().hideLoading();
                    getNavigator().handleApiFailure(t);
                }
            });
        } else {
            getNavigator().hideSwipeToRefresh();
            getNavigator().hideLoading();
        }
    }

    List<VisitorProfileBean> setClickVisitorDetail(HouseKeepingResponse.ContentBean bean) {
        getNavigator().showLoading();
        List<VisitorProfileBean> visitorProfileBeanList = new ArrayList<>();
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_name, bean.getFullName())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_profile, bean.getProfile())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_days_slot), VisitorProfileBean.VIEW_TYPE_DAYS, bean.getWorkingDays()));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_time_slot, CalenderUtils.formatDateWithOutUTC(bean.getTimeIn(), CalenderUtils.TIME_FORMAT, CalenderUtils.TIME_FORMAT_AM), CalenderUtils.formatDateWithOutUTC(bean.getTimeOut(), CalenderUtils.TIME_FORMAT, CalenderUtils.TIME_FORMAT_AM))));
        if (!bean.getContactNo().isEmpty())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_mobile, bean.getContactNo().isEmpty() ? getNavigator().getContext().getString(R.string.na) : CommonUtils.paritalEncodeData("".concat(bean.getDialingCode()).concat(" ").concat(bean.getContactNo())))));
        if (!bean.getDocumentId().isEmpty()) {
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_identity_type, bean.getDocumentType().isEmpty() ? getNavigator().getContext().getString(R.string.na) : getIdentityType(bean.getDocumentType()))));
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_identity, CommonUtils.paritalEncodeData(bean.getDocumentId()))));
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_nationality, bean.getNationality().isEmpty() ? getNavigator().getContext().getString(R.string.na) : bean.getNationality())));
        }

        if (!getDataManager().isCommercial() && !bean.getFlatNo().isEmpty()) {
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_dynamic_premise, getDataManager().getLevelName(), bean.getPremiseName().isEmpty() ? getNavigator().getContext().getString(R.string.na) : bean.getPremiseName())));
        }

        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_comp_name, bean.getCompanyName().isEmpty() ? getNavigator().getContext().getString(R.string.na) : bean.getCompanyName())));
        if (bean.getResidentName() != null && !bean.getResidentName().isEmpty())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_register_by, bean.getResidentName())));
        else if (bean.getCreatedBy() != null && !bean.getCreatedBy().isEmpty())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_register_by, bean.getCreatedBy())));
        if (bean.getCreatedDate() != null && !bean.getCreatedDate().isEmpty())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_register_date, CalenderUtils.formatDate(bean.getCreatedDate(), CalenderUtils.SERVER_DATE_FORMAT2, CalenderUtils.CUSTOM_TIMESTAMP_FORMAT_SLASH))));

        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.visitor_mode, bean.getMode())));

        getNavigator().hideLoading();
        return visitorProfileBeanList;
    }

    private String getHKDays(List<String> days) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String day : days) {
            stringBuilder.append(AppUtils.capitaliseFirstLetter(day)).append(", ");
        }
        if (stringBuilder.toString().trim().endsWith(",")) {
            stringBuilder = new StringBuilder(stringBuilder.substring(0, stringBuilder.toString().trim().length() - 1));
        }

        return stringBuilder.toString();
    }
}
