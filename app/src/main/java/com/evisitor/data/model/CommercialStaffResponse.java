package com.evisitor.data.model;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CommercialStaffResponse {
    /**
     * content : [{"country":null,"address":null,"documentType":null,"workingDays":["sunday","monday"],"profile":"Driver","fullName":"SURESH RAINA","employment":"self","checkOutTime":"18:20:02","createdDate":"2020-09-01T08:54:18Z","flatNo":"zxcvbdcfv","createdBy":"superadmin",""documentId":null,"flatId":2,"id":8,"email":null,"checkInTime":"09:20:02","contactNo":"89655552633"}]
     * pageable : {"sort":{"sorted":true,"unsorted":false,"empty":false},"offset":0,"pageNumber":0,"pageSize":10,"unpaged":false,"paged":true}
     * last : true
     * totalElements : 8
     * totalPages : 1
     * size : 10
     * number : 0
     * sort : {"sorted":true,"unsorted":false,"empty":false}
     * numberOfElements : 8
     * first : true
     * empty : false
     */

    private PageableBean pageable;
    private boolean last;
    private int totalElements;
    private int totalPages;
    private int size;
    private int number;
    private SortBeanX sort;
    private int numberOfElements;
    private boolean first;
    private boolean empty;
    private List<ContentBean> content;


    public PageableBean getPageable() {
        return pageable;
    }

    public void setPageable(PageableBean pageable) {
        this.pageable = pageable;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public SortBeanX getSort() {
        return sort;
    }

    public void setSort(SortBeanX sort) {
        this.sort = sort;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public List<ContentBean> getContent() {
        return content;
    }

    public void setContent(List<ContentBean> content) {
        this.content = content;
    }



    public static class PageableBean {
        /**
         * sort : {"sorted":true,"unsorted":false,"empty":false}
         * offset : 0
         * pageNumber : 0
         * pageSize : 10
         * unpaged : false
         * paged : true
         */

        private PageableBean.SortBean sort;
        private int offset;
        private int pageNumber;
        private int pageSize;
        private boolean unpaged;
        private boolean paged;

        public SortBean getSort() {
            return sort;
        }

        public void setSort(SortBean sort) {
            this.sort = sort;
        }

        public int getOffset() {
            return offset;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }

        public int getPageNumber() {
            return pageNumber;
        }

        public void setPageNumber(int pageNumber) {
            this.pageNumber = pageNumber;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public boolean isUnpaged() {
            return unpaged;
        }

        public void setUnpaged(boolean unpaged) {
            this.unpaged = unpaged;
        }

        public boolean isPaged() {
            return paged;
        }

        public void setPaged(boolean paged) {
            this.paged = paged;
        }

        public static class SortBean {
            /**
             * sorted : true
             * unsorted : false
             * empty : false
             */

            private boolean sorted;
            private boolean unsorted;
            private boolean empty;

            public boolean isSorted() {
                return sorted;
            }

            public void setSorted(boolean sorted) {
                this.sorted = sorted;
            }

            public boolean isUnsorted() {
                return unsorted;
            }

            public void setUnsorted(boolean unsorted) {
                this.unsorted = unsorted;
            }

            public boolean isEmpty() {
                return empty;
            }

            public void setEmpty(boolean empty) {
                this.empty = empty;
            }
        }
    }

    public static class SortBeanX {
        /**
         * sorted : true
         * unsorted : false
         * empty : false
         */

        private boolean sorted;
        private boolean unsorted;
        private boolean empty;

        public boolean isSorted() {
            return sorted;
        }

        public void setSorted(boolean sorted) {
            this.sorted = sorted;
        }

        public boolean isUnsorted() {
            return unsorted;
        }

        public void setUnsorted(boolean unsorted) {
            this.unsorted = unsorted;
        }

        public boolean isEmpty() {
            return empty;
        }

        public void setEmpty(boolean empty) {
            this.empty = empty;
        }
    }

    public static class ContentBean {
        /**
         * "content": [
         * {
         * "country": "",
         * "gender": "Male",
         * "documentType": "passport",
         * "workingDays": [
         * "mon",
         * "tue"
         * ],
         * "documentImage": null,
         * "isHostCheckOut": false,
         * "flatId": 3,
         * "id": 4,
         * "email": null,
         * "image": null,
         * "address": "eee",
         * "profile": "ggg",
         * "checkInTime": "2020-12-23T11:13:15.000+0000",
         * "fullName": " bbbb",
         * "employeeId": "ffff",
         * "employment": null,
         * "dialingCode": "1-242",
         * "createdDate": "2020-12-23T11:13:15Z",
         * "checkOutTime": "2020-12-29T11:13:15.000+0000",
         * "flatNo": "DEP-111",
         * "createdBy": "RAJESH VYAS",
         * "documentId": "ffff",
         * "premiseName": "DEP-111,CHL HOSPITAL(RAJSTHAN GARDEN)",
         * "contactNo": "84659595951",
         * "status": true
         */

        @SerializedName("image")
        private String imageUrl;
        private String country;
        private String address;
        private String gender;
        private String documentType;
        private List<String> workingDays;
        private String profile;
        private String documentImage;
        private String fullName;
        private String employeeId;
        private String employment;
        private String dialingCode;
        private boolean isHostCheckOut = false;
        private String createdDate;
        private String flatNo;
        private String createdBy;
        private String documentId;
        private int flatId;
        private int id;
        private String premiseName;
        private String checkOutTime;
        private String checkInTime;
        private String contactNo;
        private String expectedVehicleNo;
        private String enteredVehicleNo = "";
        private String staffId;
        private String timeIn;
        private String timeOut;
        private String nationality;
        private String vehicleImage;
        private Bitmap bitmapVehicleImage;
        private String mode;
        public String bodyTemperature;

        public Bitmap getBitmapVehicleImage() {
            return bitmapVehicleImage;
        }

        public void setBitmapVehicleImage(Bitmap bitmapVehicleImage) {
            this.bitmapVehicleImage = bitmapVehicleImage;
        }

        public String getImageUrl() {
            return imageUrl == null ? "" : imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getCountry() {
            return country == null ? "" : country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getAddress() {
            return address == null ? "" : address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getDocumentType() {
            return documentType == null ? "" : documentType;
        }

        public void setDocumentType(String documentType) {
            this.documentType = documentType;
        }

        public String getProfile() {
            return profile == null ? "" : profile;
        }

        public void setProfile(String profile) {
            this.profile = profile;
        }

        public String getFullName() {
            return fullName == null ? "" : fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getEmployment() {
            return employment == null ? "" : employment;
        }

        public void setEmployment(String employment) {
            this.employment = employment;
        }

        public String getCheckOutTime() {
            return checkOutTime == null ? "" : checkOutTime;
        }

        public void setCheckOutTime(String checkOutTime) {
            this.checkOutTime = checkOutTime;
        }

        public String getCreatedDate() {
            return createdDate == null ? "" : createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public String getFlatNo() {
            return flatNo == null ? "" : flatNo;
        }

        public void setFlatNo(String flatNo) {
            this.flatNo = flatNo;
        }

        public String getCreatedBy() {
            return createdBy == null ? "" : createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        public String getDocumentId() {
            return documentId == null ? "" : documentId;
        }

        public void setDocumentId(String documentId) {
            this.documentId = documentId;
        }

        public int getFlatId() {
            return flatId;
        }

        public void setFlatId(int flatId) {
            this.flatId = flatId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getContactNo() {
            return contactNo == null ? "" : contactNo;
        }

        public void setContactNo(String contactNo) {
            this.contactNo = contactNo;
        }

        public List<String> getWorkingDays() {
            return workingDays == null ? new ArrayList<>() : workingDays;
        }

        public void setWorkingDays(List<String> workingDays) {
            this.workingDays = workingDays;
        }

        public String getExpectedVehicleNo() {
            return expectedVehicleNo == null ? "" : expectedVehicleNo;
        }

        public void setExpectedVehicleNo(String expectedVehicleNo) {
            this.expectedVehicleNo = expectedVehicleNo;
        }

        public String getEnteredVehicleNo() {
            return enteredVehicleNo == null || enteredVehicleNo.isEmpty() ? getExpectedVehicleNo() : enteredVehicleNo;
        }

        public void setEnteredVehicleNo(String enteredVehicleNo) {
            this.enteredVehicleNo = enteredVehicleNo;
        }

        public String getDialingCode() {
            return dialingCode == null ? "" : dialingCode;
        }

        public void setDialingCode(String dialingCode) {
            this.dialingCode = dialingCode;
        }


        public String getPremiseName() {
            return premiseName == null ? "" : premiseName;
        }

        public void setPremiseName(String premiseName) {
            this.premiseName = premiseName;
        }

        public String getEmployeeId() {
            return employeeId == null ? "" : employeeId;
        }

        public void setEmployeeId(String employeeId) {
            this.employeeId = employeeId;
        }

        public String getDocumentImage() {
            return documentImage == null ? "" : documentImage;
        }

        public void setDocumentImage(String documentImage) {
            this.documentImage = documentImage;
        }

        public String getCheckInTime() {
            return checkInTime == null ? "" : checkInTime;
        }

        public void setCheckInTime(String checkInTime) {
            this.checkInTime = checkInTime;
        }

        public String getStaffId() {
            return staffId == null ? "" : staffId;
        }

        public void setStaffId(String staffId) {
            this.staffId = staffId;
        }


        public boolean isHostCheckOut() {
            return isHostCheckOut;
        }

        public void setHostCheckOut(boolean hostCheckOut) {
            isHostCheckOut = hostCheckOut;
        }

        public String getGender() {
            return gender == null ? "" : gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getTimeIn() {
            return timeIn == null ? "" : timeIn;
        }

        public void setTimeIn(String timeIn) {
            this.timeIn = timeIn;
        }

        public String getTimeOut() {
            return timeOut == null ? "" : timeOut;
        }

        public void setTimeOut(String timeOut) {
            this.timeOut = timeOut;
        }

        public String getNationality() {
            return nationality == null ? "" : nationality;
        }

        public void setNationality(String nationality) {
            this.nationality = nationality;
        }

        public String getVehicleImage() {
            return vehicleImage;
        }

        public void setVehicleImage(String vehicleImage) {
            this.vehicleImage = vehicleImage;
        }

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public String getBodyTemperature() {
            return bodyTemperature == null ? "" : bodyTemperature;
        }

        public void setBodyTemperature(String bodyTemperature) {
            this.bodyTemperature = bodyTemperature;
        }
    }



}

