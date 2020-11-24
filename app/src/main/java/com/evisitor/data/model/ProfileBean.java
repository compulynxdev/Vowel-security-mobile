package com.evisitor.data.model;

import androidx.annotation.NonNull;

public class ProfileBean {

    private int id;
    private String profileName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    @NonNull
    @Override
    public String toString() {
        return profileName;
    }
}
