package com.evisitor.data.model;

public class GuestConfigurationResponse {

    /**
     * guestField : {"contactNo":true,"email":true,"gender":true,"address":true}
     */

    public boolean isDataUpdated = false;
    private GuestFieldsBean guestField;
    private boolean guestGroupFeature = false;
    private SecondGuestFieldsBean secGuestField;

    public GuestFieldsBean getGuestField() {
        return guestField == null ? new GuestFieldsBean() : guestField;
    }

    public void setGuestField(GuestFieldsBean guestField) {
        this.guestField = guestField;
    }

    public SecondGuestFieldsBean getSecGuestField() {
        return secGuestField == null ? new SecondGuestFieldsBean() : secGuestField;
    }

    public void setSecGuestField(SecondGuestFieldsBean secGuestField) {
        this.secGuestField = secGuestField;
    }

    public boolean isGuestGroupFeature() {
        return guestGroupFeature;
    }

    public void setGuestGroupFeature(boolean guestGroupFeature) {
        this.guestGroupFeature = guestGroupFeature;
    }

    public static class GuestFieldsBean {
        /**
         * contactNo : true
         * email : true
         * gender : true
         * address : true
         */

        private boolean contactNo;
        private boolean email;
        private boolean gender;
        private boolean address;

        public boolean isContactNo() {
            return contactNo;
        }

        public void setContactNo(boolean contactNo) {
            this.contactNo = contactNo;
        }

        public boolean isEmail() {
            return email;
        }

        public void setEmail(boolean email) {
            this.email = email;
        }

        public boolean isGender() {
            return gender;
        }

        public void setGender(boolean gender) {
            this.gender = gender;
        }

        public boolean isAddress() {
            return address;
        }

        public void setAddress(boolean address) {
            this.address = address;
        }
    }


    public static class SecondGuestFieldsBean {
        private boolean secAddress=false;
        private boolean secFullname=false;
        private boolean secDocumentID=false;
        private boolean secContactNo=false;

        public boolean isSecAddress() {
            return secAddress;
        }

        public void setSecAddress(boolean secAddress) {
            this.secAddress = secAddress;
        }

        public boolean isSecFullname() {
            return secFullname;
        }

        public void setSecFullname(boolean secFullname) {
            this.secFullname = secFullname;
        }

        public boolean isSecDocumentID() {
            return secDocumentID;
        }

        public void setSecDocumentID(boolean secDocumentID) {
            this.secDocumentID = secDocumentID;
        }

        public boolean isSecContactNo() {
            return secContactNo;
        }

        public void setSecContactNo(boolean secContactNo) {
            this.secContactNo = secContactNo;
        }
    }
}
