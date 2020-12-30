package com.evisitor.ui.main.commercial.add.whomtomeet.level;


import com.evisitor.data.model.HouseDetailBean;
import com.evisitor.ui.base.BaseNavigator;

import java.util.List;

/**
 * Created by Hemant Sharma on 23-02-20.
 */
interface SelectLastLevelNavigator extends BaseNavigator {

    void onLastLevelDataReceived(List<HouseDetailBean> lastLevelDataList);

}
