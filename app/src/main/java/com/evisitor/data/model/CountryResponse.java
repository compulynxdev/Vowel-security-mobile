package com.evisitor.data.model;

public class CountryResponse {

    /**
     * name : Afghanistan
     * dial_code : +93
     * code : AF
     * nationality : Afghan
     */

    private String name;
    private String dial_code;
    private String code;
    private String nationality;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDial_code() {
        return dial_code;
    }

    public void setDial_code(String dial_code) {
        this.dial_code = dial_code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
}
