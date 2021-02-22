package com.evisitor.data.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class RecurrentVisitor implements Parcelable {

    public static final Creator<RecurrentVisitor> CREATOR = new Creator<RecurrentVisitor>() {
        @Override
        public RecurrentVisitor createFromParcel(Parcel in) {
            return new RecurrentVisitor(in);
        }

        @Override
        public RecurrentVisitor[] newArray(int size) {
            return new RecurrentVisitor[size];
        }
    };
    private String visitorType;
    @SerializedName("address")
    private String address;
    @SerializedName("image")
    private String image;
    @SerializedName("fullName")
    private String fullName;
    @SerializedName("flatId")
    private int flatId;
    @SerializedName("flatNo")
    private String flatNo;
    @SerializedName("expectedVehicleNo")
    private String expectedVehicleNo;
    @SerializedName("documentType")
    private String documentType;
    @SerializedName("dialingCode")
    private String dialingCode;
    @SerializedName("contactNo")
    private String contactNo;
    @SerializedName("gender")
    private String gender;
    @SerializedName("nationality")
    private String nationality;
    @SerializedName("documentId")
    private String documentId;
    @SerializedName("residentName")
    private String residentName;
    @SerializedName("residentId")
    private int residentId;
    @SerializedName("email")
    private String email;
    @SerializedName("staffId")
    private Object staffId;
    @SerializedName("staffName")
    private String staffName;
    @SerializedName("companyName")
    private String companyName;
    @SerializedName("employment")
    private String employment;
    @SerializedName("companyAddress")
    private String companyAddress;
    @SerializedName("profile")
    private String profile;
    @SerializedName("premiseName")
    private String premiseName;

    public RecurrentVisitor() {

    }

    protected RecurrentVisitor(Parcel in) {
        visitorType = in.readString();
        address = in.readString();
        image = in.readString();
        fullName = in.readString();
        flatId = in.readInt();
        flatNo = in.readString();
        expectedVehicleNo = in.readString();
        documentType = in.readString();
        dialingCode = in.readString();
        contactNo = in.readString();
        gender = in.readString();
        nationality = in.readString();
        documentId = in.readString();
        residentName = in.readString();
        residentId = in.readInt();
        email = in.readString();
        staffName = in.readString();
        companyName = in.readString();
        employment = in.readString();
        companyAddress = in.readString();
        profile = in.readString();
        premiseName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(visitorType);
        dest.writeString(address);
        dest.writeString(image);
        dest.writeString(fullName);
        dest.writeInt(flatId);
        dest.writeString(flatNo);
        dest.writeString(expectedVehicleNo);
        dest.writeString(documentType);
        dest.writeString(dialingCode);
        dest.writeString(contactNo);
        dest.writeString(gender);
        dest.writeString(nationality);
        dest.writeString(documentId);
        dest.writeString(residentName);
        dest.writeInt(residentId);
        dest.writeString(email);
        dest.writeString(staffName);
        dest.writeString(companyName);
        dest.writeString(employment);
        dest.writeString(companyAddress);
        dest.writeString(profile);
        dest.writeString(premiseName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getAddress() {
        return address == null ? "" : address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image == null ? "" : image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFullName() {
        return fullName == null ? "" : fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getFlatId() {
        return flatId;
    }

    public void setFlatId(int flatId) {
        this.flatId = flatId;
    }

    public String getFlatNo() {
        return flatNo == null ? "" : flatNo;
    }

    public void setFlatNo(String flatNo) {
        this.flatNo = flatNo;
    }

    public String getExpectedVehicleNo() {
        return expectedVehicleNo == null ? "" : expectedVehicleNo;
    }

    public void setExpectedVehicleNo(String expectedVehicleNo) {
        this.expectedVehicleNo = expectedVehicleNo;
    }

    public String getDocumentType() {
        return documentType == null ? "" : documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDialingCode() {
        return dialingCode == null ? "" : dialingCode;
    }

    public void setDialingCode(String dialingCode) {
        this.dialingCode = dialingCode;
    }

    public String getContactNo() {
        return contactNo == null ? "" : contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getGender() {
        return gender == null ? "" : gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNationality() {
        return nationality == null ? "" : nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getDocumentId() {
        return documentId == null ? "" : documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getResidentName() {
        return residentName == null ? "" : residentName;
    }

    public void setResidentName(String residentName) {
        this.residentName = residentName;
    }

    public int getResidentId() {
        return residentId;
    }

    public void setResidentId(int residentId) {
        this.residentId = residentId;
    }

    public Object getEmail() {
        return email == null ? "" : email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Object getStaffId() {
        return staffId;
    }

    public void setStaffId(Object staffId) {
        this.staffId = staffId;
    }

    public String getStaffName() {
        return staffName == null ? "" : staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getCompanyName() {
        return companyName == null ? "" : companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getEmployment() {
        return employment == null ? "" : employment;
    }

    public void setEmployment(String employment) {
        this.employment = employment;
    }

    public String getCompanyAddress() {
        return companyAddress == null ? "" : companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getProfile() {
        return profile == null ? "" : profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getVisitorType() {
        return visitorType == null ? "" : visitorType;
    }

    public void setVisitorType(String visitorType) {
        this.visitorType = visitorType;
    }

    public String getPremiseName() {
        return premiseName == null ? "" : premiseName;
    }

    public void setPremiseName(String premiseName) {
        this.premiseName = premiseName;
    }
}