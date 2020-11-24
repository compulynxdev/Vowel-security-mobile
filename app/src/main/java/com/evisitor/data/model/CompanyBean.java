package com.evisitor.data.model;

import androidx.annotation.NonNull;

public class CompanyBean {

    private int id;

    private String companyName;

    private String companyAddress;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    @NonNull
    @Override
    public String toString() {
        return companyName;
    }
}
