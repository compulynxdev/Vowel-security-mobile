package com.evisitor.data.model;

import com.google.gson.annotations.SerializedName;

public class HouseKeeping {


    @SerializedName("fullName")
    private String name;

    @SerializedName("createdDate")
    private String createdDate;

    @SerializedName("checkInTime")
    private String checkInTime;


    @SerializedName("checkOutTime")
    private String checkOutTime;

    @SerializedName("flatNo")
    private String houseNo;

    @SerializedName("residentName")
    private String host;

    @SerializedName("documentId")
    private String identityNo;

    @SerializedName("contactNo")
    private String contactNo;

    @SerializedName("createdBy")
    private String createdBy;

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    @SerializedName("profile")
    private String profile;

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
/* @SerializedName("premiseHierarchyDetailsId")
    private String flatId;*/

    @SerializedName("id")
    private String houseKeeperId;

    //@SerializedName("image")
    private String url;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(String checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getIdentityNo() {
        return identityNo;
    }

    public void setIdentityNo(String identityNo) {
        this.identityNo = identityNo;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getHouseKeeperId() {
        return houseKeeperId;
    }

    public void setHouseKeeperId(String houseKeeperId) {
        this.houseKeeperId = houseKeeperId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
