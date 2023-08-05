package com.evisitor.data.model;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class AddVisitorData {
    public boolean isGuest;

    //recurrent guest
    public String imageUrl = "";

    //guest default data
    public boolean isAccept;
    public Bitmap bmp_profile;
    public String identityNo;
    public String idType;
    public String nationality = "";
    public String name;
    public String vehicleNo;
    public String vehicleModel;
    public String contact;
    public String dialingCode;
    public String address;
    public String gender;
    public String houseId;//department for commercial
    public boolean isStaffSelect;//for staff selection commercial
    public String residentId;
    public String visitorCompanyName;
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
    public String bodyTemperature = "";



    //commercial
    public String purpose;
    public List<DeviceBean> deviceBeanList = new ArrayList<>();
    public List<SecondaryGuest> guestList = new ArrayList<>();

    public String mode;
    public String groupType;
    public Bitmap vehicalNoPlateBitMapImg;










}
