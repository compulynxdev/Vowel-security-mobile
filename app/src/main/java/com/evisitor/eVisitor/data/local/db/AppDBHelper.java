package com.evisitor.eVisitor.data.local.db;


import android.content.Context;

import com.evisitor.eVisitor.data.model.DaoMaster;
import com.evisitor.eVisitor.data.model.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * Created by Priyanka Joshi on 14-07-2020.
 */
public class AppDBHelper implements DBHelper {
    private DaoSession daoSession;
    private static AppDBHelper instance;

    public AppDBHelper(Context context) {

        DaoMaster.DevOpenHelper devHelper = new DaoMaster.DevOpenHelper(context,"EVisitorManagement");
        Database db = devHelper.getWritableDb();

        daoSession = new DaoMaster(db).newSession();
    }

    public static AppDBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new AppDBHelper(context);
        }
        return instance;
    }
}
