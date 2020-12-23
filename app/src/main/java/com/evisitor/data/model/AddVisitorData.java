package com.evisitor.data.model;

import android.graphics.Bitmap;

public class AddVisitorData {
    public boolean isGuest;

    //guest default data
    public boolean isAccept;
    public Bitmap bmp_profile;
    public String identityNo;
    public String idType;
    public String name;
    public String vehicleNo;
    public String contact;
    public String dialingCode;
    public String address;
    public String gender;
    public String houseId;//department for commercial
    public String residentId;
    //reject reason
    public String rejectedReason;

    //sp data
    public boolean isResident;
    public boolean isCompany;
    public String assignedTo = "";
    public String visitorType;
    public String employment;
    public String profile;
    public String companyName = "";
    public String companyAddress = "";

    //commercial
    public String purpose;
}
