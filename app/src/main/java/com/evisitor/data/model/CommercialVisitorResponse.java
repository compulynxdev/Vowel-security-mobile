package com.evisitor.data.model;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CommercialVisitorResponse {

    @SerializedName("content")
    private List<CommercialGuest> content;

    public List<CommercialGuest> getContent() {
        return content;
    }

    public void setContent(List<CommercialGuest> content) {
        this.content = content;
    }

    public static class CommercialGuest {
        @SerializedName("bmp_profile")
        public Bitmap bmp_profile;
        @SerializedName("no_plate_bmp_img")
        public Bitmap no_plate_bmp_img;
        @SerializedName("vehicleImage")
        public String vehicleImage;
        @SerializedName("groupType")
        public String groupType;
        public String bodyTemperature;
        @SerializedName("guestList")
        public List<SecondaryGuest> guestList;
        @SerializedName("flagedVisitorStatus")
        public boolean flagedVisitorStatus = false;
        public boolean minor = false;
        @SerializedName("qrCode")
        private String qrCode;
        @SerializedName("employeeName")
        private String host;
        @SerializedName("image")
        private String imageUrl;
        @SerializedName("checkInApproved")
        private boolean checkInApproved;
        @SerializedName("premiseHierarchyDetailsId")
        private String flatId = "";
        @SerializedName("fullName")
        private String name;
        @SerializedName("employeeId")
        private String staffId;
        @SerializedName("expectedDate")
        private String time;
        @SerializedName("dialingCode")
        private String dialingCode;
        @SerializedName("expectedVehicleNo")
        private String expectedVehicleNo;
        @SerializedName("createdDate")
        private String createdDate;
        @SerializedName("flatNo")
        private String houseNo;
        @SerializedName("isVip")
        private boolean isVip;
        @SerializedName("createdBy")
        private String createdBy;
        @SerializedName("deviceList")
        private List<DeviceBean> deviceBeanList;
        @SerializedName("documentId")
        private String identityNo;
        @SerializedName("id")
        private String guestId;
        @SerializedName("state")
        private String status;
        @SerializedName("premiseName")
        private String premiseName;
        @SerializedName("contactNo")
        private String contactNo;
        @SerializedName("gender")
        private String gender;
        @SerializedName("checkInTime")
        private String checkInTime;
        @SerializedName("enteredVehicleNo")
        private String enteredVehicleNo = "";
        @SerializedName("checkOutTime")
        private String checkOutTime;
        @SerializedName("flatId")  //for guest check in/out list
        private String flatId2 = "";
        @SerializedName("guestType")
        private String guestType;
        @SerializedName("checkOutFeature")
        private boolean checkOutFeature;
        @SerializedName("checkInStatus")
        private boolean checkInStatus;
        @SerializedName("rejectedBy")
        private String rejectedBy;

//        @SerializedName("noPlateImage")
//        private String noPlateImage;
        @SerializedName("rejectReason")
        private String rejectedReason;
        @SerializedName("rejectedOn")
        private String rejectedOn;
        @SerializedName("purposeOfExit")
        private String purpose;
        @SerializedName("hostCheckOutTime")
        private String hostCheckOutTime;
        @SerializedName("nationality")
        private String nationality;
        @SerializedName("documentType")
        private String documentType;
        @SerializedName("mode")
        private String mode;

        @SerializedName("enteredVehicleModel")
        private String enteredVehicleModel;

        public String getQrCode() {
            return qrCode;
        }

        public void setQrCode(String qrCode) {
            this.qrCode = qrCode;
        }

        public String getCheckInTime() {
            return checkInTime;
        }

        public void setCheckInTime(String checkInTime) {
            this.checkInTime = checkInTime;
        }

        public String getStatus() {
            if (status == null) {
                return "PENDING";
            } else if (status.equals("")) {
                return "PENDING APPROVAL";
            } else return status;
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
            return name == null ? "" : name;
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
            return checkOutTime;
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

        public String getStaffId() {
            return staffId;
        }

        public void setStaffId(String staffId) {
            this.staffId = staffId;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public String getHostCheckOutTime() {
            return hostCheckOutTime == null ? "" : hostCheckOutTime;
        }

        public void setHostCheckOutTime(String hostCheckOutTime) {
            this.hostCheckOutTime = hostCheckOutTime;
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

        public Bitmap getBmp_profile() {
            return bmp_profile;
        }

        public void setBmp_profile(Bitmap bmp_profile) {
            this.bmp_profile = bmp_profile;
        }

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public Bitmap getNo_plate_bmp_img() {
            return no_plate_bmp_img;
        }

        public void setNo_plate_bmp_img(Bitmap no_plate_bmp_img) {
            this.no_plate_bmp_img = no_plate_bmp_img;
        }

        public String getVehicleImage() {
            return vehicleImage;
        }

        public void setVehicleImage(String vehicleImage) {
            this.vehicleImage = vehicleImage;
        }

        public String getGuestType() {
            return guestType;
        }

        public void setGuestType(String guestType) {
            this.guestType = guestType;
        }

        public String getGroupType() {
            return groupType;
        }

        public void setGroupType(String groupType) {
            this.groupType = groupType;
        }

        public List<SecondaryGuest> getGuestList() {
            return guestList == null ? new ArrayList<>() : guestList;
        }

        public void setGuestList(List<SecondaryGuest> guestList) {
            this.guestList = guestList;
        }

        public String getBodyTemperature() {
            return bodyTemperature == null ? "" : bodyTemperature;
        }

        public void setBodyTemperature(String bodyTemperature) {
            this.bodyTemperature = bodyTemperature;
        }

        public boolean isMinor() {
            return minor;
        }

        public void setMinor(boolean minor) {
            this.minor = minor;
        }

        public boolean isFlagedVisitorStatus() {
            return flagedVisitorStatus;
        }

        public void setFlagedVisitorStatus(boolean flagedVisitorStatus) {
            this.flagedVisitorStatus = flagedVisitorStatus;
        }

        public boolean isCheckInApproved() {
            return checkInApproved;
        }

        public void setCheckInApproved(boolean checkInApproved) {
            this.checkInApproved = checkInApproved;
        }

        public boolean isVip() {
            return isVip;
        }

        public void setVip(boolean vip) {
            isVip = vip;
        }

        public String getEnteredVehicleModel() {
            return enteredVehicleModel;
        }

        public void setEnteredVehicleModel(String enteredVehicleModel) {
            this.enteredVehicleModel = enteredVehicleModel;
        }
    }


}

