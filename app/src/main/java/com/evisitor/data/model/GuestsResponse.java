package com.evisitor.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GuestsResponse {
    @SerializedName("content")
    private List<Guests> content;

    public List<Guests> getContent() {
        return content;
    }

    public void setContent(List<Guests> content) {
        this.content = content;
    }
}
