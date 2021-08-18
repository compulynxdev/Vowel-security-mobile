package com.evisitor.data.model;

import com.google.gson.annotations.SerializedName;

public class UserDetail {

    /**
     * username : ram
     * email : device@gmail.com
     * id : 1
     * address : indore
     * contactNo : 9754812548
     * fullName : Ram Singh
     * country : india
     * gender : Male
     * role : DEVICE_ADMIN
     */

    private String username;
    private String email;
    private int id;
    private int userMasterId;
    private String address;
    private String contactNo;
    private String dialingCode;
    private String fullName;
    private String country;
    private String gender;
    private String role;
    private String apiKey;
    private String groupName;
    @SerializedName("image")
    private String imageUrl;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email == null ? "" : email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address == null ? "" : address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNo() {
        return contactNo == null ? "" : contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getFullName() {
        return fullName == null ? "" : fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCountry() {
        return country == null ? "" : country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getGender() {
        return gender == null ? "" : gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getImageUrl() {
        return imageUrl == null ? "" : imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDialingCode() {
        return dialingCode == null ? "" : dialingCode;
    }

    public void setDialingCode(String dialingCode) {
        this.dialingCode = dialingCode;
    }

    public String getGroupName() {
        return groupName == null ? "" : groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getApiKey() {
        return apiKey == null ? "" : apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public int getUserMasterId() {
        return userMasterId;
    }

    public void setUserMasterId(int userMasterId) {
        this.userMasterId = userMasterId;
    }
}
