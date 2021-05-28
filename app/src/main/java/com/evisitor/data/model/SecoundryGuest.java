package com.evisitor.data.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SecoundryGuest {






    @SerializedName("fullName")
    private String fullName;

    @SerializedName("count")
    private String count;

    @SerializedName("contactNo")
    private String contactNo;

    @SerializedName("dialingCode")
    private String dialingCode="359";

    @SerializedName("documentType")
    private String documentType = "";

    @SerializedName("documentId")
    private String documentId = "";

    @SerializedName("address")
    private String address;


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }



    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public SecoundryGuest(String count, String name, String contactNo, String dailingCode, String documentType, String documentId, String address) {
        this.fullName = name;
        this.count = count;
        this.contactNo = contactNo;
        this.dialingCode = dailingCode;
        this.documentType = documentType;
        this.documentId = documentId;
        this.address = address;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }


}
