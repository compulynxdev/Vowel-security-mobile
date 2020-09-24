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
    private int propertyType;
    private String propertyTypeName = "";
    private int id;
    private String fullName = "";
    private String zipCode = "";
    private String address = "";
    private String country = "";

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getExtensionNo() {
        return extensionNo;
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
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(int propertyType) {
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
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
