package com.evisitor.data.model;

import java.util.List;

public class SPResponse {

    /**
     * content : [{"checkInTime":null,"profile":"isp","companyName":"AIRTEL TELECOM","fullName":"munna sing","expectedDate":"2020-01-01T00:00:00.000+0000","expectedVehicleNo":"mp10mb9654","createdDate":"2020-08-19T06:50:55Z","flatNo":null,"checkOutTime":null,"createdBy":"superadmin","residentName":null,"documentId":"123844845555","id":2,"contactNo":"9854512655"},{"checkInTime":null,"profile":"isp","companyName":"JIO TELECOM","fullName":"kishan sing","expectedDate":"2020-01-01T00:00:00.000+0000","expectedVehicleNo":"mp10mb9654","createdDate":"2020-08-19T06:49:55Z","flatNo":"200","checkOutTime":null,"createdBy":"superadmin","residentName":"RAJA","documentId":"123844845555","id":1,"contactNo":"9854512655"}]
     * pageable : {"sort":{"sorted":true,"unsorted":false,"empty":false},"offset":0,"pageNumber":0,"pageSize":10,"unpaged":false,"paged":true}
     * last : true
     * totalPages : 1
     * totalElements : 2
     * size : 10
     * number : 0
     * sort : {"sorted":true,"unsorted":false,"empty":false}
     * numberOfElements : 2
     * first : true
     * empty : false
     */

    private PageableBean pageable;
    private boolean last;
    private int totalPages;
    private int totalElements;
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

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
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

        private SortBean sort;
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
         * checkInTime : null
         * profile : isp
         * companyName : AIRTEL TELECOM
         * fullName : munna sing
         * expectedDate : 2020-01-01T00:00:00.000+0000
         * expectedVehicleNo : mp10mb9654
         * createdDate : 2020-08-19T06:50:55Z
         * flatNo : null
         * checkOutTime : null
         * createdBy : superadmin
         * residentName : null
         * documentId : 123844845555
         * id : 2
         * contactNo : 9854512655
         */

        private Object checkInTime;
        private String profile;
        private String companyName;
        private String fullName;
        private String expectedDate;
        private String expectedVehicleNo;
        private String createdDate;
        private Object flatNo;
        private Object checkOutTime;
        private String createdBy;
        private Object residentName;
        private String documentId;
        private int id;
        private String contactNo;

        public Object getCheckInTime() {
            return checkInTime;
        }

        public void setCheckInTime(Object checkInTime) {
            this.checkInTime = checkInTime;
        }

        public String getProfile() {
            return profile;
        }

        public void setProfile(String profile) {
            this.profile = profile;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getExpectedDate() {
            return expectedDate;
        }

        public void setExpectedDate(String expectedDate) {
            this.expectedDate = expectedDate;
        }

        public String getExpectedVehicleNo() {
            return expectedVehicleNo;
        }

        public void setExpectedVehicleNo(String expectedVehicleNo) {
            this.expectedVehicleNo = expectedVehicleNo;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public Object getFlatNo() {
            return flatNo;
        }

        public void setFlatNo(Object flatNo) {
            this.flatNo = flatNo;
        }

        public Object getCheckOutTime() {
            return checkOutTime;
        }

        public void setCheckOutTime(Object checkOutTime) {
            this.checkOutTime = checkOutTime;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        public Object getResidentName() {
            return residentName;
        }

        public void setResidentName(Object residentName) {
            this.residentName = residentName;
        }

        public String getDocumentId() {
            return documentId;
        }

        public void setDocumentId(String documentId) {
            this.documentId = documentId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getContactNo() {
            return contactNo;
        }

        public void setContactNo(String contactNo) {
            this.contactNo = contactNo;
        }
    }
}
