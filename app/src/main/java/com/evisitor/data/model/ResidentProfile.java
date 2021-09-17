package com.evisitor.data.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
   "image" : "67deb144-3549-4010-b40c-a78725c56986.png",
   "address" : "eeee",
   "gender" : "Male",
   "fullName" : "TESTING",
   "type" : "OWNER",
   "secDialingCode" : "",
   "dialingCode" : "973",
   "accountId" : 2,
   "primaryNo" : "555454555",
   "flatNo" : "flat-5555 ",
   "qrDate" : "2021-01-26T06:30:41.000+0000",
   "secondaryNo" : null,
   "flatId" : 10,
   "id" : 1,
   "relationship" : null,
   "email" : null,
   "premiseName" : "flat-5555 ,block-011(RESIDENTILA PROPERTY)"
*/

public class ResidentProfile implements Serializable {
    private String fullName;
    private String ownerName;
    private String address;
    private String premiseName;
    private String email;
    private String gender;
    private String secDialingCode;
    private String dialingCode;
    private String primaryNo;
    private String secondaryNo;
    private String image;
    private String relationship;
    private String type;
    private int id = 0;
    private int primaryId = 0;
    private List<String> vehicleList;

    public String getFullName() {
        return fullName == null ? "" : fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getOwnerName() {
        return ownerName == null ? "" : ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getAddress() {
        return address == null ? "" : address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email == null ? "" : email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender == null ? "" : gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPrimaryNo() {
        return primaryNo == null ? "" : primaryNo;
    }

    public void setPrimaryNo(String primaryNo) {
        this.primaryNo = primaryNo;
    }

    public String getSecondaryNo() {
        return secondaryNo == null ? "" : secondaryNo;
    }

    public void setSecondaryNo(String secondaryNo) {
        this.secondaryNo = secondaryNo;
    }

    public String getImage() {
        return image == null ? "" : image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRelationship() {
        return relationship == null ? "" : relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getType() {
        return type == null ? "" : type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPremiseName() {
        return premiseName == null ? "" : premiseName;
    }

    public void setPremiseName(String premiseName) {
        this.premiseName = premiseName;
    }

    public String getSecDialingCode() {
        return secDialingCode == null ? "" : secDialingCode;
    }

    public void setSecDialingCode(String secDialingCode) {
        this.secDialingCode = secDialingCode;
    }

    public String getDialingCode() {
        return dialingCode == null ? "" : dialingCode;
    }

    public void setDialingCode(String dialingCode) {
        this.dialingCode = dialingCode;
    }

    public List<String> getVehicleList() {
        return vehicleList == null ? new ArrayList<>() : vehicleList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setVehicleList(List<String> vehicleList) {
        this.vehicleList = vehicleList;
    }

    public int getPrimaryId() {
        return primaryId;
    }

    public void setPrimaryId(int primaryId) {
        this.primaryId = primaryId;
    }
}
