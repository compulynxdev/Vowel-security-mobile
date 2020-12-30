package com.evisitor.ui.main.commercial.add.whomtomeet;

import com.evisitor.data.model.CommercialStaffResponse;
import com.evisitor.data.model.HouseDetailBean;

public interface WhomToMeetCallback {
    void onLastLevelClick(HouseDetailBean houseDetailBean);

    void onStaffClick(CommercialStaffResponse staffDetail);
}
