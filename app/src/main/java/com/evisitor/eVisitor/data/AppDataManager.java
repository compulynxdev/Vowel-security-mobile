package com.evisitor.eVisitor.data;

import android.content.Context;
import com.evisitor.eVisitor.data.local.db.AppDBHelper;
import com.evisitor.eVisitor.data.local.prefs.AppPreferenceHelper;
import com.evisitor.eVisitor.data.remote.AppApiHelper;

/**
 * Created by Priyanka Joshi on 14-07-2020.
 */
public class AppDataManager implements DataManager {

    private AppApiHelper apiHelper;
    private AppPreferenceHelper preferenceHelper;
    private AppDBHelper dbHelper;
    private static AppDataManager instance;

    public AppDataManager(Context context) {
        apiHelper = AppApiHelper.getAppApiInstance();
        preferenceHelper = new AppPreferenceHelper(context);
        dbHelper = AppDBHelper.getInstance(context);

    }

    public static synchronized AppDataManager getInstance(Context context){
        if (instance==null){
            instance = new AppDataManager(context);
        }
        return instance;
    }

}
