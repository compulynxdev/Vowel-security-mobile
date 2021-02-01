package com.evisitor.data.model;

public class PropertyInfoResponse {

    /**
     * contactNo : 88878878
     * extensionNo : 87878778
     * status : true
     * email : india@gmail.com
     * image : 1eac401f-6d6c-4657-96cf-ff5942889396.png
     * propertyType : 1
     * propertyTypeName : RESIDENTY
     * id : 1
     * fullName : ACCOUNT
     * zipCode : 454545
     * address : INDORE
     * country : india
     */

    private String contactNo = "";
    private String extensionNo = "";
    private boolean status;
    private String email = "";
    private String image = "";
    private String propertyType;
    private String propertyTypeName = "";
    private int id;
    private String fullName = "";
    private String zipCode = "";
    private String address = "";
    private String country = "";
    private String dialingCode = "";
    private String extDialingCode = "";

    public String getContactNo() {
        return contactNo == null ? "" : contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getExtensionNo() {
        return extensionNo == null ? "" : extensionNo;
    }

    public void setExtensionNo(String extensionNo) {
        this.extensionNo = extensionNo;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getEmail() {
        return email == null ? "" : email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image == null ? "" : image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPropertyType() {
        return propertyType == null ? "" : propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getPropertyTypeName() {
        return propertyTypeName;
    }

    public void setPropertyTypeName(String propertyTypeName) {
        this.propertyTypeName = propertyTypeName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName == null ? "" : fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getZipCode() {
        return zipCode == null ? "" : zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getAddress() {
        return address == null ? "" : address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country == null ? "" : country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDialingCode() {
        return dialingCode == null ? "" : dialingCode;
    }

    public void setDialingCode(String dialingCode) {
        this.dialingCode = dialingCode;
    }

    public String getExtDialingCode() {
        return extDialingCode == null ? "" : extDialingCode;
    }

    public void setExtDialingCode(String extDialingCode) {
        this.extDialingCode = extDialingCode;
    }
}
