package com.evisitor.data.model;

public class ResidentProfile {
    private String fullName;
    private String ownerName;
    private String address;
    private String email;
    private String gender;
    private String primaryNo;
    private String secondaryNo;
    private String image;
    private String relationship;
    private String type;

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
}
