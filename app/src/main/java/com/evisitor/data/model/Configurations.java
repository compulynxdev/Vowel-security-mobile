package com.evisitor.data.model;

import com.google.gson.annotations.SerializedName;

public class Configurations {
    @SerializedName("featureCode")
    private String featureCode;
    @SerializedName("featureId")
    private Long featureId;
    @SerializedName("featureName")
    private String featureName;

    @SerializedName("status")
    private boolean status;

    @SerializedName("type")
    private String type;

    @SerializedName("value")
    private String value;

    public String getFeatureCode() {
        return featureCode;
    }

    public void setFeatureCode(String featureCode) {
        this.featureCode = featureCode;
    }

    public Long getFeatureId() {
        return featureId;
    }

    public void setFeatureId(Long featureId) {
        this.featureId = featureId;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean getValue() {
        return stringToBoolean(this.value);
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean stringToBoolean(String value) {
        if (this.value == null) {
            return false;
        }
        if (this.value.equalsIgnoreCase("false")) {
            return false;
        } else if (this.value.equalsIgnoreCase("true")) {
            return true;
        } else return false;
    }
}
