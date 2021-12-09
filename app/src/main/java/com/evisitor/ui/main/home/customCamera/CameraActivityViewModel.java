package com.evisitor.ui.main.home.customCamera;

import android.graphics.Bitmap;

import com.evisitor.R;
import com.evisitor.data.DataManager;
import com.evisitor.data.model.MrzResponse;
import com.evisitor.ui.base.BaseViewModel;
import com.evisitor.util.AppUtils;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CameraActivityViewModel  extends BaseViewModel<CameraActivityNavigator> {
    public CameraActivityViewModel(DataManager dataManager) {
        super(dataManager);
    }


    public void documentMrzExtraction(Bitmap bitmap, String absolutePath, MRZCallback callback){

        File file = AppUtils.bitmapToFile(getNavigator().getContext(),bitmap,absolutePath);
        RequestBody description = RequestBody.create(MultipartBody.FORM,"imagefile");
        RequestBody fileBody = RequestBody.create(MediaType.parse("imagefile"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("imagefile", file.getName(), fileBody);

        getDataManager().doPostDocument(description,body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (response.code() == 200) {
                            assert response.body() != null;
                            MrzResponse mrzResponse = getDataManager().getGson().fromJson(response.body().string(),MrzResponse.class);
                            if (mrzResponse != null) {
                                callback.onMrzSuccess(mrzResponse);
                            }
                        } else if (response.code() == 401) {
                            getNavigator().openActivityOnTokenExpire();
                        } else callback.OnError(response.errorBody().string());
                    } catch (Exception e) {
                        getNavigator().showAlert(R.string.alert, R.string.alert_error);
                    }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                getNavigator().handleApiFailure(t);
            }
        });
    }
}
