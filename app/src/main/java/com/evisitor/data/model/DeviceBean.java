package com.evisitor.data.model;

public class DeviceBean {
    private String sNo;
    private String name;
    private String type;
    private String manufacturer;
    private String tagNo;
    private String serialNo;

    public DeviceBean(String sNo, String name, String type, String manufacturer, String tagNo, String serialNo) {
        this.sNo = sNo;
        this.name = name;
        this.type = type;
        this.manufacturer = manufacturer;
        this.tagNo = tagNo;
        this.serialNo = serialNo;
    }

    public String getsNo() {
        return sNo;
    }

    public void setsNo(String sNo) {
        this.sNo = sNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getTagNo() {
        return tagNo;
    }

    public void setTagNo(String tagNo) {
        this.tagNo = tagNo;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }
}
