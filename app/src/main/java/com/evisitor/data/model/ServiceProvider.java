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

    @SerializedName("dialingCode")
    private String dialingCode;

    @SerializedName("residentId")
    private String residentId;

    @SerializedName("flatId")
    private String flatId = "";

    @SerializedName("id")
    private String serviceProviderId;

    @SerializedName("image")
    private String imageUrl;

    @SerializedName("createdBy")
    private String createdBy;

    @SerializedName("expectedVehicleNo")
    private String expectedVehicleNo;

    @SerializedName("enteredVehicleNo")
    private String enteredVehicleNo = "";

    @SerializedName("checkOutFeature")
    private boolean checkOutFeature;

    @SerializedName("hostCheckOutTime")
    private String hostCheckOutTime;

    @SerializedName("isHostCheckOut")
    private boolean isHostCheckOut;


    @SerializedName("notificationStatus")
    private boolean notificationStatus;

    @SerializedName("state")
    private String status;

    @SerializedName("checkInStatus")
    private boolean checkInStatus;

    @SerializedName("rejectedBy")
    private String rejectedBy;

    @SerializedName("rejectReason")
    private String rejectedReason;

    @SerializedName("companyName")
    private String companyName;

    @SerializedName("companyAddress")
    private String companyAddress;

    @SerializedName("premiseName")
    private String premiseName;

    @SerializedName("rejectedOn")
    private String rejectedOn;

    public String getStatus() {
        return status == null ? "PENDING" : status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isNotificationStatus() {
        return notificationStatus;
    }

    public void setNotificationStatus(boolean notificationStatus) {
        this.notificationStatus = notificationStatus;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedDate() {
        return createdDate == null ? "" : createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCheckInTime() {
        return checkInTime == null ? "" : checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public boolean isCheckOutFeature() {
        return checkOutFeature;
    }

    public void setCheckOutFeature(boolean checkOutFeature) {
        this.checkOutFeature = checkOutFeature;
    }

    public String getHostCheckOutTime() {
        return hostCheckOutTime == null ? "" : hostCheckOutTime;
    }

    public void setHostCheckOutTime(String hostCheckOutTime) {
        this.hostCheckOutTime = hostCheckOutTime;
    }

    public boolean isHostCheckOut() {
        return isHostCheckOut;
    }

    public void setHostCheckOut(boolean hostCheckOut) {
        isHostCheckOut = hostCheckOut;
    }

    public String getCheckOutTime() {
        return checkOutTime == null ? "" : checkOutTime;
    }

    public void setCheckOutTime(String checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public String getHouseNo() {
        return houseNo == null ? "" : houseNo;
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
        return expectedVehicleNo == null ? "" : expectedVehicleNo;
    }

    public void setExpectedVehicleNo(String expectedVehicleNo) {
        this.expectedVehicleNo = expectedVehicleNo;
    }

    public String getIdentityNo() {
        return identityNo == null ? "" : identityNo;
    }

    public void setIdentityNo(String identityNo) {
        this.identityNo = identityNo;
    }

    public String getContactNo() {
        return contactNo == null ? "" : contactNo;
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

    public String getImageUrl() {
        return imageUrl == null ? "" : imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getResidentId() {
        return residentId == null ? "" : residentId;
    }

    public void setResidentId(String residentId) {
        this.residentId = residentId;
    }

    public String getFlatId() {
        return flatId == null ? "" : flatId;
    }

    public void setFlatId(String flatId) {
        this.flatId = flatId;
    }

    public String getCreatedBy() {
        return createdBy == null ? "" : createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getProfile() {
        return profile == null ? "" : profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getTime() {
        return time == null ? "" : time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEnteredVehicleNo() {
        return enteredVehicleNo == null || enteredVehicleNo.isEmpty() ? getExpectedVehicleNo() : enteredVehicleNo;
    }

    public void setEnteredVehicleNo(String enteredVehicleNo) {
        this.enteredVehicleNo = enteredVehicleNo;
    }

    public boolean getCheckInStatus() {
        return checkInStatus;
    }

    public void setCheckInStatus(boolean checkInStatus) {
        this.checkInStatus = checkInStatus;
    }

    public String getDialingCode() {
        return dialingCode == null ? "" : dialingCode;
    }

    public void setDialingCode(String dialingCode) {
        this.dialingCode = dialingCode;
    }

    public String getRejectedBy() {
        return rejectedBy == null ? "" : rejectedBy;
    }

    public void setRejectedBy(String rejectedBy) {
        this.rejectedBy = rejectedBy;
    }

    public String getRejectedReason() {
        return rejectedReason == null ? "" : rejectedReason;
    }

    public void setRejectedReason(String rejectedReason) {
        this.rejectedReason = rejectedReason;
    }

    public String getCompanyName() {
        return companyName == null ? "" : companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddress() {
        return companyAddress == null ? "" : companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getPremiseName() {
        return premiseName == null ? "" : premiseName;
    }

    public void setPremiseName(String premiseName) {
        this.premiseName = premiseName;
    }

    public String getRejectedOn() {
        return rejectedOn == null ? "" : rejectedOn;
    }

    public void setRejectedOn(String rejectedOn) {
        this.rejectedOn = rejectedOn;
    }
}
