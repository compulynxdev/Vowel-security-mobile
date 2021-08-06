package com.evisitor.data.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SecondaryGuest {

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("count")
    private String count;

    @SerializedName("contactNo")
    private String contactNo;

    @SerializedName("dialingCode")
    private String dialingCode;

    @SerializedName("documentType")
    private String documentType = "359";

    @SerializedName("documentId")
    private String documentId = "";

    @SerializedName("address")
    private String address;

    @SerializedName("bodyTemperature")
    private String bodyTemperature;

    @SerializedName("minor")
    private boolean minor = false;

    public String getDialingCode() {
        return dialingCode == null ? "" : dialingCode;
    }

    public void setDialingCode(String dialingCode) {
        this.dialingCode = dialingCode;
    }

    @SerializedName("id")
    private int id;

    boolean isPrimary;

    public String getFullName() {
        return fullName ==null ? "" : fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getContactNo() {
        return contactNo  ==null ? "" : contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }



    public String getDocumentType() {
        return documentType  ==null ? "" : documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentId() {
        return documentId  ==null ? "" : documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getAddress() {
        return address  ==null ? "" : address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public SecondaryGuest(String fullName, String count, String contactNo, String dialingCode, String documentType, String documentId, String address, boolean minor, int id) {
        this.fullName = fullName;
        this.count = count;
        this.contactNo = contactNo;
        this.dialingCode = dialingCode;
        this.documentType = documentType;
        this.documentId = documentId;
        this.address = address;
        this.minor = minor;
        this.id = id;
    }

    public String getCount() {
        return count  ==null ? "" : count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public boolean isMinor() {
        return minor;
    }


    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMinor(boolean minor) {
        this.minor = minor;
    }

    public String getBodyTemperature() {
        return bodyTemperature == null ? "" : bodyTemperature;
    }

    public void setBodyTemperature(String bodyTemperature) {
        this.bodyTemperature = bodyTemperature;
    }
}
