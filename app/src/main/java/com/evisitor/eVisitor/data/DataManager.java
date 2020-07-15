package com.evisitor.eVisitor.data;

import com.evisitor.eVisitor.data.local.db.DBHelper;
import com.evisitor.eVisitor.data.local.prefs.PreferenceHelper;
import com.evisitor.eVisitor.data.remote.ApiHelper;

/**
 * Created by Priyanka Joshi on 14-07-2020.
 */
public interface DataManager extends ApiHelper, PreferenceHelper, DBHelper {

}
