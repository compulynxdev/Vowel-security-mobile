package com.evisitor;

import android.app.Application;

import com.evisitor.data.AppDataManager;
import com.evisitor.util.AppLogger;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Objects;

/**
 * Created by Priyanka Joshi on 14-07-2020.
 * Divergent software labs pvt. ltd
 */
public class EVisitor extends Application {

    private static EVisitor instance;
    private AppDataManager appInstance;

    public static synchronized EVisitor getInstance() {
        if (instance != null) {
            return instance;
        }
        return new EVisitor();
    }

    public AppDataManager getDataManager() {
        return appInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        appInstance = AppDataManager.getInstance(this);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        AppLogger.d("Firebase getInstanceId failed " , String.valueOf(task.getException()));
                        return;
                    }
                    // Get new Instance ID token
                    String token = Objects.requireNonNull(task.getResult()).getToken();
                    AppLogger.d("Registration Token = " , token);
                    //TODO register token to your server.
                });
    }

}
