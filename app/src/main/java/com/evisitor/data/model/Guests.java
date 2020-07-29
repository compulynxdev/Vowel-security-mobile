package com.evisitor.data.model;

import com.google.gson.annotations.SerializedName;

public class Guests {

    @SerializedName("fullName")
    private String name;

    @SerializedName("expectedVehicleNo")
    private String expectedVehicleNo;

    @SerializedName("expectedDate")
    private String time;

    @SerializedName("flatNo")
    private String houseNo;

    @SerializedName("residentName")
    private String host;

    @SerializedName("isOwner")
    private boolean isOwner;

    @SerializedName("residentId")
    private boolean residentId;

    @SerializedName("notificationStatus")
    private boolean notificationStatus;

    @SerializedName("identityNo")
    private String identityNo;

    @SerializedName("contactNo")
    private String contactNo;

    @SerializedName("premiseHierarchyDetailsId")
    private String flatId;

    @SerializedName("id")
    private String guestId;

    @SerializedName("image")
    private String url;

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }

    public boolean isResidentId() {
        return residentId;
    }

    public void setResidentId(boolean residentId) {
        this.residentId = residentId;
    }

    public boolean isNotificationStatus() {
        return notificationStatus;
    }

    public void setNotificationStatus(boolean notificationStatus) {
        this.notificationStatus = notificationStatus;
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

    public String getFlatId() {
        return flatId;
    }

    public void setFlatId(String flatId) {
        this.flatId = flatId;
    }

    public String getGuestId() {
        return guestId;
    }

    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExpectedVehicleNo() {
        return expectedVehicleNo ==null? "" : expectedVehicleNo;
    }

    public void setExpectedVehicleNo(String expectedVehicleNo) {
        this.expectedVehicleNo = expectedVehicleNo;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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
}
