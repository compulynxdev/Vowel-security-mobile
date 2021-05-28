package com.evisitor.util;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.Settings;
import android.util.Patterns;

import androidx.annotation.NonNull;

import com.evisitor.EVisitor;
import com.evisitor.R;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Created by Hemant Sharma on 23-02-20.
 */

public final class CommonUtils {

    private CommonUtils() {
        // This utility class is not publicly instantiable
    }

    @SuppressLint("HardwareIds")
    public static String getDeviceId(@NonNull Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    public static String paritalEncodeData(String data) {
        String obj = data.replace("+", "");
        return data.replace(" ", "").replaceAll("\\w(?=\\w{4})", "*");
    }

    public static String loadJSONFromAsset(Context context, String jsonFileName) {
        try {
            InputStream is = context.getAssets().open(jsonFileName);

            int size = is.available();
            byte[] buffer = new byte[size];
            int count = is.read(buffer);
            is.close();
            AppLogger.d("CommonUtils", "" + count);
            return new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            return null;
        }
    }

    public static ProgressDialog showLoadingDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        if (progressDialog.getWindow() != null) {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        progressDialog.setContentView(R.layout.custom_progress);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }

    public static String getVisitorType(String type) {
        if (AppConstants.SERVICE_PROVIDER.equalsIgnoreCase(type)) {
            return AppConstants.SP_LABEL;
        } else if (AppConstants.GUEST.equalsIgnoreCase(type)) {
            return EVisitor.getInstance().getDataManager().isCommercial() ? AppConstants.VISITOR_LABEL : AppUtils.capitaliseFirstLetter(type);
        }
        return AppUtils.capitaliseFirstLetter(type);
    }
}
