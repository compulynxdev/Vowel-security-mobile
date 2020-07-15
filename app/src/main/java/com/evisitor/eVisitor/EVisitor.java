package com.evisitor.eVisitor;

import android.app.Application;

import com.evisitor.eVisitor.data.AppDataManager;
import com.evisitor.eVisitor.data.DataManager;

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
    }

}
