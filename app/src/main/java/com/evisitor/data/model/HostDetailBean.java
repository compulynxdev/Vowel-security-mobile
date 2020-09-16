package com.evisitor.data.model;

import androidx.annotation.NonNull;

public class HostDetailBean {

    /**
     * id : 1
     * fullName : raja
     * isOwner : true
     */

    private int id;
    private String fullName;
    private boolean isOwner;

    public HostDetailBean() {

    }

    public HostDetailBean(int id, String fullName, boolean isOwner) {
        this.id = id;
        this.fullName = fullName;
        this.isOwner = isOwner;
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

    public boolean isIsOwner() {
        return isOwner;
    }

    public void setIsOwner(boolean isOwner) {
        this.isOwner = isOwner;
    }

    @NonNull
    @Override
    public String toString() {
        return fullName;
    }
}
