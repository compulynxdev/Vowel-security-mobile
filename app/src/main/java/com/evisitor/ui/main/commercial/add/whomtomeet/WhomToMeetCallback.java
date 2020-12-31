package com.evisitor.ui.main.commercial.add.whomtomeet;

import com.evisitor.data.model.HouseDetailBean;
import com.evisitor.data.model.SelectCommercialStaffResponse;

public interface WhomToMeetCallback {
    void onLastLevelClick(HouseDetailBean houseDetailBean);

    void onStaffClick(SelectCommercialStaffResponse staffDetail);
}
