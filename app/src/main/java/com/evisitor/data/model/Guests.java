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
    private String residentId;

    @SerializedName("notificationStatus")
    private boolean notificationStatus;

    @SerializedName("documentId")
    private String identityNo;

    @SerializedName("contactNo")
    private String contactNo;

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    @SerializedName("checkInTime")
    private String checkInTime;

    @SerializedName("premiseHierarchyDetailsId")
    private String flatId;

    @SerializedName("id")
    private String guestId;

    //@SerializedName("image")
    private String url;

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }


    public boolean isNotificationStatus() {
        return notificationStatus;
    }

    public void setNotificationStatus(boolean notificationStatus) {
        this.notificationStatus = notificationStatus;
    }

    public String getResidentId() {
        return residentId;
    }

    public void setResidentId(String residentId) {
        this.residentId = residentId;
    }

    public String getIdentityNo() {
        return identityNo;
    }

    public void setIdentityNo(String identityNo) {
        this.identityNo = identityNo;
    }

    public String getContactNo() {
        return contactNo==null? "": contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getFlatId() {
        return flatId==null? "": flatId;
    }

    public void setFlatId(String flatId) {
        this.flatId = flatId;
    }

    public String getGuestId() {
        return guestId==null?"": guestId;
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
        return time==null?"": time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHouseNo() {
        return houseNo== null? "" : houseNo;

    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    public String getHost() {
        return host==null ? "" : host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
