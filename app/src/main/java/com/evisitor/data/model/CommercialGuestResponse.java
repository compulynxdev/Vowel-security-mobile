package com.evisitor.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CommercialGuestResponse {

    @SerializedName("content")
    private List<CommercialGuest> content;

    public List<CommercialGuest> getContent() {
        return content;
    }

    public void setContent(List<CommercialGuest> content) {
        this.content = content;
    }

    public class CommercialGuest {

        @SerializedName("employeeName")
        private String host;

        @SerializedName("image")
        private String imageUrl;

        @SerializedName("gender")
        private String gender;

        @SerializedName("checkInTime")
        private String checkInTime;

        @SerializedName("fullName")
        private String name;

        @SerializedName("expectedDate")
        private String time;

        @SerializedName("enteredVehicleNo")
        private String enteredVehicleNo = "";

        @SerializedName("dialingCode")
        private String dialingCode;

        @SerializedName("expectedVehicleNo")
        private String expectedVehicleNo;

        @SerializedName("createdDate")
        private String createdDate;

        @SerializedName("flatNo")
        private String houseNo;

        @SerializedName("checkOutTime")
        private String checkOutTime;

        @SerializedName("createdBy")
        private String createdBy;

        @SerializedName("documentId")
        private String identityNo;

        @SerializedName("flatId")  //for guest check in/out list
        private String flatId2 = "";

        @SerializedName("id")
        private String guestId;

        @SerializedName("state")
        private String status;

        @SerializedName("guestType")
        private String guestType;

        @SerializedName("premiseName")
        private String premiseName;

        @SerializedName("contactNo")
        private String contactNo;

        @SerializedName("premiseHierarchyDetailsId")
        private String flatId = "";

        @SerializedName("checkOutFeature")
        private boolean checkOutFeature;

        @SerializedName("checkInStatus")
        private boolean checkInStatus;

        @SerializedName("rejectedBy")
        private String rejectedBy;
        @SerializedName("rejectReason")
        private String rejectedReason;
        @SerializedName("rejectedOn")
        private String rejectedOn;

        @SerializedName("purposeOfExit")
        private String purpose;

        @SerializedName("deviceList")
        private List<DeviceBean> deviceBeanList;


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

        public String getPurpose() {
            return purpose == null ? "" : purpose;
        }

        public void setPurpose(String purpose) {
            this.purpose = purpose;
        }

        public List<DeviceBean> getDeviceBeanList() {
            return deviceBeanList == null ? new ArrayList<>() : deviceBeanList;
        }

        public void setDeviceBeanList(List<DeviceBean> deviceBeanList) {
            this.deviceBeanList = deviceBeanList;
        }
    }

}

