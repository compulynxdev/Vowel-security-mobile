package com.evisitor.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HouseKeepingCheckInResponse {
    @SerializedName("content")
    private List<HouseKeeping> content;

    public List<HouseKeeping> getContent() {
        return content;
    }

    public void setContent(List<HouseKeeping> content) {
        this.content = content;
    }
}
