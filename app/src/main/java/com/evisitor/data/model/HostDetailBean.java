package com.evisitor.data.model;

import androidx.annotation.NonNull;

public class HostDetailBean {


    /**
     * status : true
     * fullName : RAJ KUMAR HIRANI
     * type : OWNER
     * id : 1
     * contactNo : 8965699
     * gender : Male
     * email : RAJ@GMAIL.COM
     */

    private boolean status;
    private String fullName;
    private String type;
    private int id;
    private String contactNo;
    private String gender;
    private String email;

    public HostDetailBean() {

    }

    public HostDetailBean(int id, String fullName) {
        this.fullName = fullName;
        this.id = id;
    }

    @NonNull
    @Override
    public String toString() {
        return fullName;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
