package com.evisitor.data.model;

import java.util.List;

public class TrespasserResponse {
    /**
     * content : [{"country":null,"address":null,"documentType":null,"workingDays":["sunday","monday"],"profile":"Driver","fullName":"SURESH RAINA","employment":"self","timeOut":"18:20:02","createdDate":"2020-09-01T08:54:18Z","flatNo":"zxcvbdcfv","createdBy":"superadmin","residentName":"zxdcfvb","documentId":null,"flatId":2,"residentId":1,"id":8,"email":null,"timeIn":"09:20:02","contactNo":"89655552633"}]
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
         "createdDate" : "2020-09-07T04:49:18Z",
         "fullName" : "account",
         "flatNo" : "200",
         "checkOutTime" : "RANDOM_VISITOR",
         "gender" : "Male",
         "createdBy" : "GUEST",
         "checkInTime" : "2020-09-04T04:43:27Z",
         "createdBy" : "account",
         "documentId" : "121212",
         "id" : 1,
         "enteredVehicleNo" : 1,
         "hostCheckoutTime" : 1,
         "contactNo" : "978784664"*/


        private String fullName;
        private String flatNo;
        private String createdDate;
        private String createdBy;
        private String hostCheckoutTime;
        private String checkInTime;
        private String documentId;
        private String gender;
        private String enteredVehicleNo;
        private String contactNo;
        private int id;

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getFlatNo() {
            return flatNo;
        }

        public void setFlatNo(String flatNo) {
            this.flatNo = flatNo;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        public String getHostCheckoutTime() {
            return hostCheckoutTime;
        }

        public void setHostCheckoutTime(String hostCheckoutTime) {
            this.hostCheckoutTime = hostCheckoutTime;
        }

        public String getCheckInTime() {
            return checkInTime;
        }

        public void setCheckInTime(String checkInTime) {
            this.checkInTime = checkInTime;
        }

        public String getDocumentId() {
            return documentId == null ? "" : documentId;
        }

        public void setDocumentId(String documentId) {
            this.documentId = documentId;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getEnteredVehicleNo() {
            return enteredVehicleNo;
        }

        public void setEnteredVehicleNo(String enteredVehicleNo) {
            this.enteredVehicleNo = enteredVehicleNo;
        }

        public String getContactNo() {
            return contactNo;
        }

        public void setContactNo(String contactNo) {
            this.contactNo = contactNo;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

}
