package com.evisitor.data.model;

public class DeviceBean {
    private String sNo;
    private String deviceName;
    private String deviceType;
    private String manufacturer;
    private String tagNo;
    private String serialNo;

    public DeviceBean(String sNo, String name, String type, String manufacturer, String tagNo, String serialNo) {
        this.sNo = sNo;
        this.deviceName = name;
        this.deviceType = type;
        this.manufacturer = manufacturer;
        this.tagNo = tagNo;
        this.serialNo = serialNo;
    }

    public String getsNo() {
        return sNo == null ? "" : sNo;
    }

    public void setsNo(String sNo) {
        this.sNo = sNo;
    }

    public String getDeviceName() {
        return deviceName == null ? "" : deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getType() {
        return deviceType == null ? "" : deviceType;
    }

    public void setType(String type) {
        this.deviceType = type;
    }

    public String getManufacturer() {
        return manufacturer == null ? "" : manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getTagNo() {
        return tagNo == null ? "" : tagNo;
    }

    public void setTagNo(String tagNo) {
        this.tagNo = tagNo;
    }

    public String getSerialNo() {
        return serialNo == null ? "" : serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

}
