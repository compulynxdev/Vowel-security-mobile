package com.evisitor.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * {
 *     "country": "United Kingdom of Great Britain and Northern Ireland",
 *     "country_code": "GBR",
 *     "date_of_birth": "880911",
 *     "date_of_expiry": "250310",
 *     "first_name": "ANGELA ZOE",
 *     "last_name": "ST HELENA SPECIMEN",
 *     "nationality": "United Kingdom of Great Britain and Northern Ireland",
 *     "pasport_number": "760641560",
 *     "passport_type": "P<",
 *     "sex": "F"
 * }
 */
public class MrzResponse {

    @SerializedName("country")
    private String country;

    @SerializedName("country_code")
    private String country_code;

    @SerializedName("date_of_birth")
    private String date_of_birth;

    @SerializedName("date_of_expiry")
    private String date_of_expiry;

    @SerializedName("first_name")
    private String first_name;

    @SerializedName("last_name")
    private String last_name;

    @SerializedName("nationality")
    private String nationality;

    @SerializedName("pasport_number")
    private  String pasport_number;

    @SerializedName("passport_type")
    private String passport_type;

    @SerializedName("sex")
    private String sex;

    public MrzResponse(String result) {
    }

    public String getCountry() {
        return country;
    }

    public String getCountry_code() {
        return country_code;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public String getDate_of_expiry() {
        return date_of_expiry;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getNationality() {
        return nationality;
    }

    public String getPasport_number() {
        return pasport_number;
    }

    public String getPassport_type() {
        return passport_type;
    }

    public String getSex() {
        return sex;
    }
}
