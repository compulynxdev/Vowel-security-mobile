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
    private String address;
    private String contactNo;
    private String fullName;
    private String country;
    private String gender;
    private String role;
    @SerializedName("image")
    private String imageUrl;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
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
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getGender() {
        return gender;
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
}
