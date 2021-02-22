package com.evisitor.data.model;

import com.google.gson.annotations.SerializedName;

public class Guests {

    @SerializedName("fullName")
    private String name;

    @SerializedName("expectedVehicleNo")
    private String expectedVehicleNo;

    @SerializedName("enteredVehicleNo")
    private String enteredVehicleNo = "";

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

    @SerializedName("createdBy")
    private String createdBy;

    @SerializedName("contactNo")
    private String contactNo;

    @SerializedName("dialingCode")
    private String dialingCode;
    @SerializedName("checkInTime")
    private String checkInTime;
    @SerializedName("checkOutTime")
    private String checkOutTime;
    @SerializedName("premiseHierarchyDetailsId")
    private String flatId = "";
    @SerializedName("flatId")  //for guest check in/out list
    private String flatId2 = "";
    @SerializedName("id")
    private String guestId;
    @SerializedName("checkOutFeature")
    private boolean checkOutFeature;
    @SerializedName("hostCheckOutTime")
    private String hostCheckOutTime;
    @SerializedName("isHostCheckOut")
    private boolean isHostCheckOut;
    @SerializedName("image")
    private String imageUrl;
    @SerializedName("state")
    private String status;
    @SerializedName("checkInStatus")
    private boolean checkInStatus;
    @SerializedName("rejectedBy")
    private String rejectedBy;
    @SerializedName("rejectReason")
    private String rejectedReason;
    @SerializedName("rejectedOn")
    private String rejectedOn;
    @SerializedName("premiseName")
    private String premiseName;

    @SerializedName("nationality")
    private String nationality;

    @SerializedName("gender")
    private String gender;

    @SerializedName("documentType")
    private String documentType;

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getStatus() {
        return status == null ? "PENDING" : status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isCheckOutFeature() {
        return checkOutFeature;
    }

    public void setCheckOutFeature(boolean checkOutFeature) {
        this.checkOutFeature = checkOutFeature;
    }

    public String getHostCheckOutTime() {
        return hostCheckOutTime;
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

    public String getFlatId() {
        return flatId == null ? "" : flatId;
    }

    public void setFlatId(String flatId) {
        this.flatId = flatId;
    }

    public String getGuestId() {
        return guestId == null ? "" : guestId;
    }

    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }

    public String getImageUrl() {
        return imageUrl == null ? "" : imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExpectedVehicleNo() {
        return expectedVehicleNo == null ? "" : expectedVehicleNo;
    }

    public void setExpectedVehicleNo(String expectedVehicleNo) {
        this.expectedVehicleNo = expectedVehicleNo;
    }

    public String getTime() {
        return time == null ? "" : time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHouseNo() {
        return houseNo == null ? "" : houseNo;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    public String getCheckOutTime() {
        return checkOutTime == null ? "" : checkOutTime;
    }

    public void setCheckOutTime(String checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public String getHost() {
        return host == null ? "" : host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getCreatedBy() {
        return createdBy == null ? "" : createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
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

    public String getFlatId2() {
        return flatId2 == null ? "" : flatId2;
    }

    public void setFlatId2(String flatId2) {
        this.flatId2 = flatId2;
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

    public String getNationality() {
        return nationality == null ? "" : nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getDocumentType() {
        return documentType == null ? "" : documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getGender() {
        return gender == null ? "" : gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
