package com.evisitor.data.model;

public class GuestConfigurationResponse {

    /**
     * guestField : {"contactNo":true,"email":true,"gender":true,"address":true}
     */

    private GuestFieldsBean guestField;

    public GuestFieldsBean getGuestField() {
        return guestField == null ? new GuestFieldsBean() : guestField;
    }

    public void setGuestField(GuestFieldsBean guestField) {
        this.guestField = guestField;
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
}
