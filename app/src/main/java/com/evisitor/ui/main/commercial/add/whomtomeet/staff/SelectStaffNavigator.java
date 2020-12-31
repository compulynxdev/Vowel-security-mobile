package com.evisitor.ui.main.commercial.add.whomtomeet.staff;


import com.evisitor.data.model.SelectCommercialStaffResponse;
import com.evisitor.ui.base.BaseNavigator;

import java.util.List;

/**
 * Created by Hemant Sharma on 23-02-20.
 */
interface SelectStaffNavigator extends BaseNavigator {

    void onStaffDataReceived(List<SelectCommercialStaffResponse> staffList);

}
