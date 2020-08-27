package com.evisitor.data.model;

import com.google.gson.annotations.SerializedName;

public class ServiceProvider {


    @SerializedName("fullName")
    private String name;

    @SerializedName("createdDate")
    private String createdDate;

    @SerializedName("checkInTime")
    private String checkInTime;

    @SerializedName("checkOutTime")
    private String checkOutTime;

    @SerializedName("profile")
    private String profile;

    @SerializedName("expectedDate")
    private String time;

    @SerializedName("flatNo")
    private String houseNo;

    @SerializedName("residentName")
    private String host;

    @SerializedName("documentId")
    private String identityNo;

    @SerializedName("contactNo")
    private String contactNo;

    @SerializedName("residentId")
    private String residentId;

    @SerializedName("flatId")
    private String flatId;

    @SerializedName("id")
    private String serviceProviderId;

    //@SerializedName("image")
    private String url;

    @SerializedName("createdBy")
    private String createdBy;

    @SerializedName("expectedVehicleNo")
    private String expectedVehicleNo;


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
        return houseNo == null || houseNo.isEmpty() ? "" : houseNo;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    public String getHost() {
        return host == null ? "" : host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getExpectedVehicleNo() {
        return expectedVehicleNo;
    }

    public void setExpectedVehicleNo(String expectedVehicleNo) {
        this.expectedVehicleNo = expectedVehicleNo;
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

    public String getServiceProviderId() {
        return serviceProviderId;
    }

    public void setServiceProviderId(String serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getResidentId() {
        return residentId;
    }

    public void setResidentId(String residentId) {
        this.residentId = residentId;
    }

    public String getFlatId() {
        return flatId;
    }

    public void setFlatId(String flatId) {
        this.flatId = flatId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
