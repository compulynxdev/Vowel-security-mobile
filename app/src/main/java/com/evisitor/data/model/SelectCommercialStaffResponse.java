package com.evisitor.data.model;

import com.google.gson.annotations.SerializedName;

public class SelectCommercialStaffResponse {

    /**
     * profile :  software engineer
     * fullName : SANDEEP EMPLOYEE
     * id : 1
     */

    private String profile;
    private String fullName;
    @SerializedName("image")
    private String imageUrl;
    private int id;

    public String getProfile() {
        return profile == null ? "" : profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getFullName() {
        return fullName == null ? "" : fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl == null ? "" : imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
