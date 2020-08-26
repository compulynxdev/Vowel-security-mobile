package com.evisitor.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServiceProviderResponse {

    @SerializedName("content")
    private List<ServiceProvider> content;

    public List<ServiceProvider> getContent() {
        return content;
    }

    public void setContent(List<ServiceProvider> content) {
        this.content = content;
    }
}
