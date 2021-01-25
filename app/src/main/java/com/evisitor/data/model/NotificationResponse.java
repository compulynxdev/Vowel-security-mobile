package com.evisitor.data.model;

import java.util.ArrayList;
import java.util.List;

public class NotificationResponse {

    /**
     * content : [{"reason":"Check in notification","notificationStatus":"PENDING","createdDate":"2020-09-16T12:38:38Z","flatNo":"200","createdBy":"hemant","residentName":"RESIDENT","id":4,"type":"HOUSE_HELP"},{"reason":"Check in notification","notificationStatus":"ACCEPT","createdDate":"2020-09-16T11:47:45Z","flatNo":"200","createdBy":"hemant","residentName":"RESIDENT","fullName":"GUEST","id":3,"type":"GUEST"},{"reason":"Check in notification","notificationStatus":"ACCEPT","createdDate":"2020-09-16T11:10:25Z","flatNo":"200","createdBy":"hemant","residentName":"RESIDENT","fullName":"Guest 2","id":2,"type":"GUEST"},{"reason":"Check in notification","notificationStatus":"REJECT","createdDate":"2020-09-16T11:10:07Z","flatNo":"200","createdBy":"hemant","residentName":"RESIDENT","fullName":"Guest 3","id":1,"type":"GUEST"}]
     * pageable : {"sort":{"sorted":true,"unsorted":false,"empty":false},"pageNumber":0,"pageSize":20,"offset":0,"paged":true,"unpaged":false}
     * last : true
     * totalPages : 1
     * totalElements : 4
     * numberOfElements : 4
     * first : true
     * sort : {"sorted":true,"unsorted":false,"empty":false}
     * size : 20
     * number : 0
     * empty : false
     */

    private PageableBean pageable;
    private boolean last;
    private int totalPages;
    private int totalElements;
    private int numberOfElements;
    private boolean first;
    private SortBeanX sort;
    private int size;
    private int number;
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

    public SortBeanX getSort() {
        return sort;
    }

    public void setSort(SortBeanX sort) {
        this.sort = sort;
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

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public List<ContentBean> getContent() {
        return content == null ? new ArrayList<>() : content;
    }

    public void setContent(List<ContentBean> content) {
        this.content = content;
    }

    public static class PageableBean {
        /**
         * sort : {"sorted":true,"unsorted":false,"empty":false}
         * pageNumber : 0
         * pageSize : 20
         * offset : 0
         * paged : true
         * unpaged : false
         */

        private SortBean sort;
        private int pageNumber;
        private int pageSize;
        private int offset;
        private boolean paged;
        private boolean unpaged;

        public SortBean getSort() {
            return sort;
        }

        public void setSort(SortBean sort) {
            this.sort = sort;
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

        public int getOffset() {
            return offset;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }

        public boolean isPaged() {
            return paged;
        }

        public void setPaged(boolean paged) {
            this.paged = paged;
        }

        public boolean isUnpaged() {
            return unpaged;
        }

        public void setUnpaged(boolean unpaged) {
            this.unpaged = unpaged;
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
         * reason : Check in notification
         * notificationStatus : PENDING
         * createdDate : 2020-09-16T12:38:38Z
         * flatNo : 200
         * createdBy : hemant
         * residentName : RESIDENT
         * id : 4
         * type : HOUSE_HELP
         * fullName : GUEST
         */

        private String reason;
        private String notificationStatus;
        private String createdDate;
        private String flatNo;
        private String createdBy;
        private String residentName;
        private int id;
        private String type;
        private String fullName;
        private String companyName;
        private String companyAddress;
        private String staffName;

        public String getReason() {
            return reason == null ? "" : reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getNotificationStatus() {
            return notificationStatus == null ? "" : notificationStatus;
        }

        public void setNotificationStatus(String notificationStatus) {
            this.notificationStatus = notificationStatus;
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

        public String getResidentName() {
            return residentName == null ? "" : residentName;
        }

        public void setResidentName(String residentName) {
            this.residentName = residentName;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getType() {
            return type == null ? "" : type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getFullName() {
            return fullName == null ? "" : fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getCompanyName() {
            return companyName == null ? "" : companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getCompanyAddress() {
            return companyAddress == null ? "" : companyAddress;
        }

        public void setCompanyAddress(String companyAddress) {
            this.companyAddress = companyAddress;
        }

        public String getStaffName() {
            return staffName == null ? "" : staffName;
        }

        public void setStaffName(String staffName) {
            this.staffName = staffName;
        }
    }
}
