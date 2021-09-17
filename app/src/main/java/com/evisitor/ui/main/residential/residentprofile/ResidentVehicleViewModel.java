package com.evisitor.ui.main.residential.residentprofile;

import android.graphics.Bitmap;

import com.evisitor.R;
import com.evisitor.data.DataManager;
import com.evisitor.data.model.ResidentProfile;
import com.evisitor.ui.base.BaseNavigator;
import com.evisitor.ui.base.BaseViewModel;
import com.evisitor.ui.dialog.AlertDialog;
import com.evisitor.util.AppConstants;
import com.evisitor.util.AppLogger;
import com.evisitor.util.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.MutableLiveData;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResidentVehicleViewModel extends BaseViewModel<BaseNavigator> {
    private final MutableLiveData<String> numberPlate = new MutableLiveData<>();

    public ResidentVehicleViewModel(DataManager dataManager) {
        super(dataManager);
    }

    public MutableLiveData<String> getNumberPlate() {
        return numberPlate;
    }

    void numberPlateVerification(Bitmap bitmap){
        MultipartBody.Part body = AppUtils.prepareFilePart("upload","image/png",AppUtils.bitmapToFile(getNavigator().getContext(),bitmap, UUID.randomUUID().toString().concat(".png")));
        getDataManager().doNumberPlateDetails(AppConstants.TOKEN.concat(" ").concat(getDataManager().getUserDetail().getApiKey()),body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    if (response.code() == 201) {
                        assert response.body() != null;
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        if(jsonObject.has("results")){
                            JSONArray array = jsonObject.getJSONArray("results");
                            if(array.length()>0){
                                if(array.getJSONObject(0).has("plate")){
                                    String numerPlate = array.getJSONObject(0).getString("plate");
                                    numberPlate.setValue(numerPlate);
                                }
                            }
                        }
                    } else if (response.code() == 401) {
                        getNavigator().openActivityOnTokenExpire();
                    } else getNavigator().handleApiError(response.errorBody());
                } catch (Exception e) {
                    AppLogger.w("VisitorProfileViewModel", e.toString());
                }

            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                getNavigator().hideLoading();
                getNavigator().handleApiFailure(t);
            }
        });
    }
}
