package com.evisitor.data.model;

import androidx.annotation.NonNull;

public class IdentityBean {
    private String title;
    private String key;

    public IdentityBean(String title, String key) {
        this.title = title;
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @NonNull
    @Override
    public String toString() {
        return title;
    }
}
