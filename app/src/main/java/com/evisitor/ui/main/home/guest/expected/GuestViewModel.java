package com.evisitor.ui.main.home.guest.expected;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.evisitor.data.DataManager;
import com.evisitor.data.model.Guests;
import com.evisitor.data.model.GuestsResponse;
import com.evisitor.ui.base.BaseViewModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GuestViewModel extends BaseViewModel<GuestNavigator> {
    private MutableLiveData<List<Guests>> guestListData = new MutableLiveData<>();
    public GuestViewModel(DataManager dataManager) {
        super(dataManager);
    }

    MutableLiveData<List<Guests>> getGuestListData(int page, String search) {
        getNavigator().showLoading();
        Map<String, String> map = new HashMap<>();
        map.put("accountId", getDataManager().getAccountId());
        if (!search.isEmpty())
        map.put("search", search);
        map.put("page", ""+page);
        map.put("offset", search);
        List<Guests> list = new ArrayList<>();
        getDataManager().doGetExpectedGuestListDetail(getDataManager().getHeader(),map).enqueue(new Callback<GuestsResponse>() {
            @Override
            public void onResponse(@NonNull Call<GuestsResponse> call,@NonNull  Response<GuestsResponse> response) {
                getNavigator().hideLoading();
                if (response.code()==200){
                    assert response.body()!=null;
                    if (response.body().getContent().size()>0){
                        list.addAll(response.body().getContent());
                    }
                }else getNavigator().handleApiError(response.errorBody());
            }
            @Override
            public void onFailure(@NonNull Call<GuestsResponse> call,@NonNull Throwable t) {
                getNavigator().hideLoading();
                getNavigator().handleApiFailure(t);
            }
        });
        guestListData.setValue(list);
        return guestListData;
    }
}
