package com.evisitor.data.model;

public class GuestConfigurationResponse {

    /**
     * guestFields : {"contactNo":true,"email":true,"gender":true,"address":true}
     */

    private GuestFieldsBean guestFields;

    public GuestFieldsBean getGuestFields() {
        return guestFields == null ? new GuestFieldsBean() : guestFields;
    }

    public void setGuestFields(GuestFieldsBean guestFields) {
        this.guestFields = guestFields;
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
